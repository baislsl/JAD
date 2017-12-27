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
    private List<Point> featurePoints = new ArrayList<>();
    private Point currentPoint;

    LineShape(DrawBoard canvas, int x1, int y1, int x2, int y2) {
        super(canvas);
        featurePoints.add(new Point(x1, y1));
        featurePoints.add(new Point(x2, y2));
    }

    @Override
    public void mouseDown(MouseEvent e) {
        Point cur = new Point(e.x, e.y);
        currentPoint = featurePoints.stream()
                .min((p1, p2) -> {
                    double distance = Util2D.distance(p1, cur) - Util2D.distance(p2, cur);
                    return distance > 0 ? 1 : (distance < 0 ? -1 : 0);
                }).filter(p -> Util2D.distance(p, cur) < GAP)
                .orElse(null);
        super.mouseDown(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        currentPoint = null;
        super.mouseUp(e);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (currentPoint != null) { // resize advance drag
            currentPoint.x = e.x;
            currentPoint.y = e.y;
        } else if (selected) {    // drag
            int dx = e.x - dragBeginPoint.x, dy = e.y - dragBeginPoint.y;
            for (Point p : featurePoints) {
                p.x += dx;
                p.y += dy;
            }
        }
        super.mouseMove(e);
    }

    @Override
    public void render(GC gc) {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        gc.setForeground(color);
        gc.setLineWidth(width);
        gc.drawLine(x1, y1, x2, y2);

        if (selected) {
            gc.setLineWidth(1);
            gc.drawRectangle(x1, y1, x2 - x1, y2 - y1);

            for (Point p : featurePoints) {
                if (p != currentPoint) {
                    gc.setLineWidth(2);
                    gc.drawOval(p.x - 1, p.y - 1, 2, 2);
                }
            }
        }

        if (currentPoint != null) {
            gc.drawOval(currentPoint.x - 2, currentPoint.y - 2, 4, 4);
        }
    }

    @Override
    public Rectangle getBounds() {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        return new Rectangle(x1, y1, x2, y2);
    }

    @Override
    public boolean intersects(Point p) {
        // TODO:
        return getBounds().contains(p);
    }
}

