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

    public List<MedicationLog> getLogsByPatientId(String patientId) {
        String sql = "SELECT * FROM medicationLogs WHERE patientId = ?";

        List<MedicationLog> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String prescriptionId = rs.getString("prescriptionId");
                String date = rs.getString("date");
                boolean taken = rs.getBoolean("taken");
                String note = rs.getString("note");

                MedicationLog m = new MedicationLog(id, patientId, prescriptionId, date, taken, note);
                list.add(m);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }
}
