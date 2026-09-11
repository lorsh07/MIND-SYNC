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

            RoundedPanel moodCard = new RoundedPanel(12);
            //"기분 추이" 전체를 담을 큰 카드예요. KPI 카드랑 똑같이 RoundedPanel(둥근 모서리, 반경 12px)을 재사용해요.
            moodCard.setLayout(new BoxLayout(moodCard, BoxLayout.Y_AXIS));
            //이 카드 안에는 "제목 줄"과 "막대그래프 줄"이 위아래로 쌓일 거라서, 세로 방향 BoxLayout을 씁니다.
            moodCard.setBackground(Color.WHITE);
            //배경은 흰색 (스펙에 나온 카드 배경색)
            moodCard.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
            //카드 안쪽 여백을 18px씩
            moodCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            //왼쪽 정렬로 통일
            moodCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
            //너비는 화면 전체로 늘어나도 되지만, 높이는 220px로 제한

            JPanel moodHeaderRow = new JPanel();
            //"기분 추이" 제목 줄만 담을 작은 패널
            moodHeaderRow.setLayout(new BorderLayout());
            //이 작은 패널 안에서 "왼쪽 끝"과 "오른쪽 끝"으로 글자를 벌리는 용도
            moodHeaderRow.setBackground(Color.WHITE);
            //카드 배경과 똑같이 흰색
            moodHeaderRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            //이 패널도 왼쪽 정렬
            moodHeaderRow.setMaximumSize(new Dimension(Integer.MAX_VALUE,24));
            //너비는 카드 전체, 높이는 글자 한 줄 정도(24px)로 제한

            JLabel moodTitleLabel = new JLabel("기분 추이");
            moodTitleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
            moodTitleLabel.setForeground(new Color(0x16, 0x21, 0x1E));
            //"기분 추이" 글자 라벨을 만들고, 스펙대로 15px Bold, 진한 색

            JLabel moodPeriodLabel = new JLabel("최근 8주");
            moodTitleLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 12));
            moodTitleLabel.setForeground(new Color(0x8A,0x99,0x95));
            //"최근 8주" 글자 라벨을 만들고, 12px Regular, 연한 회색

            moodHeaderRow.add(moodTitleLabel, BorderLayout.WEST);
            //제목을 왼쪽(WEST) 끝에 배치
            moodHeaderRow.add(moodPeriodLabel, BorderLayout.EAST);
            //기간 글자를 오른쪽(EAST) 끝에 배치

            JPanel barsRow = new JPanel();
            //8개 막대를 나란히 담을 줄
            barsRow.setLayout(new GridLayout(1, 8, 9, 0));
            //1줄, 8칸"으로 나눠서 막대 8개를 정확히 같은 너비로 배치해요. 칸 사이 간격은 9px
            barsRow.setBackground(Color.WHITE);
            barsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            int[] moodValues = {38, 52, 44, 66, 58, 78, 72, 90};

            for (int i = 0; i < moodValues.length; i++) {
                boolean isLast = (i == moodValues.length - 1);
                //지금 i가 배열의 마지막 인덱스(7)와 같은가?"를 확인해요. 마지막 반복일 때만 true가 돼요 — 이게 "최신 막대만 진한 색"을 판단하는 조건
                RoundedPanel bar = createBar(moodValues[i], isLast);

                JPanel barWrapper = new JPanel();
                //막대 하나하나를 감싸는 작은 상자
                barWrapper.setLayout(new BorderLayout());
                barWrapper.setBackground(Color.WHITE);
                barWrapper.add(bar, BorderLayout.SOUTH);
                //실제 그래프처럼 "바닥에서부터 자라는" 느낌을 주려면, 각 막대를 BorderLayout.SOUTH(아래쪽)에 붙여서, 짧은 막대는 위에 빈 공간이 남고 긴 막대는 꽉 채우도록 만들어요.

                barsRow.add(barWrapper);
            }
            moodCard.add(moodHeaderRow);
            //"기분 추이 / 최근 8주" 제목 줄을 카드 맨 위에 쌓아요.
            moodCard.add(Box.createVerticalStrut(14));
            //제목 줄과 막대그래프 사이에 14px 간격을 둬요
            moodCard.add(barsRow);
            //막대 8개가 담긴 줄을 그 아래에 쌓아요.

            contentPanel.add(moodCard);
            //이렇게 완성된 "기분 추이" 카드 전체를, 아까 KPI 카드들 아래에 이어서 contentPanel에 쌓아요.
            contentPanel.add(Box.createVerticalStrut(18));
            //다음에 올 요소(복약 배너)와의 간격을 미리 18px 확보

            RoundedPanel medicationBanner =  new RoundedPanel(12);
            medicationBanner.setLayout(new BorderLayout());
            medicationBanner.setBackground(Color.WHITE);
            medicationBanner.setBorder(BorderFactory.createEmptyBorder(14,18,14,18));
            medicationBanner.setAlignmentX(Component.LEFT_ALIGNMENT);
            medicationBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE,60));

            JLabel medicationText = new JLabel("오늘 저녁 8시 · 세르트랄린 50mg");
            medicationText.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
            medicationText.setForeground(new Color(0x4C,0x5A,0x56));

            JLabel checkButton = new JLabel("복용 체크");
            checkButton.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
            checkButton.setForeground(Color.white);
            checkButton.setOpaque(true);
            checkButton.setBackground(new Color(21,100,90));
            checkButton.setBorder(BorderFactory.createEmptyBorder(9,14,9,14));

            medicationBanner.add(medicationText, BorderLayout.WEST);
            medicationBanner.add(checkButton, BorderLayout.EAST);

            contentPanel.add(medicationBanner);

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

    private RoundedPanel createBar(int heightPercent, boolean isLatest) {
            RoundedPanel bar = new RoundedPanel(5);
            if(isLatest) {
                bar.setBackground(new Color(21,100,90));
            }else {
                bar.setBackground(new Color(0xD9, 0xE7, 0xE2));
            }
            //이게 최신 막대면(isLatest가 true면) → 진한 포인트컬러(
        //#15645A)로 칠해라. 아니면(else) → 연한 회녹색(
        //#D9E7E2)으로 칠해라
            bar.setPreferredSize(new Dimension(20, heightPercent));
            return bar;
    }

        }
