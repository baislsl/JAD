package com.baislsl.minicad.json;

import com.baislsl.minicad.shape.Shape;
import com.baislsl.minicad.shape.ShapeType;
import com.baislsl.minicad.ui.draw.DrawBoard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JSONShapeManager {
    private DrawBoard canvas;

    public JSONShapeManager(DrawBoard canvas) {
        this.canvas = canvas;
    }

    @SuppressWarnings("unchecked")
    public void dump(List<? extends Shape> shapeList, Writer writer) throws IOException {
        JSONObject root = new JSONObject();
        JSONArray shapes = new JSONArray();
        for (Shape shape : shapeList) {
            shapes.add(shape.toJSONObject());
        }
        root.put("shape", shapes);

        root.writeJSONString(writer);
    }

    public List<Shape> load(Reader reader)
            throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(reader);

        JSONArray shapes = (JSONArray) root.get("shape");
        List<Shape> shapeList = new ArrayList<>();

        for (Object o : shapes) {
            shapeList.add(shapeLoad((JSONObject) o));
        }
        return shapeList;
    }

    private Shape shapeLoad(JSONObject jsonObject) {
        String shapeName = (String) jsonObject.get("name");
        for (ShapeType shapeType : ShapeType.values()) {
            if (shapeType.getShapeClazz().getSimpleName().equals(shapeName)) {
                Shape shape = shapeType.newShapeInstance(canvas, 0, 0, 0, 0);
                shape.loadFormJSONObject(jsonObject, canvas);
                return shape;
            }
        }
        throw new RuntimeException("Error loading json object");
    }

}
