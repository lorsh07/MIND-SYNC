package mindsync.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 시안의 버튼 (주 버튼 / 보조 버튼 / 비활성 / 완료 상태)
public class Btn extends Card {
    public enum Style { PRIMARY, SECONDARY, DISABLED, DONE }

    private final JLabel label;
    private Style style;
    private Runnable action;
    private boolean hover = false;

    public Btn(String text, float size, int padV, int padH, int radius) {
        super(radius, Theme.GREEN, null);
        pad(padV, padH, padV, padH);
        label = Theme.label(text, Theme.bold(size), Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
        setStyle(Style.PRIMARY);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (style != Style.DISABLED && style != Style.DONE && action != null) action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                apply();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                apply();
            }
        });
    }

    public void onClick(Runnable r) {
        this.action = r;
    }

    public void setText(String t) {
        label.setText(t);
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style s) {
        this.style = s;
        apply();
    }

    private void apply() {
        switch (style) {
            case PRIMARY:
                setFill(hover ? Theme.GREEN_DARK : Theme.GREEN);
                setStroke(null);
                label.setForeground(Color.WHITE);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                break;
            case SECONDARY:
                setFill(hover ? Theme.BG : Color.WHITE);
                setStroke(Theme.LINE_HOVER);
                label.setForeground(Theme.GREEN);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                break;
            case DONE:
                setFill(Theme.GREEN_LIGHT);
                setStroke(null);
                label.setForeground(Theme.GREEN);
                setCursor(Cursor.getDefaultCursor());
                break;
            default:
                setFill(Theme.GREEN_LIGHT);
                setStroke(null);
                label.setForeground(Theme.GRAY_LIGHT);
                setCursor(Cursor.getDefaultCursor());
        }
    }
}
