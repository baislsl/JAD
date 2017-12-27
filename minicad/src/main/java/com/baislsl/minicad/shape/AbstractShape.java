package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Mode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractShape implements Shape {
    private final static Logger log = LoggerFactory.getLogger(AbstractShape.class);
    private final static int DEFAULT_COLOR = SWT.COLOR_BLUE;
    private final static int DEFAULT_WIDTH = 2;

    protected Color color;
    protected int width;
    protected Mode mode;
    protected DrawBoard canvas;
    private MouseListener preMouseListener;

    AbstractShape(DrawBoard canvas) {
        this(canvas, Display.getCurrent().getSystemColor(DEFAULT_COLOR), DEFAULT_WIDTH, null);
    }

    AbstractShape(DrawBoard canvas, MouseListener preMouseListener) {
        this(canvas, Display.getCurrent().getSystemColor(DEFAULT_COLOR), DEFAULT_WIDTH, preMouseListener);
    }

    AbstractShape(DrawBoard canvas, Color color, int width, MouseListener preMouseListener) {
        this.canvas = canvas;
        this.color = color;
        this.width = width;
        this.preMouseListener = preMouseListener;
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

    @Override
    public void uninstall(Canvas c) {
        c.removeMouseListener(getMouseListener());
        c.removeMouseMoveListener(getMouseMoveListener());
        c.addMouseListener(preMouseListener);
    }

    protected Color onOpenSetColorPanel() {
        // TODO:
        throw new RuntimeException("todo color panel");
    }
}
