package com.baislsl.minicad.ui;

import com.baislsl.minicad.ui.control.ControlPanel;
import com.baislsl.minicad.ui.draw.DrawPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainFrame {
    private Display display = new Display();
    private final Shell shell = new Shell(display);

    public MainFrame() {
        DrawPanel drawPanel = new DrawPanel(shell, SWT.NONE);
        ControlPanel controlPanel = new ControlPanel(shell, SWT.BORDER, drawPanel);
        drawPanel.setBounds(10, 10, 510, 510);
        controlPanel.setBounds(520, 10, 100, 510);
        shell.setSize(640, 560);
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
}
