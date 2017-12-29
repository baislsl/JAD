package com.baislsl.minicad.util;


import org.eclipse.swt.graphics.Point;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

public class Util2D {
    public static double distance(Point p1, Point p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
    }

    public static boolean lineIntersect(double x1, double y1, double x2, double y2,
                                        double px, double py, double gap) {
        return px > min(x1, x2) - gap
                && px < max(x1, x2) + gap
                && py > min(y1, y2) - gap
                && py < max(y1, y2) + gap
                && pointToLine(px, py, x1, y1, x2, y2) < gap;
    }

    public static boolean rectangleIntersect(double x1, double y1, double x2, double y2,
                                             double px, double py, double gap) {
        return ((abs(px - x1) < gap || abs(px - x2) < gap) && (min(y1, y2) < py && max(y1, y2) > py))
                || ((abs(py - y1) < gap || abs(py - y2) < gap) && (min(x1, x2) < px && max(x1, x2) > py));
    }

    public static boolean ovalIntersect(double rx, double ry, double ox, double oy,
                                        double px, double py, double gap) {
        // 以y = oy为轴伸缩变换为圆
        double rate = rx / ry;
        py = (py - oy) * rate + oy;
        return circleIntersect(rx, ox, oy, px, py, gap);
    }

    public static boolean circleIntersect(double r, double ox, double oy,
                                          double px, double py, double gap) {
        double dis = distance(ox, oy, px, py);
        return abs(dis - r) < gap;
    }

    private static double pointToLine(double px, double py, double x1, double y1,
                                      double x2, double y2) {
        if (x1 == x2) { // ensure x1 != x2
            return y1 == y2 ? Integer.MAX_VALUE : pointToLine(py, px, y1, x1, y2, x2);
        }

        // Ax + By + C = 0
        double B = 1.0, A = -(y1 - y2) / (x1 - x2), C = -A * x1 - B * y1;

        return abs(A * px + B * py + C) / sqrt(A * A + B * B);
    }


}
