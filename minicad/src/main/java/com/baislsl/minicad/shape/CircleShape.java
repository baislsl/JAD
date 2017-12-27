package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CircleShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(CovalShape.class);

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
        int r = Math.min(x2 - x1, y2 - y1);
        gc.drawOval(x1, y1, r, r);
    }

    @Override
    public Rectangle getBounds() {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public boolean intersects(Point p) {
        // TODO:
        return getBounds().contains(p);
    }
}
