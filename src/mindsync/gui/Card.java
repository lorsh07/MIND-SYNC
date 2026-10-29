package mindsync.gui;

import javax.swing.*;
import java.awt.*;

// 둥근 모서리 + 1px 테두리를 가진 패널 (카드, 칩, 버튼의 바탕)
public class Card extends JPanel {
    private final int arc;
    private Color fill;
    private Color stroke;

    public Card(int radius, Color fill, Color stroke) {
        super(new BorderLayout());
        this.arc = radius * 2;
        this.fill = fill;
        this.stroke = stroke;
        setOpaque(false);
    }

    public void setFill(Color c) {
        this.fill = c;
        repaint();
    }

    public void setStroke(Color c) {
        this.stroke = c;
        repaint();
    }

    public Card pad(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (fill != null) {
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        }
        if (stroke != null) {
            g2.setColor(stroke);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    // 글자만 들어가는 작은 뱃지/칩
    public static Card badge(String text, Color fg, Color bg, Color border, float size, boolean bold, int radius, int padV, int padH) {
        Card c = new Card(radius, bg, border);
        c.pad(padV, padH, padV, padH);
        JLabel l = Theme.label(text, bold ? Theme.bold(size) : Theme.regular(size), fg);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        c.add(l, BorderLayout.CENTER);
        return c;
    }
}
