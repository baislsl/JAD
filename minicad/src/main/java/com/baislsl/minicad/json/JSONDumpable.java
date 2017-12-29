package com.baislsl.minicad.json;

import com.baislsl.minicad.ui.draw.DrawBoard;
import org.json.simple.JSONObject;

public interface JSONDumpable {

    JSONObject toJSONObject();

    void loadFormJSONObject(JSONObject object, DrawBoard drawBoard);
}
