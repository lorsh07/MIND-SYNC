package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.UserDAO;
import mindsync.model.Doctor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 시안 04 의료진 찾기: 검색 + 분야 칩 + 정렬 + 의사 목록
public class DoctorSearchPanel extends JPanel {
    private static final String[] CHIPS = {"우울", "불안", "수면", "청소년"};
    private static final String[] SORTS = {"예약 가능 순", "이름 순", "내 지역 순"};

    private final PatientDashboardFrame frame;
    private final String myRegion;
    private final List<Doctor> all;
    private final Map<String, String> nextOpen = new HashMap<String, String>(); // 의사 id -> 예약 가능한 가장 빠른 날짜
    private final Map<String, Card> chipViews = new LinkedHashMap<String, Card>();
    private final List<String> selected = new ArrayList<String>();

    private JTextField searchField;
    private JLabel countLabel;
    private JLabel sortLabel;
    private JPanel listHolder;
    private int sortIndex = 0;

    public DoctorSearchPanel(PatientDashboardFrame frame, String patientId) {
        super(new BorderLayout());
        this.frame = frame;
        String region = new UserDAO().getRegionByUserId(patientId);
        this.myRegion = region == null ? "" : region.trim();
        this.all = new UserDAO().getAllDoctors();

        AppointmentDAO dao = new AppointmentDAO();
        String today = LocalDate.now().toString();
        for (Doctor d : all) {
            for (int i = 0; i < 14; i++) {
                String date = LocalDate.now().plusDays(i).toString();
                if (DoctorDetailPanel.hasOpenSlot(date, dao.getBookedTimes(d.getId(), date))) {
                    nextOpen.put(d.getId(), date);
                    break;
                }
            }
        }

        Card panel = new Card(14, Color.WHITE, Theme.LINE).pad(24, 24, 24, 24);
        JPanel inner = new JPanel(new BorderLayout(0, 16));
        inner.setOpaque(false);
        inner.add(buildHeader(), BorderLayout.NORTH);

        listHolder = new JPanel(new BorderLayout());
        listHolder.setOpaque(false);
        JScrollPane scroll = new JScrollPane(listHolder);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        inner.add(scroll, BorderLayout.CENTER);

        panel.add(inner, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
        reload();
    }

    // ---------- 위쪽(제목, 검색, 칩, 정렬) ----------

    private JComponent buildHeader() {
        Stack head = new Stack(16);
        head.add(Theme.label("의료진 찾기", Theme.bold(20), Theme.TEXT));

        // 검색 줄
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        final Card field = new Card(10, Theme.BG, Theme.LINE_INPUT).pad(13, 16, 13, 16);
        searchField = new JTextField();
        searchField.setFont(Theme.regular(15));
        searchField.setForeground(Theme.TEXT);
        searchField.setOpaque(false);
        searchField.setBorder(null);
        searchField.putClientProperty("JTextField.placeholderText", "이름, 지역, 전문 분야 검색");
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setStroke(Theme.GREEN);
                field.setFill(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setStroke(Theme.LINE_INPUT);
                field.setFill(Theme.BG);
            }
        });
        field.add(searchField, BorderLayout.CENTER);
        row.add(field, BorderLayout.CENTER);
        Btn searchBtn = new Btn("검색", 15, 13, 22, 10);
        searchBtn.onClick(new Runnable() {
            public void run() {
                reload();
            }
        });
        row.add(searchBtn, BorderLayout.EAST);
        head.add(row);

        // 입력이 멈추고 300ms 뒤에 자동 조회
        final javax.swing.Timer debounce = new javax.swing.Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        });
        debounce.setRepeats(false);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { debounce.restart(); }
            public void removeUpdate(DocumentEvent e) { debounce.restart(); }
            public void changedUpdate(DocumentEvent e) { debounce.restart(); }
        });
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        });

        // 칩
        JPanel chips = Theme.row(8);
        chips.setOpaque(false);
        chips.add(chip("전체"));
        for (String c : CHIPS) chips.add(chip(c));
        head.add(chips);
        refreshChips();

        // 결과 개수 + 정렬
        JPanel meta = new JPanel(new BorderLayout());
        meta.setOpaque(false);
        countLabel = Theme.label("총 0명", Theme.regular(13), Theme.GRAY_LIGHT);
        meta.add(countLabel, BorderLayout.WEST);
        sortLabel = Theme.label(SORTS[0], Theme.regular(13), Theme.GRAY_LIGHT);
        sortLabel.setIcon(Theme.chevron(Theme.GRAY_LIGHT));
        sortLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        sortLabel.setIconTextGap(6);
        sortLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sortLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sortLabel.setForeground(Theme.TEXT2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sortLabel.setForeground(Theme.GRAY_LIGHT);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showSortMenu();
            }
        });
        meta.add(sortLabel, BorderLayout.EAST);
        head.add(meta);
        return head;
    }

    private Card chip(final String text) {
        final Card c = Card.badge(text, Theme.TEXT2, Color.WHITE, Theme.LINE_INPUT, 13, false, 999, 8, 14);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ("전체".equals(text)) {
                    selected.clear();
                } else if (selected.contains(text)) {
                    selected.remove(text);
                } else {
                    selected.add(text);
                }
                refreshChips();
                reload();
            }
        });
        chipViews.put(text, c);
        return c;
    }

    private void refreshChips() {
        for (Map.Entry<String, Card> e : chipViews.entrySet()) {
            boolean on = "전체".equals(e.getKey()) ? selected.isEmpty() : selected.contains(e.getKey());
            Card c = e.getValue();
            JLabel l = (JLabel) c.getComponent(0);
            c.setFill(on ? Theme.GREEN : Color.WHITE);
            c.setStroke(on ? Theme.GREEN : Theme.LINE_INPUT);
            l.setForeground(on ? Color.WHITE : Theme.TEXT2);
            l.setFont(on ? Theme.bold(13) : Theme.regular(13));
        }
    }

    private void showSortMenu() {
        JPopupMenu menu = new JPopupMenu();
        for (int i = 0; i < SORTS.length; i++) {
            final int idx = i;
            JMenuItem item = new JMenuItem(SORTS[i]);
            item.setFont(Theme.regular(13));
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortIndex = idx;
                    sortLabel.setText(SORTS[idx]);
                    reload();
                }
            });
            menu.add(item);
        }
        menu.show(sortLabel, 0, sortLabel.getHeight());
    }

    // ---------- 목록 ----------

    private void reload() {
        String q = searchField.getText().trim().toLowerCase();
        List<Doctor> shown = new ArrayList<Doctor>();
        for (Doctor d : all) {
            String hay = (nz(d.getName()) + " " + nz(d.getHospitalRegion()) + " " + nz(d.getSpecialty())).toLowerCase();
            if (!q.isEmpty() && !hay.contains(q)) continue;
            if (!selected.isEmpty()) {
                boolean any = false;
                for (String s : selected) {
                    if (nz(d.getSpecialty()).contains(s)) any = true;
                }
                if (!any) continue;
            }
            shown.add(d);
        }
        sortDoctors(shown);

        countLabel.setText("총 " + shown.size() + "명");
        listHolder.removeAll();
        if (shown.isEmpty()) {
            listHolder.add(buildEmpty(), BorderLayout.CENTER);
        } else {
            Stack list = new Stack(10);
            for (Doctor d : shown) list.add(buildItem(d));
            list.finish();
            listHolder.add(list, BorderLayout.NORTH);
        }
        listHolder.revalidate();
        listHolder.repaint();
    }

    private void sortDoctors(List<Doctor> list) {
        Collections.sort(list, new Comparator<Doctor>() {
            public int compare(Doctor a, Doctor b) {
                if (sortIndex == 0) {
                    String da = nextOpen.get(a.getId());
                    String db = nextOpen.get(b.getId());
                    if (da == null && db != null) return 1;
                    if (da != null && db == null) return -1;
                    if (da != null && db != null && !da.equals(db)) return da.compareTo(db);
                }
                if (sortIndex != 1) {
                    boolean ma = mine(a), mb = mine(b);
                    if (ma != mb) return ma ? -1 : 1;
                }
                return nz(a.getName()).compareTo(nz(b.getName()));
            }
        });
    }

    private boolean mine(Doctor d) {
        return !myRegion.isEmpty() && myRegion.equals(nz(d.getHospitalRegion()).trim());
    }

    private JComponent buildEmpty() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        Stack s = new Stack(12);
        JLabel msg = Theme.label(all.isEmpty() ? "아직 가입한 의사가 없습니다" : "조건에 맞는 의료진이 없습니다",
                Theme.regular(15), Theme.GRAY);
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        s.add(msg);
        if (!all.isEmpty()) {
            Btn reset = new Btn("필터 초기화", 14, 11, 18, 10);
            reset.setStyle(Btn.Style.SECONDARY);
            reset.onClick(new Runnable() {
                public void run() {
                    searchField.setText("");
                    selected.clear();
                    refreshChips();
                    reload();
                }
            });
            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            wrap.setOpaque(false);
            wrap.add(reset);
            s.add(wrap);
        }
        p.add(s);
        return p;
    }

    private JComponent buildItem(final Doctor d) {
        final Card item = new Card(12, Color.WHITE, Theme.LINE).pad(16, 16, 16, 16);
        item.setLayout(new BorderLayout(14, 0));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        item.add(avatar(d.getName(), 44, 16), BorderLayout.WEST);

        Stack body = new Stack(3);
        JPanel nameRow = Theme.row(8);
        nameRow.setOpaque(false);
        nameRow.add(Theme.label(d.getName() + " 정신건강의학과 전문의", Theme.bold(16), Theme.TEXT));
        if (mine(d)) nameRow.add(Card.badge("내 지역", Theme.GREEN, Theme.GREEN_LIGHT, null, 11, true, 999, 3, 8));
        body.add(nameRow);
        body.add(Theme.label(nz(d.getHospitalRegion()) + " · " + nz(d.getSpecialty()), Theme.regular(13), Theme.GRAY));
        item.add(body, BorderLayout.CENTER);

        String open = nextOpen.get(d.getId());
        Card badge;
        if (open == null) {
            badge = Card.badge("예약 마감", Theme.GRAY, Color.WHITE, Theme.LINE_INPUT, 13, false, 8, 8, 12);
        } else if (open.equals(LocalDate.now().toString())) {
            badge = Card.badge("오늘 예약 가능", Theme.GREEN, Theme.GREEN_LIGHT, null, 13, true, 8, 8, 12);
        } else {
            badge = Card.badge(Theme.shortDate(open) + " 예약", Theme.GRAY, Color.WHITE, Theme.LINE_INPUT, 13, false, 8, 8, 12);
        }
        JPanel east = new JPanel(new GridBagLayout());
        east.setOpaque(false);
        east.add(badge);
        item.add(east, BorderLayout.EAST);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setFill(Theme.BG);
                item.setStroke(Theme.LINE_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setFill(Color.WHITE);
                item.setStroke(Theme.LINE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                frame.showDetail(d);
            }
        });
        return item;
    }

    // 동그란 아바타 (사진 대신 이름 첫 글자)
    static JComponent avatar(String name, final int size, float fontSize) {
        final String ch = Theme.initial(name);
        final float fs = fontSize;
        JComponent c = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Theme.GREEN_LIGHT);
                g2.fillOval(0, 0, size, size);
                g2.setColor(Theme.GREEN);
                g2.setFont(Theme.bold(fs));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(ch, (size - fm.stringWidth(ch)) / 2, (size - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        c.setPreferredSize(new Dimension(size, size));
        c.setMinimumSize(new Dimension(size, size));
        c.setMaximumSize(new Dimension(size, size));
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.add(c);
        return wrap;
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }
}
