package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.PrescriptionDAO;
import mindsync.dao.UserDAO;
import mindsync.model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

// 의사 화면: 예약 요청 확인 → Zoom 링크로 확정 → (영상 진료) 처방 등록 / (텍스트 상담) 상담 완료
public class DoctorDashboardFrame extends JFrame {

    private static final Color BG = new Color(0xF7, 0xF9, 0xF8);
    private static final Color GREEN = new Color(0x15, 0x64, 0x5A);
    private static final Color LIGHT_GREEN = new Color(0xD9, 0xE7, 0xE2);
    private static final Color TEXT = new Color(0x16, 0x21, 0x1E);
    private static final Color GRAY = new Color(0x6B, 0x7A, 0x76);
    private static final Color LIGHT_GRAY = new Color(0x8A, 0x99, 0x95);
    private static final Color LINE = new Color(0xDC, 0xE3, 0xE1);

    private String doctorId;
    private String doctorName;
    private boolean showConfirmed = false;   // false: 예약 요청 탭, true: 확정된 진료 탭

    private JPanel list;
    private JLabel subGreetingLabel;
    private JLabel requestTab;
    private JLabel confirmedTab;

    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private UserDAO userDAO = new UserDAO();

    public DoctorDashboardFrame(String doctorId, String doctorName) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;

        setTitle("MIND SYNC - 의사 화면");
        setSize(1000, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(32, 32, 24, 32));

        // ── 위쪽: 인사말 + 로그아웃 ──
        JLabel greetingLabel = new JLabel(doctorName + " 선생님");
        greetingLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 22));
        greetingLabel.setForeground(TEXT);

        subGreetingLabel = new JLabel(" ");
        subGreetingLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        subGreetingLabel.setForeground(GRAY);

        JPanel greetingBox = new JPanel();
        greetingBox.setLayout(new BoxLayout(greetingBox, BoxLayout.Y_AXIS));
        greetingBox.setBackground(BG);
        greetingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subGreetingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        greetingBox.add(greetingLabel);
        greetingBox.add(Box.createVerticalStrut(2));
        greetingBox.add(subGreetingLabel);

        JLabel logoutLabel = AppNavigator.createTextButton("로그아웃", loadFont("fonts/Pretendard-Regular.otf", 13));
        logoutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AppNavigator.logout();
            }
        });

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(BG);
        headerRow.add(greetingBox, BorderLayout.WEST);
        headerRow.add(logoutLabel, BorderLayout.EAST);

        // ── 탭 ──
        requestTab = createTab();
        confirmedTab = createTab();
        requestTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showConfirmed = false;
                render();
            }
        });
        confirmedTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showConfirmed = true;
                render();
            }
        });

        JPanel tabRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tabRow.setBackground(BG);
        tabRow.add(requestTab);
        tabRow.add(confirmedTab);

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(BG);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(headerRow);
        topSection.add(Box.createVerticalStrut(18));
        topSection.add(tabRow);
        topSection.add(Box.createVerticalStrut(14));

        // ── 목록 ──
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

    // 탭 라벨 하나를 만든다 (글자와 선택 상태는 render()에서 정한다)
    private JLabel createTab() {
        JLabel tab = new JLabel(" ");
        tab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return tab;
    }

    private void styleTab(JLabel tab, String text, boolean selected) {
        tab.setText(text);
        if (selected) {
            tab.setFont(loadFont("fonts/Pretendard-Bold.otf", 14));
            tab.setForeground(GREEN);
            tab.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, GREEN),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        } else {
            tab.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
            tab.setForeground(LIGHT_GRAY);
            tab.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, BG),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        }
    }

    // DB에서 다시 읽어서 목록 전체를 새로 그린다
    private void render() {
        List<Appointment> requests = appointmentDAO.getRequestsByDoctorId(doctorId);
        List<Appointment> confirmed = appointmentDAO.getAppointmentsByDoctorIdAndStatus(doctorId, "확정");

        styleTab(requestTab, "예약 요청 " + requests.size(), !showConfirmed);
        styleTab(confirmedTab, "확정된 진료 " + confirmed.size(), showConfirmed);

        if (requests.isEmpty()) {
            subGreetingLabel.setText("확정을 기다리는 예약이 없어요");
        } else {
            subGreetingLabel.setText("확정을 기다리는 예약이 " + requests.size() + "건 있어요");
        }

        List<Appointment> shown = showConfirmed ? confirmed : requests;

        list.removeAll();

        if (shown.isEmpty()) {
            String message = showConfirmed ? "확정된 진료가 없어요" : "대기 중인 예약이 없어요";
            JLabel emptyLabel = new JLabel(message);
            emptyLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 15));
            emptyLabel.setForeground(LIGHT_GRAY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(24, 4, 0, 0));
            list.add(emptyLabel);
        } else {
            for (Appointment a : shown) {
                RoundedPanel card = createCard(a, showConfirmed);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                list.add(card);
                list.add(Box.createVerticalStrut(12));
            }
        }

        list.revalidate();
        list.repaint();
    }

    // 예약 카드 하나: 시간 + 방식 칩 / 환자 / 증상 / (링크) / 버튼
    private RoundedPanel createCard(Appointment a, boolean confirmed) {
        String patientName = userDAO.getNameByUserId(a.getPatientId());
        if (patientName == null) {
            patientName = "알 수 없음";
        }

        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // 위: 시간(왼쪽) + 방식 칩(오른쪽)
        String time = a.getScheduledAt();
        if (time == null || time.trim().isEmpty()) {
            time = "시간 미정";
        }
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
        timeLabel.setForeground(TEXT);

        JLabel typeChip = createChip(a.getConsultationType() == null ? "-" : a.getConsultationType());
        JPanel chipBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        chipBox.setBackground(Color.WHITE);
        chipBox.add(typeChip);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(Color.WHITE);
        topRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        topRow.add(timeLabel, BorderLayout.WEST);
        topRow.add(chipBox, BorderLayout.EAST);

        // 가운데: 환자, 증상, (링크)
        JLabel patientLabel = new JLabel("환자 · " + patientName);
        patientLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        patientLabel.setForeground(GRAY);
        patientLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String symptom = a.getSymptom();
        if (symptom == null || symptom.trim().isEmpty()) {
            symptom = "증상 입력 없음";
        }
        JTextArea symptomText = new JTextArea(symptom);
        symptomText.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        symptomText.setForeground(new Color(0x4C, 0x5A, 0x56));
        symptomText.setBackground(Color.WHITE);
        symptomText.setLineWrap(true);
        symptomText.setWrapStyleWord(true);
        symptomText.setEditable(false);
        symptomText.setFocusable(false);
        symptomText.setBorder(null);
        symptomText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel centerBox = new JPanel();
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));
        centerBox.setBackground(Color.WHITE);
        centerBox.add(patientLabel);
        centerBox.add(Box.createVerticalStrut(6));
        centerBox.add(symptomText);

        if (confirmed && a.getMeetingLink() != null && !a.getMeetingLink().isEmpty()) {
            JLabel linkLabel = new JLabel("Zoom 링크 · " + a.getMeetingLink());
            linkLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
            linkLabel.setForeground(GREEN);
            linkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerBox.add(Box.createVerticalStrut(8));
            centerBox.add(linkLabel);
        }

        // 아래: 버튼 (오른쪽)
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        if (!confirmed) {
            JLabel confirmButton = createButton("확정");
            confirmButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    confirmRequest(a);
                }
            });
            buttonRow.add(confirmButton);
        } else if ("영상".equals(a.getConsultationType())) {
            JLabel prescribeButton = createButton("처방 등록");
            String finalPatientName = patientName;
            prescribeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPrescriptionDialog(a, finalPatientName);
                }
            });
            buttonRow.add(prescribeButton);
        } else {
            // 텍스트 상담은 처방 불가 → 상담 완료만 가능
            JLabel completeButton = createButton("상담 완료");
            completeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    completeTextConsultation(a);
                }
            });
            buttonRow.add(completeButton);
        }

        card.add(topRow, BorderLayout.NORTH);
        card.add(centerBox, BorderLayout.CENTER);
        card.add(buttonRow, BorderLayout.SOUTH);
        return card;
    }

    private JLabel createChip(String text) {
        JLabel chip = new JLabel(text);
        chip.setFont(loadFont("fonts/Pretendard-Bold.otf", 12));
        chip.setForeground(GREEN);
        chip.setBackground(LIGHT_GREEN);
        chip.setOpaque(true);
        chip.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return chip;
    }

    private JLabel createButton(String text) {
        JLabel button = new JLabel(text);
        button.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
        button.setForeground(Color.WHITE);
        button.setBackground(GREEN);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // 확정: Zoom 링크를 입력받아 상태를 '확정'으로 바꾼다
    private void confirmRequest(Appointment a) {
        String link = JOptionPane.showInputDialog(DoctorDashboardFrame.this, "Zoom 링크를 입력해주세요");
        if (link == null || link.trim().isEmpty()) {
            return;
        }
        if (appointmentDAO.confirmAppointment(a.getId(), link.trim())) {
            JOptionPane.showMessageDialog(DoctorDashboardFrame.this, "진료가 확정되었어요");
            render();
        } else {
            JOptionPane.showMessageDialog(DoctorDashboardFrame.this, "확정에 실패했어요. 다시 시도해주세요");
        }
    }

    // 텍스트 상담은 처방 없이 완료 처리한다
    private void completeTextConsultation(Appointment a) {
        int answer = JOptionPane.showConfirmDialog(DoctorDashboardFrame.this,
                "상담을 완료 처리할까요? (텍스트 상담은 처방이 불가해요)", "상담 완료", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        appointmentDAO.completeAppointment(a.getId(), "텍스트 상담 완료");
        render();
    }

    // 처방 등록 창 (영상 진료에서만 열 수 있다)
    private void showPrescriptionDialog(Appointment a, String patientName) {
        JDialog dialog = new JDialog(this, "처방 등록", true);
        dialog.setSize(480, 520);
        dialog.setLocationRelativeTo(this);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel titleLabel = new JLabel("처방 등록");
        titleLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 20));
        titleLabel.setForeground(TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel patientLabel = new JLabel("환자 · " + patientName);
        patientLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        patientLabel.setForeground(GRAY);
        patientLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = createField();
        JTextField dosageField = createField();

        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        instructionsArea.setForeground(TEXT);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JScrollPane instructionsScroll = new JScrollPane(instructionsArea);
        instructionsScroll.setBorder(BorderFactory.createLineBorder(LINE, 1));
        instructionsScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructionsScroll.setPreferredSize(new Dimension(100, 90));
        instructionsScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel registerButton = createButton("처방 등록");
        registerButton.setHorizontalAlignment(SwingConstants.CENTER);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String medicationName = nameField.getText().trim();
                String dosage = dosageField.getText().trim();
                String instructions = instructionsArea.getText().trim();

                if (medicationName.isEmpty() || dosage.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "약 이름과 용량·용법을 입력해주세요");
                    return;
                }

                boolean ok = new PrescriptionDAO().createPrescription(
                        a.getId(), medicationName, dosage, instructions, LocalDate.now().toString());
                if (!ok) {
                    JOptionPane.showMessageDialog(dialog, "처방 등록에 실패했어요. 다시 시도해주세요");
                    return;
                }
                appointmentDAO.completeAppointment(a.getId(), "처방: " + medicationName);
                JOptionPane.showMessageDialog(dialog, "처방이 등록되었어요");
                dialog.dispose();
                render();
            }
        });

        body.add(titleLabel);
        body.add(Box.createVerticalStrut(4));
        body.add(patientLabel);
        body.add(Box.createVerticalStrut(18));
        body.add(createFieldLabel("약 이름"));
        body.add(Box.createVerticalStrut(6));
        body.add(nameField);
        body.add(Box.createVerticalStrut(14));
        body.add(createFieldLabel("용량·용법 (예: 1일 1회, 아침 식후)"));
        body.add(Box.createVerticalStrut(6));
        body.add(dosageField);
        body.add(Box.createVerticalStrut(14));
        body.add(createFieldLabel("복용 시 주의사항"));
        body.add(Box.createVerticalStrut(6));
        body.add(instructionsScroll);
        body.add(Box.createVerticalStrut(22));
        body.add(registerButton);

        dialog.add(body);
        dialog.setVisible(true);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(loadFont("fonts/Pretendard-Bold.otf", 13));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        field.setForeground(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return field;
    }

    private Font loadFont(String path, float size) {
        return AppNavigator.loadFont(path, size);
    }
}
