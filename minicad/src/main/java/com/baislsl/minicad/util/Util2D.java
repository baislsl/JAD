package com.baislsl.minicad.util;


import org.eclipse.swt.graphics.Point;

public class Util2D {
    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}