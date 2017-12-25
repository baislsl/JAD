package com.baislsl.spreadsheet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.swing.text.TableView;

public class Main {
    public static void main(String[] args)
    {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());

        final ScrolledComposite composite = new ScrolledComposite(shell, SWT.V_SCROLL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Table table = new Table(composite, SWT.NO_SCROLL | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);

        composite.setContent(table);
        composite.setExpandHorizontal(true);
        composite.setExpandVertical(true);
        composite.setAlwaysShowScrollBars(true);
        composite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        Button fillTable = new Button(shell, SWT.PUSH);
        fillTable.setText("Fill table");
        fillTable.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));

        fillTable.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event arg0)
            {
                if (table.getColumnCount() < 1)
                {
                    for (int col = 0; col < 4; col++)
                    {
                        TableColumn column = new TableColumn(table, SWT.NONE);
                        column.setText("Column " + col);
                    }
                }

                for (int row = 0; row < 20; row++)
                {
                    TableItem item = new TableItem(table, SWT.NONE);

                    for (int col = 0; col < table.getColumnCount(); col++)
                    {
                        item.setText(col, "Item " + row + " " + col);
                    }
                }

                for (int col = 0; col < table.getColumnCount(); col++)
                {
                    table.getColumn(col).pack();
                }

                composite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            }
        });

        Button clearTable = new Button(shell, SWT.PUSH);
        clearTable.setText("Clear table");
        clearTable.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));

        clearTable.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event arg0)
            {
                table.removeAll();

                composite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            }
        });

        shell.pack();
        shell.setSize(400, 300);
        shell.open();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}