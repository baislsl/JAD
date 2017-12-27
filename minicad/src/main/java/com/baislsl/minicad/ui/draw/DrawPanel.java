package com.baislsl.minicad.ui.draw;

import com.baislsl.minicad.shape.LineShape;
import com.baislsl.minicad.shape.Shape;
import com.baislsl.minicad.shape.ShapeCreator;
import com.baislsl.minicad.util.Mode;
import com.baislsl.minicad.util.ShapeType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends Composite implements MessageReceiver, DrawBoard {
    private final static Logger log = LoggerFactory.getLogger(DrawPanel.class);
    private final static int CANVAS_SIZE = 500;
    private Canvas canvas;
    private List<Shape> shapeList = new ArrayList<>();
    private ShapeType currentShapeType = ShapeType.LINE;
    private Mode currentMode = Mode.CREATE;
    private Shape currentShape;

    private MouseAdapter defaultMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseDown(MouseEvent e) {
            log.info("mouseDown at ({}, {})", e.x, e.y);
            if (currentMode == Mode.CREATE) {
                ShapeCreator creator = new ShapeCreator(currentShapeType, DrawPanel.this);
                creator.install(DrawPanel.this);
            } else {    // DELETE
                Shape shape = fetchShape(e.x, e.y);
                if (shape == null) return;
                shapeList.remove(shape);
            }
        }
    };

    public DrawPanel(Composite parent, int style) {
        super(parent, style);
        canvas = new MyCanvas(this, SWT.FILL | SWT.BORDER);
        canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                for (Shape shape : shapeList) {
                    shape.render(e.gc);
                }
            }
        });
        canvas.addMouseListener(defaultMouseAdapter);
        canvas.setBounds(0, 0, CANVAS_SIZE, CANVAS_SIZE);
    }

    @Override
    public void setMode(Mode mode) {
        log.info("set mode {}", mode.name());
        currentShape = null;
        this.currentMode = mode;
        canvas.addMouseListener(defaultMouseAdapter);
    }

    @Override
    public void setShapeType(ShapeType type) {
        log.info("set shape type {}", type);
        currentShape = null;
        this.currentShapeType = type;
        canvas.addMouseListener(defaultMouseAdapter);
    }

    private Shape fetchShape(int x, int y) {
        // TODO search for shape
        throw new RuntimeException("TODO: fetch shape");
    }

    @Override
    public void redraw() {
        canvas.redraw();
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void addShape(Shape shape) {
        shapeList.add(shape);
    }


    @Override
    public void removeShape(Shape shape) {
        shapeList.remove(shape);
    }

    @Override
    public void clear() {
        shapeList.clear();
    }

    @Override
    public void setUpMouseListener() {
        canvas.addMouseListener(defaultMouseAdapter);
    }
}
