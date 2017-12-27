package com.baislsl.minicad.util;


import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;

public class Util2D {
    public static double distance(Point p1, Point p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static boolean lineIntersect(double x1, double y1, double x2, double y2,
                                        double px, double py, double gap) {
        return px > Math.min(x1, x2) - gap
                && px < Math.max(x1, x2) + gap
                && py > Math.min(y1, y2) - gap
                && py < Math.max(y1, y2) + gap
                && pointToLine(px, py, x1, y1, x2, y2) < gap;
    }

    public static boolean rectangleIntersect(double x1, double y1, double x2, double y2,
                                             double px, double py, double gap) {
        return (Math.abs(px - x1) < gap || Math.abs(px - x2) < gap)
                && (Math.abs(py - y1) < gap || Math.abs(py - y2) < gap);
    }

    public static boolean ovalIntersect(double rx, double ry, double ox, double oy,
                                        double px, double py, double gap) {
        // 以(ox, oy)为中心伸缩变换为圆
        double rate = ry / rx;
        ox = (ox - rx) * rate + rx;
        px = (px - rx) * rate + rx;
        gap = gap * rate;

        return circleIntersect(rx, ox, oy, px, py, gap);
    }

    public static boolean circleIntersect(double r, double ox, double oy,
                                          double px, double py, double gap) {
        double dis = distance(ox, oy, px, py);
        return Math.abs(dis - r) < gap;
    }

    private static double pointToLine(double px, double py, double x1, double y1,
                                      double x2, double y2) {
        if (x1 == x2) { // ensure x1 != x2
            return pointToLine(py, px, y1, x1, y2, x2);
        }

        if (y1 == y2) {
            return Integer.MAX_VALUE;
        }

        // Ax + By + C = 0
        double B = 1.0, A = -(y1 - y2) / (x1 - x2), C = -A * x1 - B * y1;

        return Math.abs(A * px + B * py + C) / Math.sqrt(A * A + B * B);
    }


}
