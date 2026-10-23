package mindsync;

import com.formdev.flatlaf.FlatLightLaf;
import mindsync.dao.UserDAO;
import mindsync.db.DatabaseManager;
import mindsync.gui.AppointmentFrame;
import mindsync.model.Patient;

public class Main2 {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        DatabaseManager.createTables();

        UserDAO userDAO = new UserDAO();
        userDAO.registerPatient(new Patient("p_test2", "pt_test2", "1234", "지역없음환자", "부산 해운대구"));

        new AppointmentFrame("p_test2");
    }
}