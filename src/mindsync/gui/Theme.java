package mindsync.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

// 디자인 시안(02~07)에서 쓰는 색, 글꼴, 작은 도우미 모음
public final class Theme {
    private Theme() {}

    public static final Color BG = new Color(0xF7F9F8);
    public static final Color WHITE = Color.WHITE;
    public static final Color GREEN = new Color(0x15645A);
    public static final Color GREEN_DARK = new Color(0x0E4A42);
    public static final Color GREEN_LIGHT = new Color(0xD9E7E2);
    public static final Color TEXT = new Color(0x16211E);
    public static final Color TEXT2 = new Color(0x4C5A56);
    public static final Color GRAY = new Color(0x6B7A76);
    public static final Color GRAY_LIGHT = new Color(0x8A9995);
    public static final Color PLACEHOLDER = new Color(0x9AA8A4);
    public static final Color LINE = new Color(0xE3E8E6);
    public static final Color LINE_INPUT = new Color(0xDCE3E1);
    public static final Color LINE_SOFT = new Color(0xEEF2F1);
    public static final Color LINE_ROW = new Color(0xF2F5F4);
    public static final Color LINE_HOVER = new Color(0xC9D8D3);
    public static final Color BADGE_GRAY = new Color(0xF0F3F2);
    public static final Color WARN = new Color(0x8A5A16);
    public static final Color WARN_BG = new Color(0xF6EBD8);
    public static final Color SIDEBAR_TEXT = new Color(0xB7CEC8);

    private static Font boldBase;
    private static Font regularBase;
    private static boolean loaded = false;

    private static Font read(String path) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(path));
        } catch (Exception e) {
            return null;
        }
    }

    private static void load() {
        if (loaded) return;
        loaded = true;
        boldBase = read("fonts/Pretendard-Bold.otf");
        regularBase = read("fonts/Pretendard-Regular.otf");
    }

    public static Font bold(float size) {
        load();
        return boldBase != null ? boldBase.deriveFont(size) : new Font("맑은 고딕", Font.BOLD, Math.round(size));
    }

    public static Font regular(float size) {
        load();
        return regularBase != null ? regularBase.deriveFont(size) : new Font("맑은 고딕", Font.PLAIN, Math.round(size));
    }

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    // 왼쪽 정렬 가로 줄. FlowLayout은 맨 앞에도 간격을 넣어서, 그만큼 왼쪽으로 당겨 둔다.
    public static JPanel row(int gap) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, gap, 0));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(0, -gap, 0, 0));
        return p;
    }

    // 정렬 메뉴 옆의 작은 아래 화살표 (글꼴에 ▾ 기호가 없어서 직접 그린다)
    public static Icon chevron(final Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawPolyline(new int[]{x + 1, x + 5, x + 9}, new int[]{y + 2, y + 6, y + 2}, 3);
                g2.dispose();
            }
            public int getIconWidth() { return 10; }
            public int getIconHeight() { return 8; }
        };
    }

    public static ImageIcon icon(String path, int size) {
        ImageIcon raw = new ImageIcon(path);
        return new ImageIcon(raw.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    // 이름의 첫 글자 (아바타 자리에 사용)
    public static String initial(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        return name.trim().substring(0, 1);
    }

    // "2026-10-29" -> "10. 29"
    public static String shortDate(String ymd) {
        try {
            LocalDate d = LocalDate.parse(ymd);
            return d.getMonthValue() + ". " + d.getDayOfMonth();
        } catch (Exception e) {
            return ymd;
        }
    }

    // "2026-10-29" -> "2026. 10. 29"
    public static String fullDate(String ymd) {
        try {
            LocalDate d = LocalDate.parse(ymd);
            return d.getYear() + ". " + d.getMonthValue() + ". " + d.getDayOfMonth();
        } catch (Exception e) {
            return ymd;
        }
    }

    // "15:00" -> "오후 3:00"
    public static String clock(String hhmm) {
        try {
            String[] p = hhmm.split(":");
            int h = Integer.parseInt(p[0]);
            String ampm = h < 12 ? "오전" : "오후";
            int h12 = h % 12 == 0 ? 12 : h % 12;
            return ampm + " " + h12 + ":" + p[1];
        } catch (Exception e) {
            return hhmm;
        }
    }
}
