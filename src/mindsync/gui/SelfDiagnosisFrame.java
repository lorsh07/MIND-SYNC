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

        mainCard.add(headerRow);
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
}