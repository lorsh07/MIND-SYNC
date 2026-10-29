package mindsync.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

// 화면 이동에 공통으로 쓰는 도우미 클래스
public class AppNavigator {

    // 열려 있는 창을 모두 닫고 로그인 화면으로 돌아간다
    public static void logout() {
        for (Window w : Window.getWindows()) {
            w.dispose();
        }
        new LoginFrame();
    }

    // 화면 상단에 쓰는 작은 텍스트 버튼(로그아웃 등)
    public static JLabel createTextButton(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(0x6B, 0x7A, 0x76));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    public static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("맑은 고딕", Font.PLAIN, (int) size);
        }
    }
}
