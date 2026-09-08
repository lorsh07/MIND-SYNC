package mindsync.gui;

import mindsync.dao.UserDAO;
import mindsync.model.Doctor;
import mindsync.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.UUID;
import javax.swing.JScrollPane;


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

        JButton registerButton = new JButton("가입하기");
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        registerButton.setBackground(new Color(21, 100, 90));
        registerButton.setForeground(Color.white);
        registerButton.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));

        JPanel patientCard = new JPanel();
        patientCard.setLayout(new BoxLayout(patientCard, BoxLayout.Y_AXIS));
        patientCard.setBackground(Color.WHITE);

        JLabel regionLabel = createFieldLabel("거주 지역");
        JTextField regionField = createStyledField();

        patientCard.add(regionLabel);
        patientCard.add(Box.createVerticalStrut(6));
        patientCard.add(regionField);

        JPanel doctorCard = new JPanel();
        doctorCard.setLayout(new BoxLayout(doctorCard, BoxLayout.Y_AXIS));
        doctorCard.setBackground(Color.WHITE);

        JLabel specialtyLabel = createFieldLabel("전문 분야");
        JTextField specialtyField = createStyledField();

        JLabel licenseLabel = createFieldLabel("면허 번호");
        JTextField licenseField = createStyledField();

        JLabel hospitalRegionLabel = createFieldLabel("소속 병원 지역");
        JTextField hospitalRegionField = createStyledField();

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = UUID.randomUUID().toString();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();

                UserDAO userDAO = new UserDAO();
                boolean success;

                if (patientRadio.isSelected()) {
                    String region = regionField.getText();
                    Patient newPatient = new Patient(id, username, password, name, region);
                    success = userDAO.registerPatient(newPatient);
                }else {
                    String hospitalRegion = hospitalRegionField.getText();
                    String licence = licenseField.getText();
                    String specialty = specialtyField.getText();
                    Doctor newDoctor = new Doctor(id, username, password, name, hospitalRegion, licence, specialty);
                    success = userDAO.registerDoctor(newDoctor);
                }
                if (success) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,"회원가입이 완료되었습니다!");
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(RegisterFrame.this,"회원가입에 실패했습니다. 다시 시도해주세요");
                }
            }
        });

        doctorCard.add(hospitalRegionLabel);
        doctorCard.add(Box.createVerticalStrut(6));
        doctorCard.add(hospitalRegionField);
        doctorCard.add(Box.createVerticalStrut(14));
        doctorCard.add(licenseLabel);
        doctorCard.add(Box.createVerticalStrut(6));
        doctorCard.add(licenseField);
        doctorCard.add(Box.createVerticalStrut(14));
        doctorCard.add(specialtyLabel);
        doctorCard.add(Box.createVerticalStrut(6));
        doctorCard.add(specialtyField);

        CardLayout cardLayout = new CardLayout();
        //CardLayout cardLayout = new CardLayout(); — 카드를 관리하는 레이아웃 매니저를 만들어요
        JPanel cardContainer = new JPanel(cardLayout);
        //JPanel cardContainer = new JPanel(cardLayout); — 이 카드들을 담을 새로운 패널을 만들면서, 레이아웃을 CardLayout으로 지정
        cardContainer.setBackground(Color.WHITE);
        cardContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        cardContainer.add(patientCard,"PATIENT");
        cardContainer.add(doctorCard,"DOCTOR");
        //일반 add(부품)이 아니라, add(부품, 이름표)처럼 이름표(문자열)를 같이 붙여서 추가해요. 이 이름표로 나중에 "어떤 카드를 보여줄지" 지정할 수 있어요.

        patientRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContainer,"PATIENT");
            }
        });

        doctorRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContainer,"DOCTOR");
            }
        });

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
        panel.add(cardContainer);
        panel.add(Box.createVerticalStrut(20));
        panel.add(registerButton);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

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

