package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Util2D;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CircleShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(CircleShape.class);

    CircleShape(DrawBoard canvas, int x1, int y1, int x2, int y2) {
        super(canvas);
        int x = Math.min(x1, x2), y = Math.min(y1, y2);
        int r = Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2));
        featurePoints.add(new Point(x + r / 2, y));
        featurePoints.add(new Point(x + r / 2, y + r));
        featurePoints.add(new Point(x, y + r / 2));
        featurePoints.add(new Point(x + r, y + r / 2));
    }

    @Override
    protected void onFeaturePointDrag(MouseEvent e) {
        Rectangle rect = getBounds();
        double r = rect.width / 2.0, ox = rect.x + r, oy = rect.y + r;
        r = Util2D.distance(e.x, e.y, ox, oy);
        featurePoints.clear();
        featurePoints.add(new Point((int) (ox), (int) (oy + r)));
        featurePoints.add(new Point((int) (ox), (int) (oy - r)));
        featurePoints.add(new Point((int) (ox - r), (int) (oy)));
        featurePoints.add(new Point((int) (ox + r), (int) (oy)));
        currentPoint = featurePoints.stream()
                .min((p1, p2) -> {
                    double d = Util2D.distance(p1, new Point(e.x, e.y)) - Util2D.distance(p2, new Point(e.x, e.y));
                    return d > 0 ? 1 : (d < 0 ? -1 : 0);
                }).orElse(null);
    }

    @Override
    public void render(GC gc) {
        super.render(gc);
        gc.setForeground(color);
        gc.setLineWidth(width);
        Rectangle r = getBounds();
        gc.drawOval(r.x, r.y, r.width, r.height);
    }

    @Override
    public boolean intersects(Point p) {
        Rectangle rect = getBounds();
        double r = rect.width / 2.0, ox = rect.x + r, oy = rect.y + r;
        return Util2D.circleIntersect(r, ox, oy, p.x, p.y, GAP);
    }
}
