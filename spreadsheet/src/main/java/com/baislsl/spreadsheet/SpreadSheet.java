package com.baislsl.spreadsheet;

import com.baislsl.spreadsheet.csv.CsvEditor;
import com.baislsl.spreadsheet.csv.CsvTable;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class SpreadSheet {
    private final static Logger log = LoggerFactory.getLogger(SpreadSheet.class);
    private final static String PROP_PATH = "/default.properties";
    private final Shell shell;
    private Display display;
    private Properties props;
    private String lastPath;
    private CsvTable csvTable;


    public SpreadSheet() {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new GridLayout());
        csvTable = new CsvTable(shell);
        props = new Properties();
        try {
            props.load(SpreadSheet.class.getResourceAsStream(PROP_PATH));
        } catch (IOException e) {
            // should be impossible to happen
            log.error("error open property file. ", e);
        }
        setUpMenu();
    }

    private void setUpMenu() {
        Menu barMenu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(barMenu);

        MenuItem menu = new MenuItem(barMenu, SWT.CASCADE);
        menu.setText(props.getProperty("menu"));
        Menu operation = new Menu(shell, SWT.DROP_DOWN);
        menu.setMenu(operation);

        MenuItem openItem = new MenuItem(operation, SWT.PUSH);
        openItem.setText(props.getProperty("menu.open"));
        openItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onOpen(onOpenDialog());
            }
        });

        MenuItem saveItem = new MenuItem(operation, SWT.PUSH);
        saveItem.setText(props.getProperty("menu.save"));
        saveItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onSave();
            }
        });


        MenuItem exitItem = new MenuItem(operation, SWT.PUSH);
        exitItem.setText(props.getProperty("menu.exit"));
        exitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onExit();
            }
        });
    }

    public String onOpenDialog() {
        return onOpenDialog(Paths.get(".").toAbsolutePath().toString(), SWT.OPEN);
    }

    public String onOpenDialog(@Nullable String startPath, int style) {
        FileDialog fileDialog = new FileDialog(shell, style);
        fileDialog.setFilterPath(startPath);
        fileDialog.setText("Choose cvs file");
        fileDialog.setFilterExtensions(new String[]{
                "*.csv",
                "*.*"
        });
        return fileDialog.open();
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
            lastPath = path;
            csvTable.load(reader);
            csvTable.setFocus();
            shell.setText(lastPath);
            start();
        }
    }

    private void onSave() {
        String path = onOpenDialog(lastPath, SWT.SAVE);
        if(path == null) return;
        try {
            csvTable.save(new CSVWriter(new FileWriter(path)));
            MessageBox messageBox = new MessageBox(shell, SWT.YES);
            messageBox.setMessage("Saved in " + path);
            messageBox.open();
        } catch (IOException e) {
            MessageBox messageBox = new MessageBox(shell, SWT.YES);
            messageBox.setMessage("can not write to " + path);
            messageBox.open();
        }

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
            String path = sheet.onOpenDialog();
            sheet.onOpen(path);
        } else {
            sheet.onOpen(args[0]);
        }
    }


}
