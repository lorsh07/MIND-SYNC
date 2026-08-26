package mindsync.dao;

import mindsync.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class PrescriptionDAO {

    public boolean createPrescription(String appointmentId, String medicationName, String dosage, String instructions, String issuedDate) {
            String id = UUID.randomUUID().toString();
            String sql = "INSERT INTO prescriptions (id, appointmentId, medicationName, dosage, instructions, issuedDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id);
                pstmt.setString(2, appointmentId);
                pstmt.setString(3, medicationName);
                pstmt.setString(4, dosage);
                pstmt.setString(5, instructions);
                pstmt.setString(6, issuedDate);

                pstmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

}
