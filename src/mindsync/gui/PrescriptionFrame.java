package mindsync.gui;

import mindsync.dao.MedicationLogDAO;
import mindsync.dao.PrescriptionDAO;
import mindsync.model.Prescription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

// 환자 화면: 처방받은 약 목록 + 오늘 복용 체크
public class PrescriptionFrame extends JFrame {

    private static final Color BG = new Color(0xF7, 0xF9, 0xF8);
    private static final Color GREEN = new Color(0x15, 0x64, 0x5A);
    private static final Color LIGHT_GREEN = new Color(0xD9, 0xE7, 0xE2);
    private static final Color TEXT = new Color(0x16, 0x21, 0x1E);
    private static final Color GRAY = new Color(0x6B, 0x7A, 0x76);
    private static final Color LIGHT_GRAY = new Color(0x8A, 0x99, 0x95);

    private String patientId;
    private JPanel list;
    private JLabel adherenceLabel;

    private PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private MedicationLogDAO medicationLogDAO = new MedicationLogDAO();

    public PrescriptionFrame(String patientId, String patientName) {
        this.patientId = patientId;

        setTitle("MIND SYNC - 처방·투약");
        setSize(760, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(32, 32, 24, 32));

        JLabel titleLabel = new JLabel("처방·투약");
        titleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 22));
        titleLabel.setForeground(TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        adherenceLabel = new JLabel(" ");
        adherenceLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        adherenceLabel.setForeground(GRAY);
        adherenceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(BG);
        topSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        topSection.add(titleLabel);
        topSection.add(Box.createVerticalStrut(2));
        topSection.add(adherenceLabel);

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(BG);

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(BG);
        listWrapper.add(list, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        render();

        contentPanel.add(topSection, BorderLayout.NORTH);
        contentPanel.add(scroll, BorderLayout.CENTER);

        add(contentPanel);
        setVisible(true);
    }

    // 처방 목록과 순응도를 DB에서 다시 읽어 새로 그린다
    private void render() {
        List<Prescription> prescriptions = prescriptionDAO.getPrescriptionsByPatientId(patientId);

        int percent = medicationLogDAO.getAdherencePercent(patientId);
        if (percent < 0) {
            adherenceLabel.setText("아직 처방받은 약이 없어요");
        } else {
            adherenceLabel.setText("복약 순응도 " + percent + "% · 최근 30일");
        }

        list.removeAll();

        if (prescriptions.isEmpty()) {
            JLabel emptyLabel = new JLabel("<html>처방받은 약이 없어요.<br>영상 진료 후 의사가 처방을 등록하면 여기에 보여요.</html>");
            emptyLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 15));
            emptyLabel.setForeground(LIGHT_GRAY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(24, 4, 0, 0));
            list.add(emptyLabel);
        } else {
            // 최근 처방이 위로 오도록 거꾸로 돈다
            for (int i = prescriptions.size() - 1; i >= 0; i--) {
                RoundedPanel card = createCard(prescriptions.get(i));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                list.add(card);
                list.add(Box.createVerticalStrut(12));
            }
        }

        list.revalidate();
        list.repaint();
    }

    // 처방 카드 하나: 약 이름 / 용법 / 주의사항 / 처방일 / 복용 체크 버튼
    private RoundedPanel createCard(Prescription p) {
        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel nameLabel = new JLabel(p.getMedicationName());
        nameLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 16));
        nameLabel.setForeground(TEXT);

        JLabel dateLabel = new JLabel("처방일 " + p.getIssuedDate());
        dateLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 12));
        dateLabel.setForeground(LIGHT_GRAY);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(Color.WHITE);
        topRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        topRow.add(nameLabel, BorderLayout.WEST);
        topRow.add(dateLabel, BorderLayout.EAST);

        JLabel dosageLabel = new JLabel("용법 · " + p.getDosage());
        dosageLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        dosageLabel.setForeground(new Color(0x4C, 0x5A, 0x56));
        dosageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel centerBox = new JPanel();
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));
        centerBox.setBackground(Color.WHITE);
        centerBox.add(dosageLabel);

        String instructions = p.getInstructions();
        if (instructions != null && !instructions.trim().isEmpty()) {
            JTextArea instructionsText = new JTextArea("주의 · " + instructions);
            instructionsText.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
            instructionsText.setForeground(GRAY);
            instructionsText.setBackground(Color.WHITE);
            instructionsText.setLineWrap(true);
            instructionsText.setWrapStyleWord(true);
            instructionsText.setEditable(false);
            instructionsText.setFocusable(false);
            instructionsText.setBorder(null);
            instructionsText.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerBox.add(Box.createVerticalStrut(6));
            centerBox.add(instructionsText);
        }

        // 오늘 복용했는지에 따라 버튼 모양이 달라진다
        String today = LocalDate.now().toString();
        boolean takenToday = medicationLogDAO.isTakenOn(p.getId(), today);

        JLabel checkButton = new JLabel(takenToday ? "오늘 복용 완료" : "오늘 복용 체크");
        checkButton.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
        checkButton.setOpaque(true);
        checkButton.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));

        if (takenToday) {
            checkButton.setForeground(GREEN);
            checkButton.setBackground(LIGHT_GREEN);
        } else {
            checkButton.setForeground(Color.WHITE);
            checkButton.setBackground(GREEN);
            checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            checkButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    medicationLogDAO.createLog(patientId, p.getId(), today, true, "");
                    render();
                }
            });
        }

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        buttonRow.add(checkButton);

        card.add(topRow, BorderLayout.NORTH);
        card.add(centerBox, BorderLayout.CENTER);
        card.add(buttonRow, BorderLayout.SOUTH);
        return card;
    }

    private Font loadFont(String path, float size) {
        return AppNavigator.loadFont(path, size);
    }
}
