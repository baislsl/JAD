package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
    private String text = "input text";

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

    private void onOpenTextPanel() {
        Display display = Display.getCurrent();
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
        int x1 = featurePoints.get(0).x, y1 = featurePoints.get(0).y,
                x2 = featurePoints.get(1).x, y2 = featurePoints.get(1).y;
        gc.setForeground(color);

        Point textSize = gc.textExtent(text);
        FontData fontData = gc.getFont().getFontData()[0];
        // double rate = Math.min(1.0 * Math.abs(x2 - x1) / textSize.x, 1.0 * Math.abs(y2 - y1) / textSize.y);
        double rate = Math.abs(y2 - y1) / textSize.y;
        fontData.setHeight((int) (rate * textSize.y));
        fontData.setHeight(Math.abs(y2 - y1));
        gc.setFont(new Font(Display.getCurrent(), fontData));
        gc.drawText(text, Math.min(x1, x2), Math.min(y1, y2) - 10, true);
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
