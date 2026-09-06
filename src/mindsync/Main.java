package mindsync;

import mindsync.gui.LoginFrame;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        LoginFrame s = new LoginFrame();
    }
}