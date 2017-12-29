package com.baislsl.minicad.shape;

import com.baislsl.minicad.json.JSONDumpable;
import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Mode;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


public interface Shape extends JSONDumpable, MouseListener, MouseMoveListener {

    void render(GC gc);

    Rectangle getBounds();

    void setColor(Color color);

    void setWidth(int width);


    default boolean intersects(Point p) {
        return getBounds().contains(p);
    }

    default void install(DrawBoard c) {
        c.getCanvas().addMouseListener(this);
        c.getCanvas().addMouseMoveListener(this);
    }

    default void uninstall(DrawBoard c){
        c.getCanvas().removeMouseListener(this);
        c.getCanvas().removeMouseMoveListener(this);
        c.setUpMouseListener();
    }
}
