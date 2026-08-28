package mindsync.dao;

import mindsync.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import mindsync.model.MedicationLog;

import javax.management.openmbean.InvalidOpenTypeException;

public class MedicationLogDAO {
    public boolean createLog(String patientId, String prescriptionId, String date, boolean taken, String note) {
        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO medicationLogs (id, patientId, prescriptionId, date, taken, note) " +
                " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, patientId);
            pstmt.setString(3, prescriptionId);
            pstmt.setString(4, date);
            pstmt.setBoolean(5, taken);
            pstmt.setString(6, note);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
