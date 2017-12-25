package com.baislsl.spreadsheet;

import com.baislsl.spreadsheet.csv.CsvEditor;
import com.baislsl.spreadsheet.csv.CsvTable;
import com.opencsv.CSVReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class SpreadSheet {
    private final Shell shell;
    private Display display;

    public SpreadSheet() {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new GridLayout());
        setUpMenu();
    }

    private void setUpMenu() {
        // TODO:
    }

    public void onOpenDialog() {
        onOpenDialog(Paths.get(".").toAbsolutePath().toString());
    }
    public void onOpenDialog(@Nullable String path) {
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
        fileDialog.setFilterPath(path);
        fileDialog.setText("Choose cvs file");
        fileDialog.setFilterExtensions(new String[]{
                "*.csv"
        });
        String filePath = fileDialog.open();
        onOpen(filePath);
    }


    private void onOpen(String path) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            MessageBox messageBox = new MessageBox(shell, SWT.YES);
            messageBox.setMessage("File not found for" + path);
            messageBox.open();
        }

        if (reader != null) {
            CsvTable csvTable = new CsvTable(shell);
            csvTable.load(reader);
            shell.pack();
            start();
        }
    }

    private void onSave(String path) {
        // TODO:
    }

    private void start() {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private void onExit() {
        display.dispose();
    }

    public static void main(String[] args) {
        SpreadSheet sheet = new SpreadSheet();
        if (args.length == 0) {
            sheet.onOpenDialog();
        } else {
            sheet.onOpen(args[0]);
        }
    }


}
