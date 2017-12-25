package com.baislsl.spreadsheet.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.ArrayList;


public class CsvTable extends ScrolledComposite implements CsvEditor {
    private final static Logger log = LoggerFactory.getLogger(CsvTable.class);

    private CSVReader reader;
    private final Table table;

    public CsvTable(Composite parent) {
        this(parent, SWT.V_SCROLL | SWT.H_SCROLL);
    }

    public CsvTable(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout());
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        table = new Table(this, SWT.NO_SCROLL | SWT.FULL_SELECTION);
        setUpTable();

        setContent(table);
        setExpandHorizontal(true);
        setExpandVertical(true);
        setAlwaysShowScrollBars(true);
        setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private void setUpTable() {
        table.setLinesVisible(true);

        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        table.addMouseListener(new MouseAdapter() {
            int col = -1, row = -1;

            @Override
            public void mouseDoubleClick(MouseEvent event) {
                Control old = editor.getEditor();
                if (old != null)
                    old.dispose();

                Point pt = new Point(event.x, event.y);

                for (int i = 0; i < table.getItemCount(); i++) {
                    Rectangle rect = table.getItem(i).getBounds();
                    if (rect.y <= event.y && event.y <= rect.y + rect.height) {
                        row = i;
                        break;
                    }
                }
                if (row == -1) return;

                final TableItem item = table.getItem(row);
                if (item == null) return;
                for (int i = 0, n = table.getColumnCount(); i < n; i++) {
                    Rectangle rect = item.getBounds(i);
                    if (rect.contains(pt)) {
                        col = i;
                        break;
                    }
                }
                if (col == -1) return;

                final Text text = new Text(table, SWT.NONE);
                text.setForeground(item.getForeground());
                text.setText(item.getText(col));
                text.setForeground(item.getForeground());
                text.selectAll();
                text.setFocus();

                editor.minimumWidth = text.getBounds().width;
                editor.setEditor(text, item, col);
                text.addModifyListener(new ModifyListener() {
                    public void modifyText(ModifyEvent event) {
                        setContent(col, row, text.getText());
                    }
                });
            }
        });
    }

    @Override
    public void load(CSVReader reader) {
        this.reader = reader;
        table.clearAll();
        int columnCount = table.getColumnCount();

        for (String[] items : reader) {
            if (items.length > columnCount) {
                for (int i = columnCount; i < items.length; i++) {
                    new TableColumn(table, SWT.NONE);
                }
                columnCount = items.length;
            }
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(items);
        }
        for (int i = 0; i < columnCount; i++) {
            table.getColumn(i).pack();
        }
        table.pack();
        setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    @Override
    public void save(CSVWriter csvWriter) throws IOException {
        int col = table.getColumnCount();
        int row = table.getItemCount();
        for (int i = 0; i < row; i++) {
            java.util.List<String> texts = new ArrayList<>();
            for (int j = 0; j < col; j++) {
                texts.add(table.getItem(i).getText(j));
            }
            csvWriter.writeNext(texts.toArray(new String[0]));
        }
        csvWriter.close();
    }

    @Override
    public String getContent(int col, int row) {
        return table.getItem(row).getText(col);
    }

    @Override
    public void setContent(int col, int row, String content) {
        table.getItem(row).setText(col, content);
        table.getColumn(col).pack();
    }
}
