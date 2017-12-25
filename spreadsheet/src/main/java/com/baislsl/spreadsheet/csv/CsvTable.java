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
import java.util.List;


public class CsvTable extends ScrolledComposite implements CsvEditor {
    private final static Logger log = LoggerFactory.getLogger(CsvTable.class);

    private Table table;
    private TableEditor editor;
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseDoubleClick(MouseEvent event) {
            Control old = editor.getEditor();
            if (old != null)
                old.dispose();
            Point location = fetchLocation(new Point(event.x, event.y));
            if (location == null)
                return;

            int col= location.x, row = location.y;
            TableItem item = table.getItem(row);
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
    };

    public CsvTable(Composite parent) {
        this(parent, SWT.V_SCROLL | SWT.H_SCROLL);
    }

    public CsvTable(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout());
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        setExpandHorizontal(true);
        setExpandVertical(true);
        setAlwaysShowScrollBars(true);
        setUpTable();
    }

    private void setUpTable() {
        if(table != null) table.dispose();
        table = new Table(this, SWT.NO_SCROLL | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        table.addMouseListener(mouseAdapter);
        setContent(table);
        setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private Point fetchLocation(Point pt) {
        Point ans = new Point(-1, -1);
        for (int i = 0; i < table.getItemCount(); i++) {
            Rectangle rect = table.getItem(i).getBounds();
            if (rect.y <= pt.y && pt.y <= rect.y + rect.height) {
                ans.y = i;
                break;
            }
        }
        if (ans.y == -1) return null;

        TableItem item = table.getItem(ans.y);
        if (item == null) return null;
        for (int i = 0, n = table.getColumnCount(); i < n; i++) {
            Rectangle rect = item.getBounds(i);
            if (rect.contains(pt)) {
                ans.x = i;
                break;
            }
        }
        if (ans.x == -1) return null;
        return ans;
    }

    @Override
    public void load(CSVReader reader) {
        setUpTable();
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
