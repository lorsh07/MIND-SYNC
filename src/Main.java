package mindsync;

import com.formdev.flatlaf.FlatLightLaf;
import mindsync.db.DatabaseManager;
import mindsync.gui.SelfDiagnosisFrame;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        DatabaseManager.createTables();

        new SelfDiagnosisFrame("p001");
    }
}