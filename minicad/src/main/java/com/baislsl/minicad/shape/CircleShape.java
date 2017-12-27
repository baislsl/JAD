package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Util2D;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CircleShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(CircleShape.class);

    CircleShape(DrawBoard canvas, int x1, int y1, int x2, int y2) {
        super(canvas);
        featurePoints.add(new Point(x1, y1));
        featurePoints.add(new Point(x2, y2));
    }

    @Override
    public void render(GC gc) {
        super.render(gc);
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        gc.setForeground(color);
        gc.setLineWidth(width);
        int r2 = Math.min(x2 - x1, y2 - y1);
        gc.drawOval(Math.min(x1, x2), Math.min(y1, y2), r2, r2);
    }

    @Override
    public Rectangle getBounds() {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public boolean intersects(Point p) {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        double r = Math.min(x2 - x1, y2 - y1) / 2.0;

        return Util2D.circleIntersect(Math.min(x2 - x1, y2 - y1) / 2,
                Math.min(x1, x2) + r, Math.min(y1, y2) + r, p.x, p.y, GAP);
    }
}
