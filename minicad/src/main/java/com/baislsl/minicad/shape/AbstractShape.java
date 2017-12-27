package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Mode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractShape implements Shape {
    private final static Logger log = LoggerFactory.getLogger(AbstractShape.class);
    private final static int DEFAULT_COLOR = SWT.COLOR_BLUE;
    private final static int DEFAULT_WIDTH = 2;
    protected final static int MIN_WIDTH = 1;
    protected final static int MAX_WIDTH = 10;
    protected final static int WIDTH_INCREMENT = 1;

    protected Color color;
    protected int width;
    protected Mode mode;
    protected DrawBoard canvas;

    AbstractShape(DrawBoard canvas) {
        this(canvas, Display.getCurrent().getSystemColor(DEFAULT_COLOR), DEFAULT_WIDTH);
    }

    AbstractShape(DrawBoard canvas, Color color, int width) {
        this.canvas = canvas;
        this.color = color;
        this.width = width;
    }

    @Override
    public void setMode(Mode mode) {
        log.info("setMode {}", mode.name());
        this.mode = mode;
    }

    public void redraw() {
        canvas.redraw();
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    protected void onOpenSettingPanel() {
        Display display = Display.getCurrent();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(2, true));

        newWidthSelectPanel(shell);
        newColorSelectPanel(shell);

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void newColorSelectPanel(Shell shell) {
        // Use a label full of spaces to show the color
        final Label colorLabel = new Label(shell, SWT.NONE);
        colorLabel.setText("                       ");
        colorLabel.setBackground(color);

        Button button = new Button(shell, SWT.PUSH);
        button.setText("Color...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                // Create the color-change dialog
                ColorDialog dlg = new ColorDialog(shell);
                dlg.setRGB(color.getRGB());
                dlg.setText("Choose a Color");

                // Open the dialog and retrieve the selected color
                RGB rgb = dlg.open();
                if (rgb != null) {
                    // Dispose the old color, create the
                    // new one, and set into the label
                    color.dispose();
                    color = new Color(shell.getDisplay(), rgb);
                    colorLabel.setBackground(color);
                    setColor(color);
                    redraw();
                }
            }
        });
    }

    private void newWidthSelectPanel(Shell shell) {
        final Label colorLabel = new Label(shell, SWT.NONE);
        colorLabel.setText("width");

        Spinner spinner = new Spinner(shell, SWT.BORDER | SWT.FILL);
        spinner.setMinimum(MIN_WIDTH);
        spinner.setMaximum(MAX_WIDTH);
        spinner.setSelection(width);
        spinner.setIncrement(WIDTH_INCREMENT);
        spinner.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setWidth(spinner.getSelection());
                redraw();
            }
        });
        spinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

}
