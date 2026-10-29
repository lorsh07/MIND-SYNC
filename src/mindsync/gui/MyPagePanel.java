package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.MedicationLogDAO;
import mindsync.dao.PrescriptionDAO;
import mindsync.dao.SelfDiagnosisDAO;
import mindsync.dao.UserDAO;
import mindsync.model.Appointment;
import mindsync.model.Prescription;
import mindsync.model.SelfDiagnosisResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// 시안 06 마이페이지: 프로필 + 탭(처방·투약 / 진단 기록 / 진료 내역)
public class MyPagePanel extends JPanel {
    private static final String[] TABS = {"처방·투약", "진단 기록", "진료 내역"};

    private final PatientDashboardFrame frame;
    private final String patientId;
    private final String patientName;
    private final int tab;

    // 처방일이 30일 이내면 "복용 중", 지났으면 "종료"로 본다
    static boolean isActive(Prescription p) {
        try {
            return !LocalDate.parse(p.getIssuedDate()).isBefore(LocalDate.now().minusDays(30));
        } catch (Exception e) {
            return true;
        }
    }

    public MyPagePanel(PatientDashboardFrame frame, String patientId, String patientName, int tab) {
        super(new BorderLayout());
        this.frame = frame;
        this.patientId = patientId;
        this.patientName = patientName;
        this.tab = tab;

        Card panel = new Card(14, Color.WHITE, Theme.LINE).pad(24, 24, 24, 24);
        Stack s = new Stack(18);
        s.add(buildHeader());
        s.add(buildTabs());
        s.addFill(tab == 0 ? buildPrescriptions() : tab == 1 ? buildDiagnoses() : buildVisits());

        Card info = new Card(10, Theme.BG, null).pad(14, 16, 14, 16);
        info.add(Theme.label("진단·처방·진료 기록은 이 프로그램의 로컬 데이터베이스(mindsync.db)에 저장돼요.",
                Theme.regular(13), Theme.GRAY), BorderLayout.CENTER);
        s.add(info);

        panel.add(s, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    // ---------- 프로필 / 탭 ----------

    private JComponent buildHeader() {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.add(DoctorSearchPanel.avatar(patientName, 60, 22), BorderLayout.WEST);

        String region = new UserDAO().getRegionByUserId(patientId);
        Stack body = new Stack(3);
        body.add(Theme.label(patientName, Theme.bold(20), Theme.TEXT));
        body.add(Theme.label("환자 · " + (region == null || region.isEmpty() ? "지역 미입력" : region),
                Theme.regular(14), Theme.GRAY));
        row.add(body, BorderLayout.CENTER);
        return row;
    }

    private JComponent buildTabs() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE_SOFT));
        JPanel row = Theme.row(24);
        row.setOpaque(false);
        for (int i = 0; i < TABS.length; i++) {
            final int idx = i;
            final boolean on = i == tab;
            final JLabel l = Theme.label(TABS[i], on ? Theme.bold(15) : Theme.regular(15), on ? Theme.GREEN : Theme.GRAY);
            l.setBorder(on
                    ? BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.GREEN),
                            BorderFactory.createEmptyBorder(0, 0, 9, 0))
                    : BorderFactory.createEmptyBorder(0, 0, 11, 0));
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            l.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.showMyPage(idx);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!on) l.setForeground(Theme.TEXT2);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!on) l.setForeground(Theme.GRAY);
                }
            });
            row.add(l);
        }
        wrap.add(row, BorderLayout.WEST);
        return wrap;
    }

    // ---------- 표 만들기 ----------

    private static JComponent table(String[] headers, double[] fr, List<JComponent[]> rows,
                                    List<Runnable> clicks, String emptyMessage) {
        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(false);

        JPanel head = new JPanel(new FrLayout(fr));
        head.setOpaque(false);
        head.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE_SOFT),
                BorderFactory.createEmptyBorder(12, 4, 12, 4)));
        for (String h : headers) head.add(Theme.label(h, Theme.bold(13), Theme.GRAY_LIGHT));
        box.add(head, BorderLayout.NORTH);

        if (rows.isEmpty()) {
            JPanel empty = new JPanel(new GridBagLayout());
            empty.setOpaque(false);
            empty.add(Theme.label(emptyMessage, Theme.regular(15), Theme.GRAY));
            box.add(empty, BorderLayout.CENTER);
            return box;
        }

        Stack list = new Stack(0);
        for (int i = 0; i < rows.size(); i++) {
            JPanel r = new JPanel(new FrLayout(fr));
            r.setOpaque(false);
            boolean last = i == rows.size() - 1;
            r.setBorder(BorderFactory.createCompoundBorder(
                    last ? BorderFactory.createEmptyBorder() : BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE_ROW),
                    BorderFactory.createEmptyBorder(15, 4, 15, 4)));
            for (JComponent c : rows.get(i)) r.add(c);
            final Runnable click = clicks == null ? null : clicks.get(i);
            if (click != null) {
                r.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                r.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        click.run();
                    }
                });
            }
            list.add(r);
        }
        list.finish();
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        box.add(scroll, BorderLayout.CENTER);
        return box;
    }

    private static JLabel cell(String text, boolean strong) {
        return Theme.label(text == null ? "" : text, strong ? Theme.bold(14) : Theme.regular(14),
                strong ? Theme.TEXT : Theme.TEXT2);
    }

    private static Card status(String text, Color fg, Color bg) {
        return Card.badge(text, fg, bg, null, 12, true, 6, 5, 9);
    }

    // ---------- 처방·투약 ----------

    private JComponent buildPrescriptions() {
        List<Prescription> list = new PrescriptionDAO().getPrescriptionsByPatientId(patientId);
        Collections.sort(list, new Comparator<Prescription>() {
            public int compare(Prescription a, Prescription b) {
                return String.valueOf(b.getIssuedDate()).compareTo(String.valueOf(a.getIssuedDate()));
            }
        });
        final MedicationLogDAO logDAO = new MedicationLogDAO();
        final String today = LocalDate.now().toString();

        List<JComponent[]> rows = new ArrayList<JComponent[]>();
        for (final Prescription p : list) {
            boolean active = isActive(p);
            JComponent action;
            if (!active) {
                action = new JLabel("");
            } else if (logDAO.isTakenOn(p.getId(), today)) {
                Btn done = new Btn("복용 완료", 12, 7, 12, 8);
                done.setStyle(Btn.Style.DONE);
                action = done;
            } else {
                Btn check = new Btn("복용 체크", 12, 7, 12, 8);
                check.onClick(new Runnable() {
                    public void run() {
                        if (logDAO.createLog(patientId, p.getId(), today, true, "")) {
                            frame.showMyPage(0);
                        }
                    }
                });
                action = check;
            }
            rows.add(new JComponent[]{
                    cell(p.getMedicationName(), true),
                    cell(p.getDosage(), false),
                    cell(Theme.fullDate(p.getIssuedDate()), false),
                    active ? status("복용 중", Theme.GREEN, Theme.GREEN_LIGHT) : status("종료", Theme.GRAY, Theme.BADGE_GRAY),
                    action});
        }
        return table(new String[]{"약품", "용량 · 주기", "처방일", "상태", "오늘 복용"},
                new double[]{1.4, 1.1, 1.0, 0.7, 0.8}, rows, null, "아직 처방 기록이 없습니다");
    }

    // ---------- 진단 기록 ----------

    private JComponent buildDiagnoses() {
        List<SelfDiagnosisResult> list = new SelfDiagnosisDAO().getLogsByPatientId(patientId);
        Collections.sort(list, new Comparator<SelfDiagnosisResult>() {
            public int compare(SelfDiagnosisResult a, SelfDiagnosisResult b) {
                return b.getTestDate().compareTo(a.getTestDate());
            }
        });
        List<JComponent[]> rows = new ArrayList<JComponent[]>();
        for (SelfDiagnosisResult r : list) {
            rows.add(new JComponent[]{
                    cell(Theme.fullDate(r.getTestDate()), false),
                    cell(r.getTestType(), true),
                    cell(r.getTotalScore() + "점", false),
                    status(r.getInterpretation(), Theme.GREEN, Theme.GREEN_LIGHT)});
        }
        return table(new String[]{"날짜", "척도", "점수", "심각도"},
                new double[]{1, 1, 1, 1.2}, rows, null, "아직 진단 기록이 없습니다");
    }

    // ---------- 진료 내역 ----------

    private JComponent buildVisits() {
        List<Appointment> list = new AppointmentDAO().getAppointmentsByPatientId(patientId);
        final UserDAO userDAO = new UserDAO();
        List<JComponent[]> rows = new ArrayList<JComponent[]>();
        List<Runnable> clicks = new ArrayList<Runnable>();
        for (final Appointment a : list) {
            final String when = whenText(a.getScheduledAt());
            final String doctor = userDAO.getNameByUserId(a.getDoctorId());
            final String typeText = "영상".equals(a.getConsultationType()) ? "영상 진료" : "텍스트 상담";
            Color fg = Theme.GRAY, bg = Theme.BADGE_GRAY;
            if ("요청".equals(a.getStatus())) { fg = Theme.WARN; bg = Theme.WARN_BG; }
            if ("확정".equals(a.getStatus())) { fg = Theme.GREEN; bg = Theme.GREEN_LIGHT; }
            rows.add(new JComponent[]{
                    cell(when, false),
                    cell(doctor == null ? "-" : doctor + " 선생님", true),
                    cell(typeText, false),
                    status(a.getStatus(), fg, bg)});
            clicks.add(new Runnable() {
                public void run() {
                    showVisitDetail(a, when, doctor, typeText);
                }
            });
        }
        return table(new String[]{"일시", "의료진", "형태", "상태"},
                new double[]{1.3, 1.1, 1.0, 0.7}, rows, clicks, "아직 진료 내역이 없습니다");
    }

    private String whenText(String scheduledAt) {
        if (scheduledAt == null || scheduledAt.isEmpty()) return "일시 미정";
        String[] p = scheduledAt.split(" ");
        if (p.length == 2 && p[0].matches("\\d{4}-\\d{2}-\\d{2}")) {
            return Theme.fullDate(p[0]) + " " + Theme.clock(p[1]);
        }
        return scheduledAt;
    }

    private void showVisitDetail(Appointment a, String when, String doctor, String typeText) {
        StringBuilder sb = new StringBuilder();
        sb.append("일시: ").append(when).append("\n");
        sb.append("의료진: ").append(doctor == null ? "-" : doctor + " 선생님").append("\n");
        sb.append("진료 방식: ").append(typeText).append("\n");
        sb.append("상태: ").append(a.getStatus()).append("\n");
        sb.append("증상: ").append(a.getSymptom() == null || a.getSymptom().isEmpty() ? "-" : a.getSymptom());
        if (a.getMeetingLink() != null && !a.getMeetingLink().isEmpty()) {
            sb.append("\n진료 링크(Zoom): ").append(a.getMeetingLink());
        }
        if (a.getDoctorNotes() != null && !a.getDoctorNotes().isEmpty()) {
            sb.append("\n의사 메모: ").append(a.getDoctorNotes());
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "진료 상세", JOptionPane.PLAIN_MESSAGE);
    }
}
