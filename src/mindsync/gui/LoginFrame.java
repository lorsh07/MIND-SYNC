package mindsync.gui;

import mindsync.dao.UserDAO;
import mindsync.model.Patient;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.Box;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import mindsync.dao.UserDAO;
import mindsync.model.Patient;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("MIND SYNC - 로그인");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        //부품들을 담을 빈 판(패널)을 하나 만든다
        JPanel panel = new JPanel();
        //이 패널 안에 넣는 부품들은, 위에서 아래로(Y축 방향) 순서대로 쌓아라
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        //이 패널의 배경색을 흰색으로 칠해라
        panel.setBackground(Color.WHITE);
        //패널 안쪽에, 내용물이 가장자리에 딱 붙지 않도록 여백을 둬라
        panel.setBorder(BorderFactory.createEmptyBorder(40,30,40,30));
        //MIND SYNC'라는 글자를 보여주는 라벨을 하나 만들어서, titleLabel이라는 이름으로 저장한다
        JLabel titleLabel = new JLabel("MIND SYNC");
        //titleLabel의 글꼴을, '맑은 고딕' 서체로, 굵게, 크기 24로 설정해라
        titleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 24));
        //titleLabel 의 글자색을 포인트 컬러 #1E6657 로 설정하기
        titleLabel.setForeground(new Color(21, 100, 90));

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleLabel.getPreferredSize().height));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("마음을 돌보는 가장 편안한 방법");
        subtitleLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        subtitleLabel.setForeground(new Color(0x6B,0x7A,0x76));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, subtitleLabel.getPreferredSize().height));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon logoIcon = new ImageIcon("assets/mind-sync-mark-512.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, logoLabel.getPreferredSize().height));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //사용자가 글자를 입력할 수 있는 한 줄짜리 입력창을 만들어서, usernameField라는 이름으로 저장한다
        JTextField usernameField = new JTextField();
        //이 입력창이 커질 수 있는 최대 크기를, 가로 300px, 세로 35px로 제한
        JLabel usernameLabel = new JLabel("아이디");
        usernameLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 14));
        usernameLabel.setForeground(new Color(0x16,0x21,0x1E));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));

        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setBackground(new Color(0xF7,0xF9,0xF8));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDC,0xE3,0xE1),1),
                BorderFactory.createEmptyBorder(10,12,10,12)

        ));
        //비밀번호 입력창 만들기
        JPasswordField passwordField = new JPasswordField();

        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 14));
        passwordLabel.setForeground(new Color(0x16,0x21,0x1E));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);

        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));

        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(21, 100, 90),1),
                BorderFactory.createEmptyBorder(10,12,10,12)
        ));
        //로그인 버튼 만들기
        JButton loginButton = new JButton("로그인");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));
        //버튼 배경색을 포인트 컬러로
        loginButton.setBackground(new Color(21, 100, 90));
        //버튼 글자색을 흰색으로
        loginButton.setForeground(Color.WHITE);
        // 패널에 붙이기
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                //usernameField.getText() — 아이디 입력창에 지금 사용자가 입력해둔 글자를 꺼내옴. 반환 타입이 그냥 String
                String password = new String(passwordField.getPassword());
                //passwordField.getPassword() — 비밀번호 입력창에서 값을 꺼내는데, 이건 좀 특이하게 char[](문자 배열) 타입
                UserDAO userDAO = new UserDAO();
                Patient loggedInPatient = userDAO.loginPatient(username,password);

                if (loggedInPatient != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "로그인 성공! 환영합니다, " + loggedInPatient.getName() + "님");
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "아이디 또는 비밀번호가 올바르지 않습니다.");
                }
            }
        });
        //BoxLayout.Y_AXIS라서 추가한 순서대로 위에서 아래로 쌓인다.
        //Box.createVerticalStrut(30) — "30픽셀짜리 빈 세로 공간"을 만드는 특수한 부품
        //제목 → 간격 → 아이디입력 → 간격 → 비밀번호입력 → 간격 → 버튼 순서
        add(panel);
        setVisible(true);
    }

    private Font loadFont(String path, float size) {
        try{
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            //Font.TRUETYPE_FONT — "이 파일은 트루타입/오픈타입 계열 폰트다"라는 힌트
            // Font.createFont(...) — 그 파일을 읽어서, 자바가 쓸 수 있는 Font 객체로 변환
            //new File(path) — 컴퓨터 안의 실제 파일(우리가 넣은 otf 파일)을 자바에서 다루는 객체로 만듦
            return font.deriveFont(size);
            //deriveFont(size)는 "이 폰트를 기반으로, 크기만 이걸로 바꾼 새 버전을 만들어라"는 뜻
        }catch (Exception e){
            e.printStackTrace();
            return  new Font("맑은 고딕", Font.PLAIN, (int) size);
        }
    }
}
