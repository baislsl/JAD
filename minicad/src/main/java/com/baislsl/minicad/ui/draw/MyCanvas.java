package com.baislsl.minicad.ui.draw;

import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * swt 中 canvas 没有 removeAllListeners 之类的方法。。。
 * <p>
 * ensure only one mouse listener ...
 */
public class MyCanvas extends Canvas {
    private final static Logger log = LoggerFactory.getLogger(Canvas.class);
    private MouseListener lastMouseListener;
    private MouseMoveListener lastMouseMoveListener;

    public MyCanvas(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    public void removeMouseListener(MouseListener listener) {
        super.removeMouseListener(listener);
        if(lastMouseListener == listener) lastMouseListener = null;
    }

    @Override
    public void removeMouseMoveListener(MouseMoveListener listener) {
        super.removeMouseMoveListener(listener);
        if(lastMouseMoveListener == listener) lastMouseMoveListener = null;
    }

    @Override
    public void addMouseListener(MouseListener listener) {
        if(lastMouseListener == listener) return;
        log.debug("install mouse listener");
        super.addMouseListener(listener);

        if (lastMouseListener != null)
            removeMouseListener(lastMouseListener);
        lastMouseListener = listener;
    }

    @Override
    public void addMouseMoveListener(MouseMoveListener listener) {
        if(lastMouseMoveListener == listener) return;
        log.debug("install mouse move listener");

        super.addMouseMoveListener(listener);

        if (lastMouseMoveListener != null)
            removeMouseMoveListener(lastMouseMoveListener);
        lastMouseMoveListener = listener;
    }
}
