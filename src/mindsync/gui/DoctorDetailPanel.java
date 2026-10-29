package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

// 시안 05 의료진 상세: 프로필 + 예약 가능 시간 선택 + 예약
public class DoctorDetailPanel extends JPanel {
    static final String[] SLOTS = {"09:30", "10:30", "11:00", "13:00", "15:00", "16:00", "17:30", "18:30"};
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN);

    private final PatientDashboardFrame frame;
    private final String patientId;
    private final Doctor doctor;
    private final AppointmentDAO dao = new AppointmentDAO();

    private LocalDate date = LocalDate.now();
    private String selectedTime = null;
    private String type = "영상"; // DB에는 "영상" / "텍스트"로 저장

    private JLabel dateLabel;
    private JPanel slotGrid;
    private JLabel summary;
    private Btn bookBtn;
    private Card typeVideo;
    private Card typeText;
    private Card warnBanner;
    private JLabel warnLabel;
    private JTextField symptomField;

    // ---------- 예약 가능 여부 계산 (검색 화면에서도 사용) ----------

    static boolean isOpen(String date, String time, Set<String> booked) {
        if (booked.contains(time)) return false;
        LocalDate d = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        if (d.isBefore(today)) return false;
        if (d.isEqual(today)) return LocalTime.parse(time).isAfter(LocalTime.now());
        return true;
    }

    static boolean hasOpenSlot(String date, Set<String> booked) {
        for (String s : SLOTS) {
            if (isOpen(date, s, booked)) return true;
        }
        return false;
    }

    public DoctorDetailPanel(PatientDashboardFrame frame, String patientId, Doctor doctor) {
        super(new BorderLayout());
        this.frame = frame;
        this.patientId = patientId;
        this.doctor = doctor;

        Card panel = new Card(14, Color.WHITE, Theme.LINE).pad(24, 24, 24, 24);
        Stack s = new Stack(18);
        s.add(buildBreadcrumb());
        s.add(buildProfile());
        s.add(buildIntro());
        s.add(divider());
        s.addFill(buildBooking());
        s.add(buildBar());
        panel.add(s, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        renderSlots();
        refreshBar();
    }

    // ---------- 위쪽 ----------

    private JComponent buildBreadcrumb() {
        JPanel row = Theme.row(4);
        row.setOpaque(false);
        final JLabel back = Theme.label("의료진 찾기", Theme.regular(13), Theme.GRAY_LIGHT);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                back.setForeground(Theme.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                back.setForeground(Theme.GRAY_LIGHT);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                frame.showSearch();
            }
        });
        row.add(back);
        row.add(Theme.label("›  " + doctor.getName() + " 전문의", Theme.regular(13), Theme.GRAY_LIGHT));
        return row;
    }

    private JComponent buildProfile() {
        JPanel row = new JPanel(new BorderLayout(18, 0));
        row.setOpaque(false);
        row.add(DoctorSearchPanel.avatar(doctor.getName(), 76, 28), BorderLayout.WEST);

        Stack body = new Stack(5);
        body.add(Theme.label(doctor.getName() + " 정신건강의학과 전문의", Theme.bold(22), Theme.TEXT));
        body.add(Theme.label(nz(doctor.getHospitalRegion()) + " · " + nz(doctor.getSpecialty()), Theme.regular(14), Theme.GRAY));

        JPanel tags = Theme.row(7);
        tags.setOpaque(false);
        String sp = nz(doctor.getSpecialty());
        for (String t : sp.split("[,·/、]")) {
            t = t.trim();
            if (!t.isEmpty()) tags.add(Card.badge(t, Theme.GREEN, Theme.GREEN_LIGHT, null, 12, true, 999, 6, 10));
        }
        tags.add(Card.badge("비대면 진료", Theme.GREEN, Theme.GREEN_LIGHT, null, 12, true, 999, 6, 10));
        body.add(tags);
        body.finish();
        row.add(body, BorderLayout.CENTER);
        return row;
    }

    private JComponent buildIntro() {
        JLabel l = new JLabel("<html><body style='width:640px'>영상 진료와 텍스트 상담 중에서 고를 수 있어요. "
                + "증상을 미리 적어 주시면 진료가 한결 수월해요.</body></html>");
        l.setFont(Theme.regular(14));
        l.setForeground(Theme.TEXT2);
        return l;
    }

    private JComponent divider() {
        JPanel d = new JPanel();
        d.setBackground(Theme.LINE_SOFT);
        d.setPreferredSize(new Dimension(1, 1));
        return d;
    }

    // ---------- 예약 가능 시간 ----------

    private JComponent buildBooking() {
        Stack s = new Stack(12);

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.add(Theme.label("예약 가능 시간", Theme.bold(15), Theme.TEXT), BorderLayout.WEST);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        nav.setOpaque(false);
        nav.add(arrow("‹", -1));
        dateLabel = Theme.label("", Theme.regular(13), Theme.GRAY_LIGHT);
        nav.add(dateLabel);
        nav.add(arrow("›", 1));
        head.add(nav, BorderLayout.EAST);
        s.add(head);

        // 이미 마감된 슬롯과 부딪혔을 때 쓰는 안내
        warnBanner = new Card(10, Theme.WARN_BG, null).pad(12, 14, 12, 14);
        warnLabel = Theme.label("", Theme.regular(13), Theme.WARN);
        warnBanner.add(warnLabel, BorderLayout.CENTER);
        warnBanner.setVisible(false);
        s.add(warnBanner);

        slotGrid = new JPanel(new GridLayout(2, 4, 10, 10));
        slotGrid.setOpaque(false);
        s.add(slotGrid);

        // 진료 방식 + 증상
        JPanel typeRow = Theme.row(8);
        typeRow.setOpaque(false);
        typeRow.add(Theme.label("진료 방식", Theme.bold(13), Theme.TEXT));
        typeVideo = typeChip("영상 진료", "영상");
        typeText = typeChip("텍스트 상담", "텍스트");
        typeRow.add(typeVideo);
        typeRow.add(typeText);
        refreshTypeChips();
        s.add(typeRow);

        final Card field = new Card(10, Theme.BG, Theme.LINE_INPUT).pad(11, 14, 11, 14);
        symptomField = new JTextField();
        symptomField.setFont(Theme.regular(14));
        symptomField.setForeground(Theme.TEXT);
        symptomField.setOpaque(false);
        symptomField.setBorder(null);
        symptomField.putClientProperty("JTextField.placeholderText", "증상을 간단히 적어 주세요 (선택)");
        symptomField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setStroke(Theme.GREEN);
                field.setFill(Color.WHITE);
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setStroke(Theme.LINE_INPUT);
                field.setFill(Theme.BG);
            }
        });
        field.add(symptomField, BorderLayout.CENTER);
        s.add(field);
        s.finish();
        return s;
    }

    private JLabel arrow(String text, final int delta) {
        final JLabel l = Theme.label(text, Theme.bold(16), Theme.GRAY_LIGHT);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LocalDate next = date.plusDays(delta);
                LocalDate today = LocalDate.now();
                if (next.isBefore(today) || next.isAfter(today.plusDays(30))) return;
                date = next;
                selectedTime = null; // 날짜를 바꾸면 선택이 초기화된다
                warnBanner.setVisible(false);
                renderSlots();
                refreshBar();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                l.setForeground(Theme.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                l.setForeground(Theme.GRAY_LIGHT);
            }
        });
        return l;
    }

    private Card typeChip(String text, final String value) {
        Card c = Card.badge(text, Theme.TEXT2, Color.WHITE, Theme.LINE_INPUT, 13, false, 999, 7, 14);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                type = value;
                refreshTypeChips();
                refreshBar();
            }
        });
        return c;
    }

    private void refreshTypeChips() {
        styleChip(typeVideo, "영상".equals(type));
        styleChip(typeText, "텍스트".equals(type));
    }

    private void styleChip(Card c, boolean on) {
        JLabel l = (JLabel) c.getComponent(0);
        c.setFill(on ? Theme.GREEN : Color.WHITE);
        c.setStroke(on ? Theme.GREEN : Theme.LINE_INPUT);
        l.setForeground(on ? Color.WHITE : Theme.TEXT2);
        l.setFont(on ? Theme.bold(13) : Theme.regular(13));
    }

    private void renderSlots() {
        dateLabel.setText(date.format(DATE_FMT));
        Set<String> booked = dao.getBookedTimes(doctor.getId(), date.toString());
        slotGrid.removeAll();
        for (String time : SLOTS) {
            slotGrid.add(slot(time, isOpen(date.toString(), time, booked)));
        }
        slotGrid.revalidate();
        slotGrid.repaint();
    }

    private Card slot(final String time, boolean open) {
        boolean selected = time.equals(selectedTime);
        final Card c;
        if (selected) {
            c = Card.badge(time, Color.WHITE, Theme.GREEN, Theme.GREEN, 14, true, 9, 12, 12);
        } else if (open) {
            c = Card.badge(time, Theme.TEXT, Color.WHITE, Theme.LINE_INPUT, 14, false, 9, 12, 12);
        } else {
            c = Card.badge(time, Theme.PLACEHOLDER, Theme.BG, Theme.LINE_INPUT, 14, false, 9, 12, 12);
        }
        if (open) {
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedTime = time;
                    warnBanner.setVisible(false);
                    renderSlots();
                    refreshBar();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!time.equals(selectedTime)) {
                        c.setStroke(Theme.GREEN);
                        c.setFill(Theme.BG);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!time.equals(selectedTime)) {
                        c.setStroke(Theme.LINE_INPUT);
                        c.setFill(Color.WHITE);
                    }
                }
            });
        }
        return c;
    }

    // ---------- 아래 예약 바 ----------

    private JComponent buildBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);
        summary = Theme.label("시간을 선택해 주세요", Theme.regular(13), Theme.GRAY);
        bar.add(summary, BorderLayout.CENTER);

        bookBtn = new Btn("예약하기", 15, 14, 26, 10);
        bookBtn.onClick(new Runnable() {
            public void run() {
                book();
            }
        });
        bar.add(bookBtn, BorderLayout.EAST);
        return bar;
    }

    private String typeName() {
        return "영상".equals(type) ? "영상 진료" : "텍스트 상담";
    }

    private void refreshBar() {
        if (selectedTime == null) {
            summary.setText("시간을 선택해 주세요");
            bookBtn.setStyle(Btn.Style.DISABLED);
        } else {
            summary.setText("선택: " + date.format(DATE_FMT) + " " + Theme.clock(selectedTime) + " · " + typeName() + " 30분");
            bookBtn.setStyle(Btn.Style.PRIMARY);
        }
    }

    // ---------- 예약 ----------

    private void book() {
        if (selectedTime == null) return;
        String symptom = symptomField.getText().trim();
        if (!confirm(symptom)) return;

        // 확정하기 직전에 다시 확인한다 (그 사이 다른 환자가 잡았을 수 있어요)
        Set<String> booked = dao.getBookedTimes(doctor.getId(), date.toString());
        if (!isOpen(date.toString(), selectedTime, booked)) {
            warnLabel.setText("방금 다른 예약이 먼저 들어와서 " + Theme.clock(selectedTime) + " 시간은 마감됐어요. 다른 시간을 골라 주세요.");
            warnBanner.setVisible(true);
            selectedTime = null;
            renderSlots();
            refreshBar();
            return;
        }

        boolean ok = dao.createAppointment(patientId, doctor.getId(),
                symptom.isEmpty() ? "증상 미입력" : symptom, type, date + " " + selectedTime);
        if (ok) {
            JOptionPane.showMessageDialog(this, "예약이 접수됐어요.\n의사가 확정하면 마이페이지 진료 내역에서 확인할 수 있어요.");
            frame.showDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "예약에 실패했어요. 다시 시도해 주세요.");
        }
    }

    // 예약 확인 창. "예약 확정"을 누르면 true
    private boolean confirm(String symptom) {
        final boolean[] result = {false};
        Window owner = SwingUtilities.getWindowAncestor(this);
        final JDialog dialog = new JDialog(owner, "예약 확인", Dialog.ModalityType.APPLICATION_MODAL);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        Stack s = new Stack(14);
        s.add(Theme.label("예약 내용을 확인해 주세요", Theme.bold(18), Theme.TEXT));
        s.add(infoRow("일시", date.format(DATE_FMT) + " " + Theme.clock(selectedTime)));
        s.add(infoRow("의료진", doctor.getName() + " 정신건강의학과 전문의"));
        s.add(infoRow("진료 방식", typeName() + " 30분"));
        s.add(infoRow("증상", symptom.isEmpty() ? "입력 안 함" : symptom));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        Btn cancel = new Btn("취소", 14, 12, 22, 10);
        cancel.setStyle(Btn.Style.SECONDARY);
        cancel.onClick(new Runnable() {
            public void run() {
                dialog.dispose();
            }
        });
        Btn ok = new Btn("예약 확정", 14, 12, 22, 10);
        ok.onClick(new Runnable() {
            public void run() {
                result[0] = true;
                dialog.dispose();
            }
        });
        buttons.add(cancel);
        buttons.add(ok);
        s.add(buttons);
        s.finish();
        root.add(s, BorderLayout.CENTER);

        root.setPreferredSize(new Dimension(420, root.getPreferredSize().height));
        dialog.setContentPane(root);
        dialog.getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
        return result[0];
    }

    private JComponent infoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setOpaque(false);
        JLabel l = Theme.label(label, Theme.regular(13), Theme.GRAY_LIGHT);
        l.setPreferredSize(new Dimension(64, l.getPreferredSize().height));
        row.add(l, BorderLayout.WEST);
        row.add(new JLabel("<html><body style='width:260px'>" + escape(value) + "</body></html>") {
            {
                setFont(Theme.regular(15));
                setForeground(Theme.TEXT);
            }
        }, BorderLayout.CENTER);
        return row;
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }
}
