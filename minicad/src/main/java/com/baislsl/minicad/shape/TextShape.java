package com.baislsl.minicad.shape;

import com.baislsl.minicad.Main;
import com.baislsl.minicad.ui.draw.DrawBoard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextShape extends AbstractShape {
    private final static Logger log = LoggerFactory.getLogger(TextShape.class);
    private String text = "text";

    TextShape(DrawBoard canvas, int x1, int y1, int x2, int y2) {
        super(canvas);
        featurePoints.add(new Point(x1, y1));
        featurePoints.add(new Point(x2, y2));
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        log.info(e.toString());
        onOpenTextPanel();
    }

    @Override
    protected void onFeaturePointDrag(MouseEvent e) {
        int x = currentPoint.x, y = currentPoint.y;
        for(Point p : featurePoints){
            if(p.x == x) p.x = e.x;
            if(p.y == y) p.y = e.y;
        }
    }

    private void onOpenTextPanel() {
        Display display = drawBoard.getCanvas().getDisplay();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(2, true));

        newTextEditor(shell);
        newColorSelectPanel(shell);

        shell.pack();
        shell.open();
    }

    private void newTextEditor(Shell shell) {
        Text textEditor = new Text(shell, SWT.NONE);
        textEditor.setText(text);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        textEditor.setLayoutData(gridData);
        textEditor.addModifyListener(e -> {
            text = textEditor.getText();
            redraw();
        });
    }

    @Override
    public void render(GC gc) {
        super.render(gc);
        gc.setForeground(color);
        adjustToText(gc);
        Rectangle r = getBounds();
        gc.drawText(text, r.x, r.y, true);
    }


    private void adjustToText(GC gc) {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        FontData fontData = gc.getFont().getFontData()[0];
        Point dpi = gc.getDevice().getDPI();    // 每英寸x，y偏移量
        fontData.setHeight((y2 - y1)* dpi.y/144);
        gc.setFont(new Font(drawBoard.getCanvas().getDisplay(), fontData));
        Point textSize = gc.textExtent(text);

        featurePoints.get(0).x = Math.min(x1, x2);
        featurePoints.get(0).y = Math.min(y1, y2);
        featurePoints.get(1).x = Math.min(x1, x2) +  textSize.x;
        featurePoints.get(1).y = Math.min(y1, y2) + textSize.y;

    }

    @Override
    public Rectangle getBounds() {
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public boolean intersects(Point p) {
        return getBounds().contains(p);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {
        JSONObject root = super.toJSONObject();
        root.put("text", text);
        return root;
    }

    @Override
    public void loadFormJSONObject(JSONObject object, DrawBoard drawBoard) {
        super.loadFormJSONObject(object, drawBoard);
        this.text = (String) object.get("text");
    }
}
