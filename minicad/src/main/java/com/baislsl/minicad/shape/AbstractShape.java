package com.baislsl.minicad.shape;

import com.baislsl.minicad.util.Mode;
import org.eclipse.swt.SWT;
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
    protected Canvas canvas;

    AbstractShape(Canvas canvas) {
        this(canvas, Display.getCurrent().getSystemColor(DEFAULT_COLOR), DEFAULT_WIDTH);
    }

    AbstractShape(Canvas canvas, Color color, int width) {
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

    protected Color onOpenSetColorPanel() {
        // TODO:
        throw new RuntimeException("todo color panel");
    }
}
