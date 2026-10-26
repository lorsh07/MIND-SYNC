package mindsync;

import com.formdev.flatlaf.FlatLightLaf;
import mindsync.dao.AppointmentDAO;
import mindsync.dao.UserDAO;
import mindsync.db.DatabaseManager;
import mindsync.gui.AppointmentFrame;
import mindsync.gui.LoginFrame;
import mindsync.model.Doctor;

public class Main2 {
    public static void main(String[] args) {
        System.out.println(new AppointmentDAO().getRequestsByDoctorId("df5a97c5-dafc-4523-badc-121bf0fb6f83").size());
    }
}