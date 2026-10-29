package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.MedicationLogDAO;
import mindsync.dao.PrescriptionDAO;
import mindsync.dao.SelfDiagnosisDAO;
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

// 시안 03 대시보드: 인사 + 지표 카드 3개 + 기분 추이 + 오늘의 복약
public class DashboardPanel extends JPanel {
    private final PatientDashboardFrame frame;
    private final String patientId;
    private final LocalDate today = LocalDate.now();

    public DashboardPanel(PatientDashboardFrame frame, String patientId, String patientName) {
        super(new BorderLayout(0, 18));
        this.frame = frame;
        this.patientId = patientId;
        setOpaque(false);

        // ----- 데이터 -----
        List<SelfDiagnosisResult> phq = new ArrayList<SelfDiagnosisResult>();
        for (SelfDiagnosisResult r : new SelfDiagnosisDAO().getLogsByPatientId(patientId)) {
            if ("PHQ-9".equals(r.getTestType())) phq.add(r);
        }
        Collections.sort(phq, new Comparator<SelfDiagnosisResult>() {
            public int compare(SelfDiagnosisResult a, SelfDiagnosisResult b) {
                return a.getTestDate().compareTo(b.getTestDate());
            }
        });
        SelfDiagnosisResult latest = phq.isEmpty() ? null : phq.get(phq.size() - 1);
        Appointment next = new AppointmentDAO().getNextAppointmentByPatientId(patientId);
        int adherence = new MedicationLogDAO().getAdherencePercent(patientId);

        // ----- 위쪽: 인사 + 지표 -----
        Stack top = new Stack(18);
        top.add(buildGreeting(patientName, latest, next));
        top.add(buildKpis(latest, adherence, next));
        add(top, BorderLayout.NORTH);

        // ----- 가운데: 기분 추이 -----
        add(buildMoodCard(phq), BorderLayout.CENTER);

        // ----- 아래: 오늘의 복약 -----
        add(buildMedicationBanners(), BorderLayout.SOUTH);
    }

    // ---------- 인사 ----------

    private JComponent buildGreeting(String name, SelfDiagnosisResult latest, Appointment next) {
        Stack box = new Stack(4);
        box.add(Theme.label("안녕하세요, " + name + "님", Theme.bold(20), Theme.TEXT));

        JPanel sub = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sub.setOpaque(false);
        Font reg = Theme.regular(14);
        boolean doneToday = latest != null && today.toString().equals(latest.getTestDate());
        if (!doneToday) {
            sub.add(Theme.label("오늘의 ", reg, Theme.GRAY));
            sub.add(link("자가진단", new Runnable() {
                public void run() {
                    frame.openSelfDiagnosis();
                }
            }));
            sub.add(Theme.label("이 아직 남아 있어요.", reg, Theme.GRAY));
        } else if (next == null) {
            sub.add(Theme.label("다음 진료를 ", reg, Theme.GRAY));
            sub.add(link("예약", new Runnable() {
                public void run() {
                    frame.showSearch();
                }
            }));
            sub.add(Theme.label("해 볼까요?", reg, Theme.GRAY));
        } else {
            sub.add(Theme.label("오늘도 마음을 돌봐 주셔서 고마워요.", reg, Theme.GRAY));
        }
        box.add(sub);
        return box;
    }

    private JLabel link(String text, final Runnable action) {
        JLabel l = Theme.label(text, Theme.bold(14), Theme.GREEN);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return l;
    }

    // ---------- 지표 카드 ----------

    private JComponent buildKpis(SelfDiagnosisResult latest, int adherence, Appointment next) {
        JPanel row = new JPanel(new GridLayout(1, 3, 12, 0));
        row.setOpaque(false);

        String phqValue = latest == null ? "—" : latest.getTotalScore() + "점";
        String phqCaption = latest == null ? "기록 없음" : latest.getInterpretation();
        row.add(kpi("PHQ-9", phqValue, phqCaption, latest != null, 1));

        String adhValue = adherence < 0 ? "—" : adherence + "%";
        String adhCaption = adherence < 0 ? "처방 없음" : "최근 30일";
        row.add(kpi("복약 순응도", adhValue, adhCaption, false, 0));

        String nextValue = "—";
        String nextCaption = "예약 없음";
        if (next != null && next.getScheduledAt() != null && next.getScheduledAt().contains(" ")) {
            String[] p = next.getScheduledAt().split(" ");
            nextValue = Theme.shortDate(p[0]);
            nextCaption = Theme.clock(p[1]);
        }
        row.add(kpi("다음 진료", nextValue, nextCaption, false, 2));
        return row;
    }

    private JComponent kpi(String label, String value, String caption, boolean captionGreen, final int tab) {
        Card card = new Card(12, Color.WHITE, Theme.LINE).pad(16, 16, 16, 16);
        Stack s = new Stack(2);
        s.add(Theme.label(label, Theme.regular(12), Theme.GRAY_LIGHT));
        s.add(Theme.label(value, Theme.bold(22), Theme.TEXT));
        s.add(Theme.label(caption, captionGreen ? Theme.bold(12) : Theme.regular(12),
                captionGreen ? Theme.GREEN : Theme.GRAY_LIGHT));
        card.add(s, BorderLayout.CENTER);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.showMyPage(tab);
            }
        });
        return card;
    }

    // ---------- 기분 추이 ----------

    private JComponent buildMoodCard(List<SelfDiagnosisResult> phq) {
        Card card = new Card(12, Color.WHITE, Theme.LINE).pad(18, 18, 18, 18);
        JPanel inner = new JPanel(new BorderLayout(0, 14));
        inner.setOpaque(false);

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.add(Theme.label("기분 추이", Theme.bold(15), Theme.TEXT), BorderLayout.WEST);
        head.add(Theme.label("최근 8주 · PHQ-9 기준", Theme.regular(12), Theme.GRAY_LIGHT), BorderLayout.EAST);
        inner.add(head, BorderLayout.NORTH);

        if (phq.isEmpty()) {
            JPanel empty = new JPanel(new GridBagLayout());
            empty.setOpaque(false);
            Stack s = new Stack(12);
            JLabel msg = Theme.label("아직 기록이 없습니다", Theme.regular(15), Theme.GRAY);
            msg.setHorizontalAlignment(SwingConstants.CENTER);
            s.add(msg);
            Btn start = new Btn("자가진단 시작하기", 14, 12, 22, 10);
            start.onClick(new Runnable() {
                public void run() {
                    frame.openSelfDiagnosis();
                }
            });
            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            wrap.setOpaque(false);
            wrap.add(start);
            s.add(wrap);
            empty.add(s);
            inner.add(empty, BorderLayout.CENTER);
        } else {
            inner.add(buildChart(phq), BorderLayout.CENTER);
        }
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildChart(List<SelfDiagnosisResult> phq) {
        final int weeks = 8;
        final double[] values = new double[weeks];
        final String[] tips = new String[weeks];
        int lastWithData = -1;
        for (int i = 0; i < weeks; i++) {
            LocalDate end = today.minusDays(7L * (weeks - 1 - i));
            LocalDate start = end.minusDays(6);
            values[i] = -1;
            String range = Theme.shortDate(start.toString()) + " ~ " + Theme.shortDate(end.toString());
            tips[i] = range + " · 기록 없음";
            for (SelfDiagnosisResult r : phq) {
                try {
                    LocalDate d = LocalDate.parse(r.getTestDate());
                    if (!d.isBefore(start) && !d.isAfter(end)) {
                        values[i] = (27.0 - r.getTotalScore()) / 27.0;
                        tips[i] = range + " · PHQ-9 " + r.getTotalScore() + "점 (" + r.getInterpretation() + ")";
                    }
                } catch (Exception ignore) {
                }
            }
            if (values[i] >= 0) lastWithData = i;
        }
        final int highlight = lastWithData;

        return new JPanel() {
            {
                setOpaque(false);
                ToolTipManager.sharedInstance().registerComponent(this);
            }

            private int barWidth() {
                return (getWidth() - 9 * (weeks - 1)) / weeks;
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int bw = barWidth();
                for (int i = 0; i < weeks; i++) {
                    double v = values[i] < 0 ? 0.05 : Math.max(values[i], 0.05);
                    int h = (int) Math.round(getHeight() * v);
                    int x = i * (bw + 9);
                    g2.setColor(i == highlight ? Theme.GREEN : (values[i] < 0 ? Theme.LINE_SOFT : Theme.GREEN_LIGHT));
                    g2.fillRoundRect(x, getHeight() - h, bw, h, 10, 10);
                }
                g2.dispose();
            }

            @Override
            public String getToolTipText(MouseEvent e) {
                int bw = barWidth();
                if (bw <= 0) return null;
                int i = e.getX() / (bw + 9);
                return (i >= 0 && i < weeks) ? tips[i] : null;
            }
        };
    }

    // ---------- 오늘의 복약 ----------

    private JComponent buildMedicationBanners() {
        Stack box = new Stack(10);
        final MedicationLogDAO logDAO = new MedicationLogDAO();
        final String todayStr = today.toString();

        List<Prescription> active = new ArrayList<Prescription>();
        for (Prescription p : new PrescriptionDAO().getPrescriptionsByPatientId(patientId)) {
            if (MyPagePanel.isActive(p)) active.add(p);
        }

        if (active.isEmpty()) {
            Card c = new Card(12, Color.WHITE, Theme.LINE).pad(14, 18, 14, 18);
            c.add(Theme.label("처방받은 약이 아직 없어요", Theme.regular(14), Theme.TEXT2), BorderLayout.WEST);
            box.add(c);
            return box;
        }
        for (final Prescription p : active) {
            Card c = new Card(12, Color.WHITE, Theme.LINE).pad(14, 18, 14, 18);
            String dosage = p.getDosage() == null ? "" : " " + p.getDosage();
            c.add(Theme.label("오늘 · " + p.getMedicationName() + dosage, Theme.regular(14), Theme.TEXT2), BorderLayout.WEST);

            Btn b = new Btn("복용 체크", 13, 9, 14, 8);
            if (logDAO.isTakenOn(p.getId(), todayStr)) {
                b.setText("복용 완료");
                b.setStyle(Btn.Style.DONE);
            } else {
                b.onClick(new Runnable() {
                    public void run() {
                        if (logDAO.createLog(patientId, p.getId(), todayStr, true, "")) {
                            frame.showDashboard(); // 순응도 지표까지 다시 계산해서 보여준다
                        } else {
                            JOptionPane.showMessageDialog(DashboardPanel.this, "복용 기록을 저장하지 못했어요.");
                        }
                    }
                });
            }
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            right.add(b);
            c.add(right, BorderLayout.EAST);
            box.add(c);
        }
        return box;
    }
}
