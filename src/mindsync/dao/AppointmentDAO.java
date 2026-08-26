package mindsync.dao;

import mindsync.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AppointmentDAO {
    public String findDoctorIdByRegion(String region) {
        String sql = "SELECT id FROM users WHERE role = '의사' AND region = ?";

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, region);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("id");
            } else {
                return null;
            }
        }
            catch(SQLException e) {
                    e.printStackTrace();
            return null;
                    }

    }
    public boolean createAppointment(String patientId, String doctorId, String symptom) {
        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO appointments (id, patientId, doctorId, symptom, status, doctorNotes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, patientId);
            pstmt.setString(3, doctorId);
            pstmt.setString(4, symptom);
            pstmt.setString(5, "요청");
            pstmt.setString(6, "");

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}