package mindsync.gui;

import java.awt.*;

// 표의 한 줄을 비율(fr)대로 나눠 배치한다. 예: new FrLayout(1.4, 1.1, 1, 0.7)
public class FrLayout implements LayoutManager {
    private final double[] fr;

    public FrLayout(double... fr) {
        this.fr = fr;
    }

    @Override public void addLayoutComponent(String name, Component comp) {}
    @Override public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int w = 0, h = 0;
        for (Component c : parent.getComponents()) {
            Dimension d = c.getPreferredSize();
            w += d.width;
            h = Math.max(h, d.height);
        }
        Insets in = parent.getInsets();
        return new Dimension(w + in.left + in.right, h + in.top + in.bottom);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, preferredLayoutSize(parent).height);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets in = parent.getInsets();
        int avail = parent.getWidth() - in.left - in.right;
        int innerH = parent.getHeight() - in.top - in.bottom;
        double total = 0;
        int n = Math.min(fr.length, parent.getComponentCount());
        for (int i = 0; i < n; i++) total += fr[i];
        int x = in.left;
        for (int i = 0; i < n; i++) {
            Component c = parent.getComponent(i);
            int w = (int) Math.round(avail * fr[i] / total);
            Dimension pref = c.getPreferredSize();
            int cw = (c instanceof Card) ? Math.min(pref.width, w) : w;
            int ch = Math.min(pref.height, innerH);
            c.setBounds(x, in.top + (innerH - ch) / 2, cw, ch);
            x += w;
        }
    }
}
