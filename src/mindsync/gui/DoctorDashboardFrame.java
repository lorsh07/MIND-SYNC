package mindsync.gui;
import mindsync.dao.AppointmentDAO;
import mindsync.model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class DoctorDashboardFrame extends JFrame {
    public  DoctorDashboardFrame(String doctorId, String doctorName) {

        setTitle("MIND SYNC - 의사 화면");
        setSize(1280,800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
