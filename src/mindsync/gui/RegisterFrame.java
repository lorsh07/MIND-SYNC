package mindsync.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.FlowLayout;

public class RegisterFrame extends JFrame {
//RegisterFrame은 로그인 화면에서 잠깐 열리는 보조 창이에요
    public RegisterFrame() {
        setTitle("MIND SYNC - 회원가입");
        setSize(400,550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //DISPOSE_ON_CLOSE — "이 창만 닫아라, 나머지 프로그램은 계속 실행돼도 된다"는 뜻
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 22));
        titleLabel.setForeground(new Color(21, 100, 90));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleLabel.getPreferredSize().height));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JRadioButton patientRadio = new JRadioButton("환자", true);
        JRadioButton doctorRadio = new JRadioButton("의사");

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(patientRadio);
        roleGroup.add(doctorRadio);

        JPanel rolePanel = new JPanel();
        rolePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rolePanel.setBackground(Color.WHITE);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        rolePanel.add(patientRadio);
        rolePanel.add(doctorRadio);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(rolePanel);
        panel.add(Box.createVerticalStrut(20));
        add(panel);

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


}

