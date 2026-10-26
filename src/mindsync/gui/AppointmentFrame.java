package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.UserDAO;
import mindsync.model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.awt.event.*;

import static java.awt.SystemColor.text;

public class AppointmentFrame extends JFrame {
    private String patientId;
    private Doctor doctor;
    private LocalDate bookingDate;
    private String selectedSlot;
    private final String[] slotTimes = {"09:30", "10:30", "11:00", "13:00", "15:00", "16:00", "17:30", "18:30"};
    private JPanel slotGrid;
    private String consultationType = "영상";
    private JPanel modeRow;
    private JTextArea symptomArea;
    private JLabel summaryLabel;
    private RoundedPanel bookButton;
    private JLabel bookButtonLabel;

    public AppointmentFrame(String patientId) {
        this.patientId = patientId;
        setTitle("MIND SYNC - 진료 예약");
        setSize(720, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String region = new UserDAO().getRegionByUserId(patientId);
        Doctor doctor = new AppointmentDAO().findDoctorByRegion(region);
        System.out.println("지역: " + region + " / 의사: " + (doctor == null ? "없음" : doctor.getName()));


        RoundedPanel mainCard = new RoundedPanel(14);
        mainCard.setLayout(new BorderLayout());
        mainCard.setBackground(Color.WHITE);
        mainCard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(Color.WHITE);

        JPanel pathRow = new JPanel(new BorderLayout());
        pathRow.setBackground(Color.WHITE);
        pathRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pathRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));


        JLabel pathLabel = new JLabel("진료 예약 › 담당 의료진");
        pathLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        pathLabel.setForeground(new Color(0x8A, 0x99, 0x95));
        pathRow.add(pathLabel, BorderLayout.WEST);

        topSection.add(pathRow);
        topSection.add(Box.createVerticalStrut(18));


        if (doctor != null) {
            JLabel nameLabel = new JLabel(doctor.getName());
            nameLabel.setForeground(new Color(0x16, 0x21, 0x1E));
            nameLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 22));

            JLabel infoLabel = new JLabel(doctor.getHospitalRegion() + " · " + doctor.getSpecialty());
            infoLabel.setForeground(new Color(0x6B, 0x7A, 0x76));
            infoLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));

            JPanel chipRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
            chipRow.setBackground(Color.WHITE);

            JPanel doctorRow = new JPanel(new BorderLayout());
            doctorRow.setBackground(Color.WHITE);
            doctorRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));

            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            chipRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            doctorRow.setAlignmentX(Component.LEFT_ALIGNMENT);


            RoundedPanel avatar = new RoundedPanel(76);
            avatar.setBackground(new Color(0xD9, 0xE7, 0xE2));
            avatar.setPreferredSize(new Dimension(76, 76));


            JLabel chip1 = new JLabel(doctor.getSpecialty());
            chip1.setFont(loadFont("fonts/Pretendard-Bold.otf", 12));
            chip1.setForeground(new Color(0x15, 0x64, 0x5A));
            chip1.setBackground(new Color(0xD9, 0xE7, 0xE2));
            chip1.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            chip1.setOpaque(true);

            JLabel chip2 = new JLabel("비대면 진료");
            chip2.setFont(loadFont("fonts/Pretendard-Bold.otf", 12));
            chip2.setForeground(new Color(0x15, 0x64, 0x5A));
            chip2.setBackground(new Color(0xD9, 0xE7, 0xE2));
            chip2.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            chip2.setOpaque(true);


            JPanel textGroup = new JPanel();
            textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
            textGroup.setBackground(Color.WHITE);
            textGroup.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));   // 아바타와의 간격 18px


            textGroup.add(nameLabel);
            textGroup.add(infoLabel);
            textGroup.add(Box.createVerticalStrut(10));
            textGroup.add(chipRow);
            chipRow.add(chip1);
            chipRow.add(chip2);
            doctorRow.add(avatar, BorderLayout.WEST);
            doctorRow.add(textGroup, BorderLayout.CENTER);


            topSection.add(doctorRow);
        } else {
            JLabel noDoctorLabel = new JLabel("같은 지역에 배정 가능한 의사가 없습니다.");
            noDoctorLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 15));
            noDoctorLabel.setForeground(new Color(0x6B, 0x7A, 0x76));

            JPanel noDoctorRow = new JPanel(new BorderLayout());
            noDoctorRow.setBackground(Color.WHITE);
            noDoctorRow.add(noDoctorLabel, BorderLayout.WEST);
            noDoctorRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            topSection.add(noDoctorRow);
        }


        JPanel divider = new JPanel();
        divider.setBackground(new Color(0xEE, 0xF2, 0xF1));
        divider.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);

        topSection.add(Box.createVerticalStrut(18));
        topSection.add(divider);


        mainCard.add(topSection, BorderLayout.NORTH);
        add(mainCard);
        setVisible(true);


        JPanel centerSection = new JPanel();
        centerSection.setLayout(new BoxLayout(centerSection, BoxLayout.Y_AXIS));
        centerSection.setBackground(Color.WHITE);
        centerSection.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        JLabel slotTitle = new JLabel("예약 가능 시간");
        slotTitle.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
        slotTitle.setForeground(new Color(0x16, 0x21, 0x1E));

        bookingDate = LocalDate.now().plusDays(1);
        String dateText = bookingDate.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN));
        JLabel dateLabel = new JLabel(dateText);
        dateLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        dateLabel.setForeground(new Color(0x8A, 0x99, 0x95));

        JPanel slotHeader = new JPanel(new BorderLayout());
        slotHeader.setBackground(Color.WHITE);
        slotHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        slotHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        slotHeader.add(slotTitle, BorderLayout.WEST);
        slotHeader.add(dateLabel, BorderLayout.EAST);

        slotGrid = new JPanel(new GridLayout(2, 4, 10, 10));
        slotGrid.setBackground(Color.WHITE);
        slotGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        slotGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 104));


        modeRow = new JPanel(new GridLayout(1, 2, 10, 0));
        modeRow.setBackground(Color.WHITE);
        modeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        modeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));


        symptomArea = new JTextArea();
        symptomArea.setLineWrap(true);
        symptomArea.setWrapStyleWord(true);
        symptomArea.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        symptomArea.setForeground(new Color(0x16, 0x21, 0x1E));
        symptomArea.setBackground(new Color(0xF7, 0xF9, 0xF8));
        symptomArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JScrollPane symptomScroll = new JScrollPane(symptomArea);
        symptomScroll.setBorder(BorderFactory.createLineBorder(new Color(0xDC, 0xE3, 0xE1), 1));
        symptomScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        symptomScroll.setPreferredSize(new Dimension(100, 110));
        symptomScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        summaryLabel= new JLabel();
        summaryLabel.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        summaryLabel.setForeground(new Color(0x6B,0x7A,0x76));

        bookButtonLabel = new JLabel("예약하기", SwingConstants.CENTER);
        bookButtonLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));

        bookButton = new RoundedPanel(10);
        bookButton.setLayout(new BorderLayout());
        bookButton.setBorder(BorderFactory.createEmptyBorder(14, 26, 14, 26));
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.add(bookButtonLabel, BorderLayout.CENTER);

        bookButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedSlot == null) return;

                String scheduledAt = bookingDate + " " + selectedSlot;
                boolean ok = new AppointmentDAO().createAppointment(
                        patientId, doctor.getId(), symptomArea.getText(), consultationType, scheduledAt);

                if (ok) {
                    JOptionPane.showMessageDialog(AppointmentFrame.this, "예약이 접수됐어요.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AppointmentFrame.this, "예약에 실패했어요. 다시 시도해주세요.");
                }
            }
        });


        JPanel bookingBar = new JPanel();
        bookingBar.setBackground(Color.WHITE);
        bookingBar.setLayout(new BorderLayout());
        bookingBar.setBorder(BorderFactory.createEmptyBorder(18,0,0,0));


        bookingBar.add(summaryLabel, BorderLayout.WEST);
        bookingBar.add(bookButton, BorderLayout.EAST);
        mainCard.add(bookingBar, BorderLayout.SOUTH);
        updateBookingBar();

        centerSection.add(slotHeader);
        centerSection.add(Box.createVerticalStrut(12));
        centerSection.add(slotGrid);
        renderSlots();
        centerSection.add(Box.createVerticalStrut(18));    // ← 여기부터 새로 추가
        centerSection.add(createSectionTitle("진료 방식"));
        centerSection.add(Box.createVerticalStrut(10));
        centerSection.add(modeRow);
        renderModes();                                     // modeRow를 붙인 다음 줄

        centerSection.add(Box.createVerticalStrut(18));
        centerSection.add(createSectionTitle("증상"));
        centerSection.add(Box.createVerticalStrut(10));
        centerSection.add(symptomScroll);

        mainCard.add(centerSection, BorderLayout.CENTER);

        add(mainCard);
        setVisible(true);
    }
    private JPanel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
        label.setForeground(new Color(0x16, 0x21, 0x1E));

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        row.add(label, BorderLayout.WEST);
        return row;
    }

    private RoundedPanel createModeCard(String cardTitle, String cardDesc, boolean selected) {
        RoundedPanel modeCard = new RoundedPanel(10);
        modeCard.setLayout(new BorderLayout());

        if (selected) {
            modeCard.setBackground(Color.WHITE);
            modeCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x15, 0x64, 0x5A), 2),
                    BorderFactory.createEmptyBorder(13, 15, 13, 15)));

        } else {
            modeCard.setBackground(Color.WHITE);
            modeCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xDC, 0xE3, 0xE1), 1),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        }

        JLabel title = new JLabel(cardTitle);
        title.setFont(loadFont("fonts/Pretendard-Bold.otf", 15));
        title.setForeground(new Color(0x16, 0x21, 0x1E));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel desc = new JLabel(cardDesc);
        desc.setFont(loadFont("fonts/Pretendard-Regular.otf", 13));
        desc.setForeground(new Color(0x6B, 0x7A, 0x76));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setOpaque(false);
        textBox.add(title);
        textBox.add(desc);

        modeCard.add(textBox, BorderLayout.CENTER);
        return modeCard;

    }
    private void renderModes() {
        modeRow.removeAll();

        RoundedPanel videoCard = createModeCard("영상 진료", "화상통화로 진료하며 처방을 받을 수 있어요",
                consultationType.equals("영상"));
        videoCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        videoCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                consultationType = "영상";
                renderModes();
            }
        });

        RoundedPanel textCard = createModeCard("텍스트 상담", "메시지로 상담하며 처방은 받을 수 없어요",
                consultationType.equals("텍스트"));
        textCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        textCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                consultationType = "텍스트";
                renderModes();
            }
        });

        modeRow.add(videoCard);
        modeRow.add(textCard);

        modeRow.revalidate();
        modeRow.repaint();


    }
    private void updateBookingBar() {
        if (summaryLabel == null) return;   // 바를 만들기 전에 불리면 건너뛰어요

        if (selectedSlot == null) {
            summaryLabel.setText("시간을 선택해주세요");
            bookButton.setBackground(new Color(0xD9, 0xE7, 0xE2));
            bookButtonLabel.setForeground(new Color(0x8A, 0x99, 0x95));
        } else {
            String day = bookingDate.format(DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN));
            String mode = consultationType.equals("영상") ? "영상 진료" : "텍스트 상담";
            summaryLabel.setText("선택: " + day + " " + selectedSlot + " · " + mode);
            bookButton.setBackground(new Color(0x15, 0x64, 0x5A));
            bookButtonLabel.setForeground(Color.WHITE);
        }
        bookButton.repaint();
    }
    private RoundedPanel createSlot(String time, boolean selected) {
        RoundedPanel slot = new RoundedPanel(9);
        slot.setLayout(new BorderLayout());

        JLabel label = new JLabel(time);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        if (selected) {
            slot.setBackground(new Color(0x15, 0x64, 0x5A));
            slot.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x15, 0x64, 0x5A), 1),
                    BorderFactory.createEmptyBorder(12, 0, 12, 0)));
            label.setForeground(Color.WHITE);
            label.setFont(loadFont("fonts/Pretendard-Bold.otf", 14));
        } else {
            slot.setBackground(Color.WHITE);
            slot.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xDC, 0xE3, 0xE1), 1),
                    BorderFactory.createEmptyBorder(12, 0, 12, 0)));
            label.setForeground(new Color(0x16, 0x21, 0x1E));
            label.setFont(loadFont("fonts/Pretendard-Regular.otf", 14));
        }

        slot.add(label, BorderLayout.CENTER);
        return slot;
    }
    private void renderSlots() {
        slotGrid.removeAll();
        for (String time : slotTimes) {
            RoundedPanel slot = createSlot(time, time.equals(selectedSlot));
            slot.setCursor(new Cursor(Cursor.HAND_CURSOR));
            slot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedSlot = time;
                    renderSlots();
                }
            });
            slotGrid.add(slot);
        }
        slotGrid.revalidate();
        slotGrid.repaint();
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