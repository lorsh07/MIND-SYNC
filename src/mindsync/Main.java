package mindsync;

import mindsync.gui.SelfDiagnosisFrame;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        new SelfDiagnosisFrame("p001");
    }
}