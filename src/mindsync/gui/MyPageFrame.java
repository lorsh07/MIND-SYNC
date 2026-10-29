package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.UserDAO;
import mindsync.model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// 환자 화면: 내 정보 + 진료 내역(상태, Zoom 링크) + 로그아웃
public class MyPageFrame extends JFrame {

    private static final Color BG = new Color(0xF7, 0xF9, 0xF8);
    private static final Color GREEN = new Color(0x15, 0x64, 0x5A);
    private static final Color LIGHT_GREEN = new Color(0xD9, 0xE7, 0xE2);
    private static final Color TEXT = new Color(0x16, 0x21, 0x1E);
    private static final Color GRAY = new Color(0x6B, 0x7A, 0x76);
    private static final Color LIGHT_GRAY = new Color(0x8A, 0x99, 0x95);

    public MyPageFrame(String patientId, String patientName) {
        setTitle("MIND SYNC - 마이페이지");
        setSize(760, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        UserDAO userDAO = new UserDAO();
        String region = userDAO.getRegionByUserId(patientId);
        if (region == null || region.isEmpty()) {
            region = "지역 정보 없음";
        }

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(32, 32, 24, 32));

        // ── 위: 내 정보 카드 ──
        JLabel titleLabel = new JLabel("마이페이지");
        titleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 22));
        titleLabel.setForeground(TEXT);

        JLabel logoutLabel = AppNavigator.createTextButton("로그아웃", loadFont("fonts/Pretendard-Regular.otf", 13));
        logoutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AppNavigator.logout();
            }
        });

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(BG);
        headerRow.add(titleLabel, BorderLayout.WEST);
        headerRow.add(logoutLabel, BorderLayout.EAST);

        RoundedPanel infoCard = new RoundedPanel(12);
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel nameLabel = new JLabel(patientName + " 님");
        nameLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 18));
        nameLabel.setForeground(TEXT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel regionLabel = new JLabel("거주 지역 · " + region);
        regionLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        regionLabel.setForeground(GRAY);
        regionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoCard.add(nameLabel);
        infoCard.add(Box.createVerticalStrut(6));
        infoCard.add(regionLabel);

        JLabel historyTitle = new JLabel("진료 내역");
        historyTitle.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
        historyTitle.setForeground(TEXT);

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(BG);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(headerRow);
        topSection.add(Box.createVerticalStrut(18));
        topSection.add(infoCard);
        topSection.add(Box.createVerticalStrut(24));
        topSection.add(historyTitle);
        topSection.add(Box.createVerticalStrut(12));

        // ── 아래: 진료 내역 목록 ──
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(BG);

        List<Appointment> appointments = new AppointmentDAO().getAppointmentsByPatientId(patientId);

        if (appointments.isEmpty()) {
            JLabel emptyLabel = new JLabel("진료 내역이 없어요");
            emptyLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 15));
            emptyLabel.setForeground(LIGHT_GRAY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            list.add(emptyLabel);
        } else {
            for (Appointment a : appointments) {
                RoundedPanel card = createHistoryCard(a, userDAO);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                list.add(card);
                list.add(Box.createVerticalStrut(12));
            }
        }

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(BG);
        listWrapper.add(list, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(topSection, BorderLayout.NORTH);
        contentPanel.add(scroll, BorderLayout.CENTER);

        add(contentPanel);
        setVisible(true);
    }

    // 진료 내역 카드: 일시 + 상태 칩 / 의사 · 방식 / 증상 / (확정 시) Zoom 링크
    private RoundedPanel createHistoryCard(Appointment a, UserDAO userDAO) {
        String doctorName = userDAO.getNameByUserId(a.getDoctorId());
        if (doctorName == null) {
            doctorName = "담당의";
        }

        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        String time = a.getScheduledAt();
        if (time == null || time.trim().isEmpty()) {
            time = "시간 미정";
        }
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
        timeLabel.setForeground(TEXT);

        JLabel statusChip = new JLabel(a.getStatus());
        statusChip.setFont(loadFont("fonts/Pretendard-Bold.otf", 12));
        statusChip.setOpaque(true);
        statusChip.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        if ("확정".equals(a.getStatus())) {
            statusChip.setForeground(Color.WHITE);
            statusChip.setBackground(GREEN);
        } else if ("완료".equals(a.getStatus())) {
            statusChip.setForeground(GRAY);
            statusChip.setBackground(new Color(0xEE, 0xF2, 0xF1));
        } else {
            statusChip.setForeground(GREEN);
            statusChip.setBackground(LIGHT_GREEN);
        }

        JPanel chipBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        chipBox.setBackground(Color.WHITE);
        chipBox.add(statusChip);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(Color.WHITE);
        topRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        topRow.add(timeLabel, BorderLayout.WEST);
        topRow.add(chipBox, BorderLayout.EAST);

        String type = a.getConsultationType() == null ? "-" : a.getConsultationType();
        JLabel doctorLabel = new JLabel(doctorName + " 선생님 · " + type);
        doctorLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        doctorLabel.setForeground(GRAY);
        doctorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String symptom = a.getSymptom();
        if (symptom == null || symptom.trim().isEmpty()) {
            symptom = "증상 입력 없음";
        }
        JTextArea symptomText = new JTextArea(symptom);
        symptomText.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        symptomText.setForeground(new Color(0x4C, 0x5A, 0x56));
        symptomText.setBackground(Color.WHITE);
        symptomText.setLineWrap(true);
        symptomText.setWrapStyleWord(true);
        symptomText.setEditable(false);
        symptomText.setFocusable(false);
        symptomText.setBorder(null);
        symptomText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel centerBox = new JPanel();
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));
        centerBox.setBackground(Color.WHITE);
        centerBox.add(doctorLabel);
        centerBox.add(Box.createVerticalStrut(6));
        centerBox.add(symptomText);

        if ("확정".equals(a.getStatus()) && a.getMeetingLink() != null && !a.getMeetingLink().isEmpty()) {
            JLabel linkLabel = new JLabel("Zoom 링크 · " + a.getMeetingLink());
            linkLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
            linkLabel.setForeground(GREEN);
            linkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerBox.add(Box.createVerticalStrut(8));
            centerBox.add(linkLabel);
        }

        card.add(topRow, BorderLayout.NORTH);
        card.add(centerBox, BorderLayout.CENTER);
        return card;
    }

    private Font loadFont(String path, float size) {
        return AppNavigator.loadFont(path, size);
    }
}
