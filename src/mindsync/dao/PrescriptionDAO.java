package mindsync.dao;

import mindsync.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import mindsync.model.Prescription;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

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

    public List<Prescription> getPrescriptionsByPatientId(String patientId) {
        String sql = "SELECT p.* FROM prescriptions p " +
                "JOIN appointments a ON p.appointmentId = a.id " +
                "WHERE a.patientId = ?";

        List<Prescription> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String appointmentId = rs.getString("appointmentId");
                String medicationName = rs.getString("medicationName");
                String dosage = rs.getString("dosage");
                String instructions = rs.getString("instructions");
                String issuedDate = rs.getString("issuedDate");

                Prescription p = new Prescription(id, appointmentId, medicationName, dosage, instructions, issuedDate);
                list.add(p);
            }

            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }
}
