package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;

public enum ShapeType {
    LINE(LineShape.class) {
        @Override
        public Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new LineShape(drawBoard, x1, y1, x2, y2);
        }
    },
    RECTANGLE(RectangleShape.class) {
        @Override
        public Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new RectangleShape(drawBoard, x1, y1, x2, y2);
        }
    },
    TEXT(TextShape.class) {
        @Override
        public Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new TextShape(drawBoard, x1, y1, x2, y2);
        }
    },
    CIRCLE(CircleShape.class) {
        @Override
        public Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new CircleShape(drawBoard, x1, y1, x2, y2);
        }
    },
    OVAL(OvalShape.class) {
        @Override
        public Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new OvalShape(drawBoard, x1, y1, x2, y2);
        }
    };

    private Class<?> clazz;

    ShapeType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public abstract Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2);

}
