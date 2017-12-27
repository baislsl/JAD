package com.baislsl.minicad.ui.draw;

import com.baislsl.minicad.shape.Shape;
import org.eclipse.swt.widgets.Canvas;

public interface DrawBoard {

    void redraw();

    void setUpMouseListener();

    Canvas getCanvas();

    void addShape(Shape shape);

    void removeShape(Shape shape);

    void clear();

}
