package com.baislsl.minicad.ui.draw;

import com.baislsl.minicad.util.Mode;
import com.baislsl.minicad.util.ShapeType;

public interface MessageReceiver {

    void setMode(Mode mode);

    void setShapeType(ShapeType type);
}
