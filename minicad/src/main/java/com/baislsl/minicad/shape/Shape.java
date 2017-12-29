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

    /**
     * 执行图形绘制
     * @param gc GC
     */
    void render(GC gc);

    /**
     * 得到一个包围住该图形的长方形
     * @return Rectangle
     */
    Rectangle getBounds();

    /**
     * 定义图形颜色
     * @param color 颜色
     */
    void setColor(Color color);

    /**
     * 定义线条宽度
     * @param width 宽度
     */
    void setWidth(int width);


    /**
     * 判断某个点是否在图形上
     * @param p 点
     * @return true if intersect, else false
     */
    default boolean intersects(Point p) {
        return getBounds().contains(p);
    }

    /**
     * 将监听器安装到DrawBoard上
     * @param c DrawBoard
     */
    default void install(DrawBoard c) {
        c.getCanvas().addMouseListener(this);
        c.getCanvas().addMouseMoveListener(this);
    }

    /**
     * 卸载监听器
     * @param c DrawBoard
     */
    default void uninstall(DrawBoard c){
        c.getCanvas().removeMouseListener(this);
        c.getCanvas().removeMouseMoveListener(this);
        c.setUpMouseListener();
    }
}
