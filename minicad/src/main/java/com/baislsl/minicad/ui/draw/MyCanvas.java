package com.baislsl.minicad.ui.draw;

import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * swt 中 canvas 没有 removeAllListeners 之类的方法。。。
 *
 * ensure only one mouse listener ...
 */
public class MyCanvas extends Canvas {
    private MouseListener lastMouseListener;
    private DragDetectListener lastDragDetectListener;
    private MouseMoveListener lastMouseMoveListener;

    public MyCanvas(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    public void addMouseListener(MouseListener listener) {
        super.addMouseListener(listener);

        if (lastMouseListener != null) removeMouseListener(lastMouseListener);
        lastMouseListener = listener;
    }

    @Override
    public void addMouseMoveListener(MouseMoveListener listener) {
        super.addMouseMoveListener(listener);

        if (lastMouseMoveListener != null) removeMouseMoveListener(lastMouseMoveListener);
        lastMouseMoveListener = listener;
    }

    @Override
    public void addDragDetectListener(DragDetectListener listener) {
        super.addDragDetectListener(listener);

        if (lastDragDetectListener != null) removeDragDetectListener(lastDragDetectListener);
        lastDragDetectListener = listener;
    }
}
