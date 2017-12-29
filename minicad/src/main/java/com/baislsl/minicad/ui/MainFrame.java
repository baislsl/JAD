package com.baislsl.minicad.ui;

import com.baislsl.minicad.json.JSONShapeManager;
import com.baislsl.minicad.shape.Shape;
import com.baislsl.minicad.ui.control.ControlPanel;
import com.baislsl.minicad.ui.draw.DrawPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class MainFrame {
    private final static Logger log = LoggerFactory.getLogger(MainFrame.class);
    private final static String PROP_PATH = "/default.properties";

    private Display display = new Display();
    private final Shell shell = new Shell(display);
    private Properties props;
    private DrawPanel drawPanel;
    private ControlPanel controlPanel;
    private JSONShapeManager jsonShapeManager;


    public MainFrame() {
        drawPanel = new DrawPanel(shell, SWT.NONE);
        controlPanel = new ControlPanel(shell, SWT.BORDER, drawPanel);
        jsonShapeManager = new JSONShapeManager(drawPanel);
        drawPanel.setBounds(10, 10, 510, 510);
        controlPanel.setBounds(520, 10, 100, 510);
        shell.setSize(640, 560);
        props = new Properties();
        try {
            props.load(MainFrame.class.getResourceAsStream(PROP_PATH));
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
        openItem.setText(props.getProperty("menu.load"));
        openItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                load(onOpenDialog());
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

    public void start() {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public String onOpenDialog() {
        return onOpenDialog(Paths.get(".").toAbsolutePath().toString(), SWT.OPEN);
    }

    public String onOpenDialog(@Nullable String startPath, int style) {
        FileDialog fileDialog = new FileDialog(shell, style);
        fileDialog.setFilterPath(startPath);
        fileDialog.setText("Choose json file");
        fileDialog.setFilterExtensions(new String[]{
                "*.json",
                "*.*"
        });
        return fileDialog.open();
    }


    private void load(String path) {
        Reader reader = null;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            MessageBox messageBox = new MessageBox(shell, SWT.YES);
            messageBox.setMessage("File not found for " + path);
            messageBox.open();
        }

        if (reader != null) {
            try {
                java.util.List<Shape> shapeList = jsonShapeManager.load(reader);
                drawPanel.setShapeList(shapeList);
            } catch (IOException e) {
                MessageBox messageBox = new MessageBox(shell, SWT.YES);
                messageBox.setMessage("File open error");
                messageBox.open();
            } catch (ParseException e) {
                MessageBox messageBox = new MessageBox(shell, SWT.YES);
                messageBox.setMessage("Parse error");
                messageBox.open();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("reader close error ", e);
                }
            }
        }
        drawPanel.redraw();
    }

    private void onSave() {
        String path = onOpenDialog(Paths.get(".").toAbsolutePath().toString(), SWT.SAVE);
        if (path == null) return;
        Writer writer = null;
        try {
            writer = new FileWriter(path);
            jsonShapeManager = new JSONShapeManager(drawPanel);
            jsonShapeManager.dump(drawPanel.getShapeList(), writer);
            MessageBox messageBox = new MessageBox(shell, SWT.YES);
            messageBox.setMessage("Saved in " + path);
            messageBox.open();
        } catch (IOException e) {
            MessageBox messageBox = new MessageBox(shell, SWT.YES);
            messageBox.setMessage("can not write to " + path);
            messageBox.open();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("writer close error ", e);
                }
            }
        }

    }

    private void onExit() {
        display.dispose();
    }

}
