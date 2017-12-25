package com.baislsl.spreadsheet.csv;

import com.opencsv.CSVReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CsvTableTest {

    @Test
    public void load() throws Exception {
        Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());


        CsvTable table = new CsvTable(shell);
        table.load(new CSVReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/AnnotationReadingTestPlan.csv"))));

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();

    }
}