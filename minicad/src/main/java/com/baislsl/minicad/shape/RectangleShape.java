package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Util2D;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RectangleShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(RectangleShape.class);

    RectangleShape(DrawBoard canvas, int x1, int y1, int x2, int y2) {
        super(canvas);
        featurePoints.add(new Point(x1, y1));
        featurePoints.add(new Point(x2, y2));
        featurePoints.add(new Point(x1, y2));   // enable drag of rectangle on all vertex
        featurePoints.add(new Point(x2, y1));
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (currentPoint != null) { // resize rectangle
            int x = currentPoint.x, y = currentPoint.y;
            boolean xAllEqual = true, yAllEqual = true;
            for (Point p : featurePoints) {
                xAllEqual &= (p.x == x);
                yAllEqual &= (p.y == y);
            }
            if (xAllEqual || yAllEqual) { // rectangle on one line
                Point p11 = currentPoint, p12 = null, p21 = null, p22 = null;
                for (Point p : featurePoints) {
                    if (p.x == p11.x && p.y == p11.y) {
                        p12 = p11;
                        break;
                    }
                }
                for (Point p : featurePoints) {
                    if (p != p11 && p != p12) {
                        if (p21 == null) p21 = p;
                        else p22 = p;
                    }
                }
                // p11 和 p12 重合， p21 和 p22 重合
                p11.x = e.x;
                p11.y = e.y;
                if (p21.x == p12.x) p21.x = e.x;
                else p21.y = e.y;


            } else {
                featurePoints.forEach(p -> {
                    if (p.x == x) p.x = e.x;
                    if (p.y == y) p.y = e.y;
                });
            }
        } else if (selected) {
            int dx = e.x - dragBeginPoint.x, dy = e.y - dragBeginPoint.y;
            featurePoints.forEach(p -> {
                p.x += dx;
                p.y += dy;
            });
        }
        redraw();
        dragBeginPoint.x = e.x;
        dragBeginPoint.y = e.y;
    }

    @Override
    public void render(GC gc) {
        super.render(gc);
        gc.setForeground(color);
        gc.setLineWidth(width);
        gc.drawRectangle(getBounds());
    }

    @Override
    public boolean intersects(Point p) {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        return Util2D.rectangleIntersect(x1, y1, x2, y2, p.x, p.y, GAP);
    }
}
