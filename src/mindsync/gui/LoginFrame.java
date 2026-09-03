package mindsync.gui;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("MIND SYNC - 로그인");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

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
        titleLabel.setFont(new Font("맑은 고딕",Font.BOLD,24));
        //titleLabel 의 글자색을 포인트 컬러 #1E6657 로 설정하기
        titleLabel.setForeground(new Color(0x1E,0x66,0x57));
        //titleLabel을, 가로 방향 기준으로 가운데에 놓아라.
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //사용자가 글자를 입력할 수 있는 한 줄짜리 입력창을 만들어서, usernameField라는 이름으로 저장한다
        JTextField usernameField = new JTextField();
    }
}
