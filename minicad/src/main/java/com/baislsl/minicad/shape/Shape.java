package com.baislsl.minicad.shape;

import com.baislsl.minicad.util.Mode;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;


public interface Shape {

    void render(GC gc);

    Rectangle getBounds();

    MouseListener getMouseListener();

    MouseMoveListener getMouseMoveListener();

    void setMode(Mode mode);

    void setColor(Color color);

    void setWidth(int width);


    default boolean intersects(Point p) {
        return getBounds().contains(p);
    }

    default void install(Canvas c) {
        c.addMouseListener(getMouseListener());
        c.addMouseMoveListener(getMouseMoveListener());
    }

    void uninstall(Canvas c);
}
