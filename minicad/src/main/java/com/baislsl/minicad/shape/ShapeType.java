package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.ui.draw.DrawPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ShapeType {
    LINE(LineShape.class),
    RECTANGLE(RectangleShape.class),
    TEXT(TextShape.class),
    CIRCLE(CircleShape.class),
    OVAL(OvalShape.class);

    private final static Logger log = LoggerFactory.getLogger(ShapeType.class);
    private Class<?> shapeClazz;

    ShapeType(Class<?> shapeClazz) {
        this.shapeClazz = shapeClazz;
    }

    public Class<?> getShapeClazz() {
        return shapeClazz;
    }

    public Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
        try {
            return (Shape) this.shapeClazz.asSubclass(shapeClazz)
                    .getDeclaredConstructor(DrawBoard.class, int.class, int.class, int.class, int.class)
                    .newInstance(drawBoard, x1, y1, x2, y2);
        } catch (Exception e) {
            log.error("error constructing new shape ", e);
        }
        return null;
    }

}
