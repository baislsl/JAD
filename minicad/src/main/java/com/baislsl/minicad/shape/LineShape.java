package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LineShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(LineShape.class);
    private int x1, y1, x2, y2;
    private boolean inDrag;

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseDoubleClick(MouseEvent e) {
            log.info("mouseDoubleClick at ({}, {})", e.x, e.y);
        }

        @Override
        public void mouseDown(MouseEvent e) {
            log.info("mouseDown at ({}, {})", e.x, e.y);
            x1 = e.x;
            y1 = e.y;
            inDrag = true;
        }

        @Override
        public void mouseUp(MouseEvent e) {
            log.info("mouseUp at ({}, {})", e.x, e.y);
            inDrag = false;
        }
    };

    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
        @Override
        public void mouseMove(MouseEvent e) {
            log.info("mouseMove at ({}, {})", e.x, e.y);
            if (inDrag) {
                x2 = e.x;
                y2 = e.y;
                redraw();
            }
        }
    };

    LineShape(DrawBoard canvas, int x1, int y1, int x2, int y2){
        super(canvas);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public void render(GC gc) {
        gc.setForeground(color);
        gc.setLineWidth(width);
        gc.drawLine(x1, y1, x2, y2);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x1, y1, x2, y2);
    }

    @Override
    public boolean intersects(Point p) {
        // TODO:
        return getBounds().contains(p);
    }

    @Override
    public MouseMoveListener getMouseMoveListener() {
        return mouseMoveListener;
    }

    @Override
    public MouseListener getMouseListener() {
        return mouseAdapter;
    }
}

