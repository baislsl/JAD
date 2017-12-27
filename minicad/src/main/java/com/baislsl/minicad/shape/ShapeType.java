package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;

import java.util.function.Supplier;

public enum ShapeType {
    LINE {
        @Override
        Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new LineShape(drawBoard, x1, y1, x2, y2);
        }
    },
    RECTANGLE {
        @Override
        Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new RectangleShape(drawBoard, x1, y1, x2, y2);
        }
    },
    TEXT {
        @Override
        Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new TextShape(drawBoard, x1, y1, x2, y2);
        }
    },
    CIRCLE {
        @Override
        Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new CircleShape(drawBoard, x1, y1, x2, y2);
        }
    },
    OVAL {
        @Override
        Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2) {
            return new OvalShape(drawBoard, x1, y1, x2, y2);
        }
    };

    abstract Shape newShapeInstance(DrawBoard drawBoard, int x1, int y1, int x2, int y2);


}
