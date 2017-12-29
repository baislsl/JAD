package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Util2D;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class LineShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(LineShape.class);

    LineShape(DrawBoard canvas, int x1, int y1, int x2, int y2) {
        super(canvas);
        featurePoints.add(new Point(x1, y1));
        featurePoints.add(new Point(x2, y2));
    }

    @Override
    protected void onFeaturePointDrag(MouseEvent e) {
        currentPoint.x = e.x;
        currentPoint.y = e.y;
    }

    @Override
    public void render(GC gc) {
        super.render(gc);
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        gc.setForeground(color);
        gc.setLineWidth(width);
        gc.drawLine(x1, y1, x2, y2);
    }

    @Override
    public boolean intersects(Point p) {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        return Util2D.lineIntersect(x1, y1, x2, y2, p.x, p.y, GAP);
    }
}

