package mindsync.gui;

import mindsync.db.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.GridLayout;


public class PatientDashboardFrame extends JFrame {

        public PatientDashboardFrame(String patientName) {
            setTitle("MIND SYNC - 대시보드");
            setSize(1280,800);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(new Color(0x0E,0x4A,0x42));
            sidebar.setBorder(BorderFactory.createEmptyBorder(22,16,22,16));
            sidebar.setPreferredSize(new Dimension(240,800));

            JPanel logoPanel = new JPanel();
            logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT,9,0));
            logoPanel.setBackground(new Color(0x0E,0x4A,0x42));
            logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
            logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel dashboardMenu = new JLabel("대시보드");
            dashboardMenu.setFont(loadFont("fonts/Pretendard-Bold.otf", 14));

            dashboardMenu.setForeground(Color.white);
            dashboardMenu.setOpaque(true);
            dashboardMenu.setBackground(new Color(255,255,255,41));
            dashboardMenu.setBorder(BorderFactory.createEmptyBorder(11,12,11,12));
            dashboardMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
            dashboardMenu.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
            JLabel selfDiagnosisMenu = createInactiveMenu("자가진단");
            JLabel appointmentMenu = createInactiveMenu("진료 예약");
            JLabel prescriptionMenu = createInactiveMenu("처방·투약");
            JLabel communityMenu = createInactiveMenu("커뮤니티");
            JLabel myPageMenu = createInactiveMenu("마이페이지");

            ImageIcon logoIcon = new ImageIcon("assets/mind-sync-mark-white-512.png");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(22,22, Image.SCALE_SMOOTH);
            JLabel logoImageLabel = new JLabel(new ImageIcon(scaledLogo));

            JLabel logoTextLabel = new JLabel("MIND SYNC");
            logoTextLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
            logoTextLabel.setForeground(Color.white);

            logoPanel.add(logoImageLabel);
            logoPanel.add(logoTextLabel);

            sidebar.add(logoPanel);
            sidebar.add(Box.createVerticalStrut(22));
            sidebar.add(dashboardMenu);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(selfDiagnosisMenu);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(appointmentMenu);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(prescriptionMenu);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(communityMenu);
            sidebar.add(Box.createVerticalStrut(4));
            sidebar.add(myPageMenu);
            add(sidebar, BorderLayout.WEST);

            RoundedPanel phqCard = createKpiCard("PHQ-9", "8점", "경증", new Color(21,100,90));
            RoundedPanel adherenceCard = createKpiCard("복약 순응도", "92%", "최근 30일", new Color(0x8A,0x99,0x95));
            RoundedPanel nextAppointmentCard = createKpiCard("다음 진료","9.12","오후 3:00", new Color(0x8A,0x99,0x95));

            JPanel kpiRow = new JPanel();
            kpiRow.setLayout(new GridLayout(1,3,12,0));
            //부품들을 정확히 같은 크기의 격자(줄x칸)로 배치하는 레이아웃
            kpiRow.setBackground(new Color(0xF7,0xF9,0xF8));
            kpiRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            kpiRow.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));

            kpiRow.add(phqCard);
            kpiRow.add(adherenceCard);
            kpiRow.add(nextAppointmentCard);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(0xF7,0xF9,0xF8));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(32,32,32,32));

            JLabel greetingLabel = new JLabel("안녕하세요, " + patientName + "님");
            greetingLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 20));
            greetingLabel.setForeground(new Color(0x16, 0x21, 0x1E));
            greetingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            greetingLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, greetingLabel.getPreferredSize().height));

            JLabel subGreetingLabel = new JLabel("오늘의 자가진단이 아직 남아 있어요.");
            subGreetingLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
            subGreetingLabel.setForeground(new Color(0x6B, 0x7A, 0x76));
            subGreetingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            subGreetingLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, subGreetingLabel.getPreferredSize().height));

            contentPanel.add(greetingLabel);
            contentPanel.add(subGreetingLabel);
            contentPanel.add(Box.createVerticalStrut(18));
            contentPanel.add(kpiRow);
            contentPanel.add(Box.createVerticalStrut(18));

            add(contentPanel, BorderLayout.CENTER);

            setResizable(true);
            setVisible(true);
        }

        private Font loadFont(String path, float size) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
                return font.deriveFont(size);
            } catch (Exception e) {
                e.printStackTrace();
                return new Font("맑은 고딕", Font.PLAIN, (int) size);
            }
        }

        private JLabel createInactiveMenu(String text) {
            JLabel menu = new JLabel(text);
            menu.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
            menu.setForeground(new Color(0x87, 0xCE, 0xC8));
            menu.setBorder(BorderFactory.createEmptyBorder(11, 12, 11, 12));
            menu.setAlignmentX(Component.LEFT_ALIGNMENT);
            menu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            return menu;
        }
    private RoundedPanel createKpiCard(String label, String value, String caption, Color captionColor) {
        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.white);
        card.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JLabel labelText = new JLabel(label);
        labelText.setFont(loadFont("fonts/Pretendard-Regular.otf", 12));
        labelText.setForeground(new Color(0x8A,0x99,0x95));
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueText = new JLabel(value);
        valueText.setFont(loadFont("fonts/Pretendard-Bold.otf", 22));
        valueText.setForeground(new Color(0x16,0x21,0x1E));
        valueText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel captionText = new JLabel(caption);
        captionText.setFont(loadFont("fonts/Pretendard-Bold.otf", 12));
        captionText.setForeground(captionColor);
        captionText.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(labelText);
        card.add(valueText);
        card.add(captionText);

        return card;
    }

        }
