package com.baislsl.minicad.shape;

import com.baislsl.minicad.ui.draw.DrawBoard;
import com.baislsl.minicad.util.Util2D;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractShape implements Shape {
    private final static Logger log = LoggerFactory.getLogger(AbstractShape.class);
    private final static int DEFAULT_COLOR = SWT.COLOR_BLUE;
    private final static int FEATURE_POINT_SELECTED_COLOR = SWT.COLOR_RED;
    private final static int DEFAULT_WIDTH = 2;
    private final static int SELECT_WIDTH_INCREMENT = 1;

    protected final static int MIN_WIDTH = 1;
    protected final static int MAX_WIDTH = 10;
    protected final static int WIDTH_INCREMENT = 1;
    protected final static double GAP = 4;
    protected boolean selected;
    protected Point dragBeginPoint;
    protected Point currentPoint;
    protected List<Point> featurePoints = new ArrayList<>();


    protected Color color;
    protected int width;
    protected DrawBoard drawBoard;

    AbstractShape(DrawBoard drawBoard) {
        this(drawBoard, drawBoard.getCanvas().getDisplay().getSystemColor(DEFAULT_COLOR), DEFAULT_WIDTH);
    }

    AbstractShape(DrawBoard drawBoard, Color color, int width) {
        this.drawBoard = drawBoard;
        setColor(color);
        this.width = width;
    }

    public void redraw() {
        drawBoard.redraw();
    }

    @Override
    public void setColor(Color color) {
        this.color = new Color(drawBoard.getCanvas().getDisplay(), color.getRGB());
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        log.info(e.toString());
        onOpenSettingPanel();
    }

    @Override
    public void mouseDown(MouseEvent e) {
        log.info("mouse down: {}", e.toString());
        selected = true;
        Point cur = new Point(e.x, e.y);
        dragBeginPoint = cur;
        this.width += SELECT_WIDTH_INCREMENT;
        currentPoint = featurePoints.stream()
                .min((p1, p2) -> {
                    double distance = Util2D.distance(p1, cur) - Util2D.distance(p2, cur);
                    return distance > 0 ? 1 : (distance < 0 ? -1 : 0);
                }).filter(p -> Util2D.distance(p, cur) < GAP)
                .orElse(null);
        log.info("current point is {}", currentPoint);
        redraw();
    }


    @Override
    public void mouseUp(MouseEvent e) {
        log.info("mouseUp: {}", e.toString());
        selected = false;
        dragBeginPoint = null;
        currentPoint = null;
        this.width -= SELECT_WIDTH_INCREMENT;
        uninstall(drawBoard);
        redraw();
    }

    @Override
    public void mouseMove(MouseEvent e) {
        // log.info("mouseMove at ({}, {})", e.x, e.y);
        if (currentPoint != null) { // resize advance drag
            currentPoint.x = e.x;
            currentPoint.y = e.y;
        } else if (selected) {    // drag
            int dx = e.x - dragBeginPoint.x, dy = e.y - dragBeginPoint.y;
            featurePoints.forEach(p -> {
                p.x += dx;
                p.y += dy;
            });
        }
        redraw();
        dragBeginPoint.x = e.x;
        dragBeginPoint.y = e.y;

    }

    @Override
    public void render(GC gc) {
        gc.setLineWidth(1);
        if (selected) {
            Rectangle rectangle = getBounds();
            if (rectangle != null) {
                gc.drawRectangle(rectangle);
            }

            Color fg = drawBoard.getCanvas().getDisplay().getSystemColor(FEATURE_POINT_SELECTED_COLOR);
            Color bg = gc.getBackground();
            featurePoints.stream()
                    .filter(p -> p != currentPoint)
                    .forEach(p -> {
                        gc.setForeground(bg);
                        gc.fillOval(p.x - 3, p.y - 3, 6, 6);
                        gc.setForeground(fg);
                        gc.drawOval(p.x - 3, p.y - 3, 6, 6);
                    });
        }

        if (currentPoint != null) {
            gc.drawOval(currentPoint.x - 3, currentPoint.y - 3, 6, 6);
        }
    }

    protected void onOpenSettingPanel() {
        Display display = drawBoard.getCanvas().getDisplay();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(2, true));

        newWidthSelectPanel(shell);
        newColorSelectPanel(shell);

        shell.pack();
        shell.open();
    }

    protected void newColorSelectPanel(Shell shell) {
        // Use a label full of spaces to show the color
        final Label colorLabel = new Label(shell, SWT.NONE);
        colorLabel.setText("                       ");
        colorLabel.setBackground(color);

        Button button = new Button(shell, SWT.PUSH);
        button.setText("Color...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                // Create the color-change dialog
                ColorDialog dlg = new ColorDialog(shell);
                dlg.setRGB(color.getRGB());
                dlg.setText("Choose a Color");

                // Open the dialog and retrieve the selected color
                RGB rgb = dlg.open();
                if (rgb != null) {
                    // Dispose the old color, create the
                    // new one, and set into the label
                    color.dispose();
                    color = new Color(shell.getDisplay(), rgb);
                    colorLabel.setBackground(color);
                    setColor(color);
                    redraw();
                }
            }
        });
    }

    protected void newWidthSelectPanel(Shell shell) {
        final Label colorLabel = new Label(shell, SWT.NONE);
        colorLabel.setText("width");

        Spinner spinner = new Spinner(shell, SWT.BORDER | SWT.FILL);
        spinner.setMinimum(MIN_WIDTH);
        spinner.setMaximum(MAX_WIDTH);
        spinner.setSelection(width);
        spinner.setIncrement(WIDTH_INCREMENT);
        spinner.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setWidth(spinner.getSelection());
                redraw();
            }
        });
        spinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    @Override
    public Rectangle getBounds() {
        Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        featurePoints.forEach(p -> {
            min.x = Math.min(min.x, p.x);
            min.y = Math.min(min.y, p.y);
            max.x = Math.max(max.x, p.x);
            max.y = Math.max(max.y, p.y);
        });
        return new Rectangle(min.x, min.y, max.x - min.x, max.y - min.y);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {
        JSONObject root = new JSONObject();
        root.put("name", this.getClass().getSimpleName());

        JSONObject colorObject = new JSONObject();
        colorObject.put("red", color.getRed());
        colorObject.put("green", color.getGreen());
        colorObject.put("blue", color.getBlue());
        root.put("color", colorObject);

        root.put("width", width);

        JSONArray jsonArray = new JSONArray();
        for (Point p : featurePoints) {
            JSONObject op = new JSONObject();
            op.put("x", p.x);
            op.put("y", p.y);
            jsonArray.add(op);
        }
        root.put("points", jsonArray);

        return root;
    }

    @Override
    public void loadFormJSONObject(JSONObject object, DrawBoard drawBoard) {
        String name = (String) object.get("name");
        assert name.equals(this.getClass().getSimpleName());

        this.drawBoard = drawBoard;

        this.width = Math.toIntExact((long) object.get("width"));

        JSONObject colorObject = (JSONObject) object.get("color");
        int r = Math.toIntExact((long) colorObject.get("red")),
                g = Math.toIntExact((long) colorObject.get("green")),
                b = Math.toIntExact((long) colorObject.get("blue"));
        this.color = new Color(drawBoard.getCanvas().getDisplay(), r, g, b);

        this.featurePoints.clear();
        JSONArray array = (JSONArray) object.get("points");
        for (Object o : array) {
            JSONObject op = (JSONObject) o;
            Point p = new Point(Math.toIntExact((long) op.get("x")), Math.toIntExact((long) op.get("y")));
            featurePoints.add(p);
        }
    }
}
