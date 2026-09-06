package mindsync.dao;

import mindsync.db.DatabaseManager;
import mindsync.model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import mindsync.model.Appointment;

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
    public boolean createAppointment(String patientId, String doctorId, String symptom, String consultationType) {
        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO appointments (id, patientId, doctorId, symptom, status, doctorNotes, consultationType, meetingLink) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, patientId);
            pstmt.setString(3, doctorId);
            pstmt.setString(4, symptom);
            pstmt.setString(5, "요청");
            pstmt.setString(6, "");
            pstmt.setString(7, consultationType);
            pstmt.setString(8, "");

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean confirmAppointment(String appointmentId, String meetingLink) {
        String sql = "UPDATE appointments SET meetingLink = ?, status = '확정' WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, meetingLink);
            pstmt.setString(2, appointmentId);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Appointment getAppointmentById(String appointmentId) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Appointment a = new Appointment(
                        rs.getString("id"),
                        rs.getString("patientId"),
                        rs.getString("doctorId"),
                        rs.getString("symptom"),
                        rs.getString("consultationType")
                );
                a.setStatus(rs.getString("status"));
                a.setDoctorNotes(rs.getString("doctorNotes"));
                a.setMeetingLink(rs.getString("meetingLink"));
                return a;
            } else {
                return null;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}