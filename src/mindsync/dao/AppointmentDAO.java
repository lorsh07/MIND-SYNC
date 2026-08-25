package mindsync.dao;

import mindsync.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}