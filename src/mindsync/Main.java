package mindsync;

import mindsync.gui.LoginFrame;
import com.formdev.flatlaf.FlatLightLaf;
import mindsync.dao.SelfDiagnosisDAO;
import mindsync.db.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        DatabaseManager.createTables();

        SelfDiagnosisDAO selfDiagnosisDAO = new SelfDiagnosisDAO();
        selfDiagnosisDAO.createResult("lorsh", "PHQ-9", "2026-09-11", 8, "경증");

        LoginFrame s = new LoginFrame();
    }
}