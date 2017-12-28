package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Util2D;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OvalShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(OvalShape.class);

    OvalShape(DrawBoard canvas, int x1, int y1, int x2, int y2) {
        super(canvas);
        featurePoints.add(new Point(x1, (y1 + y2) / 2));
        featurePoints.add(new Point(x2, (y1 + y2) / 2));
        featurePoints.add(new Point((x1 + x2) / 2, y1));
        featurePoints.add(new Point((x1 + x2) / 2, y2));
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (currentPoint != null) {
            Rectangle r = getBounds();
            double rx = r.width / 2.0, ry = r.height / 2.0, ox = r.x + rx, oy = r.y + ry;
            double d1 = Util2D.distance(e.x, e.y, ox, oy),
                    d2 = Util2D.distance(currentPoint.x, currentPoint.y, ox, oy);
            double rate = d1 / d2;
            rx *= rate;
            ry *= rate;

            featurePoints.clear();
            featurePoints.add(new Point((int) (ox), (int) (oy + ry)));
            featurePoints.add(new Point((int) (ox), (int) (oy - ry)));
            featurePoints.add(new Point((int) (ox - rx), (int) (oy)));
            featurePoints.add(new Point((int) (ox + rx), (int) (oy)));
            currentPoint = featurePoints.stream()
                    .min((p1, p2) -> {
                        double d = Util2D.distance(p1, new Point(e.x, e.y)) - Util2D.distance(p2, new Point(e.x, e.y));
                        return d > 0 ? 1 : (d < 0 ? -1 : 0);
                    }).orElse(null);
        } else {
            int dx = e.x - dragBeginPoint.x, dy = e.y - dragBeginPoint.y;
            featurePoints.forEach(p -> {
                p.x += dx;
                p.y += dy;
            });
        }
        redraw();

        // update drag point
        dragBeginPoint = new Point(e.x, e.y);
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
        Rectangle r = getBounds();
        return Util2D.ovalIntersect(r.width / 2.0, r.height / 2.0,
                r.x + r.width / 2.0, r.y + r.height / 2.0, p.x, p.y, GAP);
    }
}
