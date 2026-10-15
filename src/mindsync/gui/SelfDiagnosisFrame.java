package mindsync.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SelfDiagnosisFrame extends JFrame {

    public SelfDiagnosisFrame(String patientId) {
        setTitle("MIND SYNC - 자가진단");
        setSize(700, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        RoundedPanel mainCard = new RoundedPanel(14);
        mainCard.setLayout(new BoxLayout(mainCard, BoxLayout.Y_AXIS));
        mainCard.setBackground(Color.white);
        mainCard.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JPanel headerRow = new JPanel();
        headerRow.setLayout(new BorderLayout());
        headerRow.setBackground(Color.white);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));

        JPanel titleGroup =  new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setBackground(Color.white);

        JLabel questionTitleLabel = new JLabel("우울 척도 자가진단");
        JLabel questionSubtitleLabel = new JLabel("PHQ-9 · 총 9문항 · 약 3분");
        questionTitleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf",20));
        questionSubtitleLabel.setFont(loadFont("fonts/Pretendard-Regular.otf",14));
        questionTitleLabel.setForeground(new Color(0x16, 0x21, 0x1E));
        questionSubtitleLabel.setForeground(new Color(0x6B, 0x7A, 0x76));

        titleGroup.add(questionTitleLabel);
        titleGroup.add(questionSubtitleLabel);
        headerRow.add(titleGroup,BorderLayout.WEST);

        JLabel progressChip = new JLabel("4 / 9");
        progressChip.setFont(loadFont("fonts/Pretendard-Bold.otf",13));
        progressChip.setForeground(new Color(21,100,90));
        progressChip.setOpaque(true);
        progressChip.setBackground(new Color(0xD9, 0xE7, 0xE2));
        progressChip.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        headerRow.add(progressChip,BorderLayout.EAST);

        RoundedPanel progressTrack = new RoundedPanel(3);
        RoundedPanel progressFill = new RoundedPanel(3);
        progressFill.setBackground(new Color(0x15, 0x64, 0x5A));
        progressTrack.setBackground(new Color(0xEE, 0xF2, 0xF1));
        progressTrack.setLayout(new BorderLayout());
        progressTrack.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressTrack.setPreferredSize(new Dimension(100,6));
        progressTrack.setMaximumSize(new Dimension(Integer.MAX_VALUE,6));
        progressFill.setPreferredSize(new Dimension(200,6));
        progressTrack.add(progressFill,BorderLayout.WEST);


        RoundedPanel questionCard = new RoundedPanel(12);
        questionCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
        questionCard.setBackground(new Color(0xF7, 0xF9, 0xF8));
        questionCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE3,0xE8,0xE6), 1),
                BorderFactory.createEmptyBorder(22,22,22,22)
        ));
        JLabel questionCardLabel1 = new JLabel("질문 4");
        questionCardLabel1.setFont(loadFont("fonts/Pretendard-Bold.otf",13));
        questionCardLabel1.setForeground(new Color(0x8A, 0x99, 0x95));

        JLabel questionCardLabel2 = new JLabel("지난 2주간, 피곤하다고 느끼거나 기운이 없었던 날이 얼마나 되나요?");
        questionCardLabel2.setFont(loadFont("fonts/Pretendard-Bold.otf",19));
        questionCardLabel2.setForeground(new Color(0x16, 0x21, 0x1E));

        questionCardLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionCardLabel1.setMaximumSize(new Dimension(Integer.MAX_VALUE, questionCardLabel1.getPreferredSize().height));
        questionCardLabel1.setHorizontalAlignment(SwingConstants.LEFT);

        questionCardLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionCardLabel2.setMaximumSize(new Dimension(Integer.MAX_VALUE, questionCardLabel2.getPreferredSize().height));
        questionCardLabel2.setHorizontalAlignment(SwingConstants.LEFT);

        mainCard.add(headerRow);
        mainCard.add(Box.createVerticalStrut(18));

        mainCard.add(progressTrack);
        mainCard.add(Box.createVerticalStrut(18));

        questionCard.add(questionCardLabel1);
        questionCard.add(Box.createVerticalStrut(16));
        questionCard.add(questionCardLabel2);

        questionCard.add(Box.createVerticalStrut(16));
        questionCard.add(createOption("전혀 없었다", false));
        questionCard.add(Box.createVerticalStrut(10));
        questionCard.add(createOption("며칠 정도", false));
        questionCard.add(Box.createVerticalStrut(10));
        questionCard.add(createOption("일주일 이상", true));
        questionCard.add(Box.createVerticalStrut(10));
        questionCard.add(createOption("거의 매일", false));

        mainCard.add(questionCard);
        mainCard.add(Box.createVerticalStrut(18));
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
        option.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
        JLabel label = new JLabel(text);

        if (selected) {
            option.setBackground(new Color(0x15, 0x64, 0x5A));
            option.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x15, 0x64, 0x5A),1),
                    BorderFactory.createEmptyBorder(14,16,14,16)


            ));
            label.setForeground(Color.white);
            label.setFont(loadFont("fonts/Pretendard-Bold.otf",15));
        } else {
            option.setBackground(Color.white);
            option.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xDC, 0xE3, 0xE1),1),
                    BorderFactory.createEmptyBorder(14,16,14,16)

            ));
            label.setForeground(new Color(0x4C, 0x5A, 0x56));
            label.setFont(loadFont("fonts/Pretendard-Regular.otf",15));
        }
        option.add(label,BorderLayout.WEST);
        return option;
    }
}