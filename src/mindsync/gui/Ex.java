package mindsync.gui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class Ex extends JFrame {

    public Ex() {
        setTitle("MIND SYNC");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. 전체 창의 배치를 BorderLayout으로 설정
        setLayout(new BorderLayout());

        // 2. 왼쪽 사이드바 역할을 할 '방(JPanel)' 만들기
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Color.LIGHT_GRAY); // 구분을 위한 임시 회색 배경
        sidebar.setPreferredSize(new Dimension(200, 0)); // 가로 크기 200 고정

        // 3. 오른쪽 메인 콘텐츠 역할을 할 '방(JPanel)' 만들기
        JPanel contentArea = new JPanel();
        contentArea.setBackground(Color.WHITE); // 구분을 위한 흰색 배경

        // 4. 메인 창에 방들 끼워 넣기
        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ex app = new Ex();
            app.setVisible(true); // 이 부분이 있어야 화면에 나타납니다!
        });
    }
}