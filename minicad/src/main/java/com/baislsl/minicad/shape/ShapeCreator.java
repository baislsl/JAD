package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Mode;
import com.baislsl.minicad.util.ShapeType;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShapeCreator implements Shape {
    private final static Logger log = LoggerFactory.getLogger(ShapeCreator.class);
    private volatile boolean inDrag;
    private int x1, x2, y1, y2;
    private ShapeType shapeType;
    private DrawBoard drawBoard;
    private Shape currentShape;

    private MouseListener mouseListener = new MouseAdapter() {
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

            if(currentShape != null) drawBoard.removeShape(currentShape);
            drawBoard.addShape(currentShape);
            drawBoard.redraw();
        }
    };

    private MouseMoveListener mouseMoveListener = e -> {
        // log.info("mouseMove at ({}, {})", e.x, e.y);
        if(!inDrag) return;
        x2 = e.x;
        y2 = e.y;
        if(currentShape != null) drawBoard.removeShape(currentShape);
        currentShape = generateShape();
        drawBoard.addShape(currentShape);
        drawBoard.redraw();
    };

    public ShapeCreator(ShapeType shapeType, DrawBoard drawBoard) {
        this.shapeType = shapeType;
        this.drawBoard = drawBoard;
    }

    private Shape generateShape() {
        switch (this.shapeType) {
            case LINE:
                return new LineShape(drawBoard, x1, y1, x2, y2);
            default:
                throw new RuntimeException("TODO:");
        }
    }

    @Override
    public void render(GC gc) {

    }

    @Override
    public Rectangle getBounds() {
        throw new RuntimeException("Shape creator should not have get bounds method");
    }


    @Override
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    @Override
    public MouseMoveListener getMouseMoveListener() {
        return mouseMoveListener;
    }

    @Override
    public void setMode(Mode mode) {

    }


    @Override
    public void setColor(Color color) {

    }

    @Override
    public void setWidth(int width) {

    }

    @Override
    public boolean intersects(Point p) {
        throw new RuntimeException("Shape creator should not have intersects method");
    }

}
