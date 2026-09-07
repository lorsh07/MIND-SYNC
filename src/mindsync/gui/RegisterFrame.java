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

        JLabel usernameLabel = createFieldLabel("아이디");
        JTextField usernameField = createStyledField();

        JLabel passwordLabel = createFieldLabel("비밀번호");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setBackground(new Color(0xF7, 0xF9, 0xF8));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDC, 0xE3, 0xE1), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel nameLabel = createFieldLabel("이름");
        JTextField nameField = createStyledField();

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(rolePanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(14));
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(14));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(nameField);
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
    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBackground(new Color(0xF7, 0xF9, 0xF8));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDC, 0xE3, 0xE1), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
        label.setForeground(new Color(0x16, 0x21, 0x1E));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }


}

