package mindsync.gui;

import mindsync.model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;

// 환자 화면의 틀: 왼쪽 사이드바 + 오른쪽 내용 영역 (시안 03)
public class PatientDashboardFrame extends JFrame {
    private final String patientId;
    private final String patientName;
    private final JPanel content = new JPanel(new BorderLayout());
    private final Map<String, Card> menus = new LinkedHashMap<String, Card>();
    private String activeKey = "";

    public PatientDashboardFrame(String patientId, String patientName) {
        this.patientId = patientId;
        this.patientName = patientName;

        setTitle("MIND SYNC");
        setSize(1200, 740);
        setMinimumSize(new Dimension(1000, 640));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);

        content.setBackground(Theme.BG);
        content.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);

        showDashboard();
        setVisible(true);
    }

    // ---------- 화면 전환 ----------

    public void showDashboard() {
        setActive("dashboard");
        setContent(new DashboardPanel(this, patientId, patientName));
    }

    public void showSearch() {
        setActive("reserve");
        setContent(new DoctorSearchPanel(this, patientId));
    }

    public void showDetail(Doctor doctor) {
        setActive("reserve");
        setContent(new DoctorDetailPanel(this, patientId, doctor));
    }

    // tab: 0 처방·투약, 1 진단 기록, 2 진료 내역
    public void showMyPage(int tab) {
        setActive(tab == 0 ? "rx" : "mypage");
        setContent(new MyPagePanel(this, patientId, patientName, tab));
    }

    public void openSelfDiagnosis() {
        SelfDiagnosisFrame frame = new SelfDiagnosisFrame(patientId);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                showDashboard();
            }
        });
    }

    private void setContent(JComponent c) {
        content.removeAll();
        content.add(c, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private void setActive(String key) {
        activeKey = key;
        for (Map.Entry<String, Card> e : menus.entrySet()) {
            boolean on = e.getKey().equals(key);
            e.getValue().setFill(on ? new Color(255, 255, 255, 41) : null);
            JLabel l = (JLabel) e.getValue().getComponent(0);
            l.setFont(on ? Theme.bold(14) : Theme.regular(14));
            l.setForeground(on ? Color.WHITE : Theme.SIDEBAR_TEXT);
        }
    }

    // ---------- 사이드바 ----------

    private JComponent buildSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setBackground(Theme.GREEN_DARK);
        side.setPreferredSize(new Dimension(240, 0));
        side.setBorder(BorderFactory.createEmptyBorder(22, 16, 22, 16));

        Stack top = new Stack(22);

        JPanel logoRow = Theme.row(9);
        logoRow.setOpaque(false);
        logoRow.add(new JLabel(Theme.icon("assets/mind-sync-mark-white-512.png", 22)));
        logoRow.add(Theme.label("MIND SYNC", Theme.bold(15), Color.WHITE));
        top.add(logoRow);

        Stack menuBox = new Stack(4);
        menuBox.add(menuItem("dashboard", "대시보드", new Runnable() { public void run() { showDashboard(); } }));
        menuBox.add(menuItem("diagnosis", "자가진단", new Runnable() { public void run() { openSelfDiagnosis(); } }));
        menuBox.add(menuItem("reserve", "진료 예약", new Runnable() { public void run() { showSearch(); } }));
        menuBox.add(menuItem("rx", "처방·투약", new Runnable() { public void run() { showMyPage(0); } }));
        menuBox.add(menuItem("mypage", "마이페이지", new Runnable() { public void run() { showMyPage(2); } }));
        top.add(menuBox);
        top.finish();
        side.add(top, BorderLayout.CENTER);

        Stack bottom = new Stack(4);
        bottom.add(Theme.label(patientName + "님", Theme.bold(14), Color.WHITE));
        JLabel logout = Theme.label("로그아웃", Theme.regular(13), Theme.SIDEBAR_TEXT);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AppNavigator.logout();
            }
        });
        bottom.add(logout);
        side.add(bottom, BorderLayout.SOUTH);
        return side;
    }

    private Card menuItem(final String key, String text, final Runnable action) {
        final Card item = new Card(8, null, null);
        item.pad(11, 12, 11, 12);
        item.add(Theme.label(text, Theme.regular(14), Theme.SIDEBAR_TEXT), BorderLayout.CENTER);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!key.equals(activeKey)) item.setFill(new Color(255, 255, 255, 20));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!key.equals(activeKey)) item.setFill(null);
            }
        });
        menus.put(key, item);
        return item;
    }
}
