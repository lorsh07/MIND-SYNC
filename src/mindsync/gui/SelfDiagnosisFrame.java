package mindsync.gui;

import mindsync.dao.SelfDiagnosisDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SelfDiagnosisFrame extends JFrame {

    private String patientId;

    private int currentIndex = 3;
    private int[] answers = new int[9];

    private JLabel progressChip;
    private RoundedPanel progressFill;
    private JLabel questionCardLabel1;
    private JTextArea questionTextArea;
    private JPanel optionList;
    private LocalDate bookingDate;
    private JLabel summaryLabel;
    private RoundedPanel bookButton;
    private JLabel bookButtonLabel;

    private final String[] questions = {
            "지난 2주간, 일 또는 여가 활동을 하는 데 흥미나 즐거움을 느끼지 못한 날이 얼마나 되나요?",
            "지난 2주간, 기분이 가라앉거나 우울하거나 희망이 없다고 느낀 날이 얼마나 되나요?",
            "지난 2주간, 잠들기 어렵거나 자주 깼거나 너무 많이 잔 날이 얼마나 되나요?",
            "지난 2주간, 피곤하다고 느끼거나 기운이 없었던 날이 얼마나 되나요?",
            "지난 2주간, 식욕이 없거나 너무 많이 먹은 날이 얼마나 되나요?",
            "지난 2주간, 내 자신이 실패자라고 느끼거나 가족을 실망시켰다고 느낀 날이 얼마나 되나요?",
            "지난 2주간, 신문을 읽거나 TV를 보는 것 같은 일에 집중하기 어려운 날이 얼마나 되나요?",
            "지난 2주간, 다른 사람이 알아챌 만큼 말과 행동이 느리거나 반대로 안절부절못한 날이 얼마나 되나요?",
            "지난 2주간, 죽는 게 낫겠다고 생각하거나 어떤 식으로든 자신을 해치고 싶은 생각이 든 날이 얼마나 되나요?"
    };

    private final String[] optionTexts = {"전혀 없었다", "며칠 정도", "일주일 이상", "거의 매일"};

    public SelfDiagnosisFrame(String patientId) {
        setTitle("MIND SYNC - 자가진단");
        setSize(700, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        RoundedPanel mainCard = new RoundedPanel(14);
        mainCard.setLayout(new BoxLayout(mainCard, BoxLayout.Y_AXIS));
        mainCard.setBackground(Color.white);
        mainCard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel headerRow = new JPanel();
        headerRow.setLayout(new BorderLayout());
        headerRow.setBackground(Color.white);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setBackground(Color.white);

        JLabel questionTitleLabel = new JLabel("우울 척도 자가진단");
        JLabel questionSubtitleLabel = new JLabel("PHQ-9 · 총 9문항 · 약 3분");
        questionTitleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 20));
        questionSubtitleLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        questionTitleLabel.setForeground(new Color(0x16, 0x21, 0x1E));
        questionSubtitleLabel.setForeground(new Color(0x6B, 0x7A, 0x76));

        titleGroup.add(questionTitleLabel);
        titleGroup.add(questionSubtitleLabel);
        headerRow.add(titleGroup, BorderLayout.WEST);

        progressChip = new JLabel("4 / 9");
        progressChip.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
        progressChip.setForeground(new Color(21, 100, 90));
        progressChip.setOpaque(true);
        progressChip.setBackground(new Color(0xD9, 0xE7, 0xE2));
        progressChip.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        headerRow.add(progressChip, BorderLayout.EAST);

        RoundedPanel progressTrack = new RoundedPanel(3);
        progressFill = new RoundedPanel(3);
        progressFill.setBackground(new Color(0x15, 0x64, 0x5A));
        progressTrack.setBackground(new Color(0xEE, 0xF2, 0xF1));
        progressTrack.setLayout(new BorderLayout());
        progressTrack.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressTrack.setPreferredSize(new Dimension(100, 6));
        progressTrack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        progressFill.setPreferredSize(new Dimension(200, 6));
        progressTrack.add(progressFill, BorderLayout.WEST);


        RoundedPanel questionCard = new RoundedPanel(12);
        questionCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        questionCard.setBackground(new Color(0xF7, 0xF9, 0xF8));
        questionCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE3, 0xE8, 0xE6), 1),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));
        questionCardLabel1 = new JLabel("질문 4");
        questionCardLabel1.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
        questionCardLabel1.setForeground(new Color(0x8A, 0x99, 0x95));

        questionTextArea = new JTextArea(questions[0]);
        questionTextArea.setFont(loadFont("fonts/Pretendard-Bold.otf", 19));
        questionTextArea.setForeground(new Color(0x16, 0x21, 0x1E));
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFocusable(false);
        questionTextArea.setOpaque(false);
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        questionTextArea.setPreferredSize(new Dimension(100, 84));


        questionCardLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionCardLabel1.setMaximumSize(new Dimension(Integer.MAX_VALUE, questionCardLabel1.getPreferredSize().height));
        questionCardLabel1.setHorizontalAlignment(SwingConstants.LEFT);

        questionCard.setLayout(new BorderLayout());

        JPanel questionTop = new JPanel(new BorderLayout());
        questionTop.setOpaque(false);
        questionTop.add(questionCardLabel1, BorderLayout.NORTH);
        questionTop.add(questionTextArea, BorderLayout.CENTER);

        optionList = new JPanel();
        optionList.setLayout(new BoxLayout(optionList, BoxLayout.Y_AXIS));
        optionList.setOpaque(false);
        renderOptions();

        questionCard.add(questionTop, BorderLayout.NORTH);
        questionCard.add(optionList, BorderLayout.CENTER);

        JLabel disclaimerLabel = new JLabel("응답은 진단이 아니며, 의료진 상담의 참고 자료로 쓰입니다.");
        disclaimerLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        disclaimerLabel.setForeground(new Color(0x8A, 0x99, 0x95));

        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBackground(Color.WHITE);
        actionBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        actionBar.add(disclaimerLabel, BorderLayout.WEST);

        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setBackground(Color.WHITE);

        RoundedPanel prevButton = createActionButton("이전", false);
        RoundedPanel nextButton = createActionButton("다음", true);

        buttonGroup.add(prevButton);
        buttonGroup.add(nextButton);

        prevButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    showQuestion();
                }
            }
        });

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (answers[currentIndex] == -1) return;
                if (currentIndex < 8) {
                    currentIndex++;
                    showQuestion();
                }
            }
        });
        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (answers[currentIndex] == -1) return;
                if (currentIndex < 8) {
                    currentIndex++;
                    showQuestion();
                }else{
                    int total = 0;
                    for(int a : answers) total += a;
                    String result = interpret(total);
                    new SelfDiagnosisDAO().createResult(patientId,"PHQ-9",
                            java.time.LocalDate.now().toString(), total, result);
                    JOptionPane.showMessageDialog(SelfDiagnosisFrame.this,
                            "총점 "+total+"점 ("+result+")\n※ 진단이 아닌 참고 자료입니다.");
                    dispose();
                }
            }
        });




        actionBar.add(buttonGroup, BorderLayout.EAST);

        mainCard.add(headerRow);
        mainCard.add(Box.createVerticalStrut(18));

        mainCard.add(progressTrack);
        mainCard.add(Box.createVerticalStrut(18));


        mainCard.add(questionCard);
        mainCard.add(Box.createVerticalStrut(18));
        mainCard.add(actionBar);
        mainCard.add(Box.createVerticalStrut(18));

        java.util.Arrays.fill(answers, -1);
        currentIndex = 0;
        showQuestion();

        add(mainCard);
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


    private RoundedPanel createOption(String text, boolean selected) {
        RoundedPanel option = new RoundedPanel(10);
        option.setLayout(new BorderLayout());
        option.setAlignmentX(Component.LEFT_ALIGNMENT);
        option.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel label = new JLabel(text);

        if (selected) {
            option.setBackground(new Color(0x15, 0x64, 0x5A));
            option.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x15, 0x64, 0x5A), 1),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)


            ));
            label.setForeground(Color.white);
            label.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
        } else {
            option.setBackground(Color.white);
            option.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xDC, 0xE3, 0xE1), 1),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)

            ));
            label.setForeground(new Color(0x4C, 0x5A, 0x56));
            label.setFont(loadFont("fonts/Pretendard-Regular.otf", 15));
        }
        option.add(label, BorderLayout.WEST);
        return option;

    }

    private RoundedPanel createActionButton(String text, boolean primary) {
        RoundedPanel button = new RoundedPanel(10);
        JLabel label = new JLabel(text);
        label.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));

        if (primary) {
            button.setBackground(new Color(0x15, 0x64, 0x5A));
            label.setForeground(Color.white);
            button.setBorder(BorderFactory.createEmptyBorder(14, 26, 14, 26));
        } else {
            button.setBackground(Color.white);
            label.setForeground(new Color(0x15, 0x64, 0x5A));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xC9, 0xD8, 0xD3), 1),
                    BorderFactory.createEmptyBorder(13, 20, 13, 20)
            ));
        }

        button.add(label);
        return button;
    }

    private void renderOptions() {
        optionList.removeAll();
        for (int i = 0; i < optionTexts.length; i++) {
            final int score = i;
            RoundedPanel option = createOption(optionTexts[i], answers[currentIndex] == i);
            optionList.add(option);
            if (i < optionTexts.length - 1) {
                optionList.add(Box.createVerticalStrut(10));
            }
            option.addMouseListener((new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    answers[currentIndex] = score;
                    renderOptions();
                    if(currentIndex == 8 && score >0 ) {
                        showCrisisNotice();
                    }
                }
            }));
        }
        optionList.revalidate();
        optionList.repaint();
    }

    private void showCrisisNotice() {
        JOptionPane.showMessageDialog(this,
                "힘든 마음을 솔직하게 답해주셔서 감사합니다.\\n\"\n" +
                        "            + \"혼자 견디지 않으셔도 괜찮아요. 지금 바로 이야기를 나눌 수 있는 곳이 있습니다.\\n\\n\"\n" +
                        "            + \"자살예방 상담전화 109 (24시간)\\n\"\n" +
                        "            + \"위급한 상황이라면 119에 연락해 주세요.\",\n" +
                        "            \"도움이 필요하시다면\",\n" +
                        "            JOptionPane.WARNING_MESSAGE);");
    }
    private void showQuestion() {
        questionCardLabel1.setText("질문 " + (currentIndex + 1));
        questionTextArea.setText(questions[currentIndex]);
        progressChip.setText((currentIndex + 1) + " / 9");
        renderOptions();
    }
    private String interpret(int total) {
        if (total <= 4) return "정상";
        if (total <= 9) return "경증";
        if (total <= 14) return "중등도";
        if (total <= 19) return "중등도 - 심함";
        return "심함";

    }
}
