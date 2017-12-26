package com.baislsl.minicad.ui.control;

import com.baislsl.minicad.ui.draw.MessageReceiver;
import com.baislsl.minicad.util.Mode;
import com.baislsl.minicad.util.ShapeType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ControlPanel extends Composite {
    private MessageReceiver receiver;

    public ControlPanel(Composite parent, int style, MessageReceiver receiver) {
        super(parent, style);
        this.receiver = receiver;
        init();
    }

    private void init() {
        setLayout(new RowLayout());
        for (Mode mode : Mode.values()) {
            addModeButton(mode.name(), mode);
        }

        // TODO: add a bar to split mode buttons and shape buttons

        for (ShapeType type : ShapeType.values()) {
            addShapeTypeButton(type.name(), type);
        }
    }

    private void addModeButton(String text, Mode mode) {
        Button button = new Button(this, SWT.NONE);
        button.setText(text);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                receiver.setMode(mode);
            }
        });
    }

    private void addShapeTypeButton(String text, ShapeType type) {
        Button button = new Button(this, SWT.NONE);
        button.setText(text);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                receiver.setShapeType(type);
            }
        });
    }

}
