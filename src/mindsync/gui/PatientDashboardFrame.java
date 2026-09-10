package mindsync.gui;

import mindsync.db.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;


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

            JPanel logoPanel = new JPanel();
            logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT,9,0));
            logoPanel.setBackground(new Color(0x0E,0x4A,0x42));
            logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));

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

        }
