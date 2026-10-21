package mindsync;

import com.formdev.flatlaf.FlatLightLaf;
import mindsync.db.DatabaseManager;
import mindsync.gui.LoginFrame;

public class Main2 {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        DatabaseManager.createTables();

        new LoginFrame();
    }
}