package mindsync.gui;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.UserDAO;
import mindsync.model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class AppointmentFrame extends JFrame {
    private String patientId;
    private Doctor doctor;

    public AppointmentFrame(String patientId) {
        this.patientId = patientId;
        setTitle("MIND SYNC - 진료 예약");
        setSize(720, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String region = new UserDAO().getRegionByUserId(patientId);
        Doctor doctor = new AppointmentDAO().findDoctorByRegion(region);
        System.out.println("지역: " + region + " / 의사: "+ (doctor == null ? "없음" : doctor.getName()));


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
        pathLabel.setForeground(new Color(0x8A,0x99, 0x95));
        pathRow.add(pathLabel, BorderLayout.WEST);

        topSection.add(pathRow);
        topSection.add(Box.createVerticalStrut(18));


        if (doctor != null) {
            // avatar, nameLabel, infoLabel, chip1, chip2, chipRow(+칩 add),
            // textGroup(+add), doctorRow(avatar WEST, textGroup CENTER)를 전부 여기서 만들고
            JLabel nameLabel = new JLabel(doctor.getName());
            nameLabel.setForeground(new Color(0x16,0x21,0x1E));
            nameLabel.setFont(loadFont("fonts/Pretendard-Bold.otf", 22));

            JLabel infoLabel = new JLabel(doctor.getHospitalRegion() + " · " + doctor.getSpecialty());
            infoLabel.setForeground(new Color(0x6B,0x7A,0x76));
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
            avatar.setBackground(new Color(0xD9,0xE7, 0xE2));
            avatar.setPreferredSize(new Dimension(76, 76));


            JLabel chip1 = new JLabel(doctor.getSpecialty());
            chip1.setFont(loadFont("fonts/Pretendard-Bold.otf", 12));
            chip1.setForeground(new Color(0x15,0x64,0x5A));
            chip1.setBackground(new Color(0xD9,0xE7,0xE2));
            chip1.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
            chip1.setOpaque(true);

            JLabel chip2 = new JLabel("비대면 진료");
            chip2.setFont(loadFont("fonts/Pretendard-Bold.otf", 12));
            chip2.setForeground(new Color(0x15,0x64,0x5A));
            chip2.setBackground(new Color(0xD9,0xE7,0xE2));
            chip2.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
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


            topSection.add(doctorRow);        // ← 여기서 딱 한 번
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
        divider.setPreferredSize(new Dimension(Integer.MAX_VALUE,1));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);

        topSection.add(Box.createVerticalStrut(18));
        topSection.add(divider);

// 6. 붙이고 보이기
        mainCard.add(topSection, BorderLayout.NORTH);
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