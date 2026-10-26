package mindsync.dao;

import mindsync.db.DatabaseManager;
import mindsync.model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mindsync.model.Appointment;
import mindsync.model.Doctor;

public class AppointmentDAO {

    public Doctor findDoctorByRegion(String region) {
        String sql = "SELECT * FROM users WHERE role = '의사' AND region = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, region);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String username = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                String name = rs.getString("name");
                String doctorRegion = rs.getString("region");
                String licenseNumber = rs.getString("license_number");
                String specialty = rs.getString("specialty");
                return new Doctor(id, username, passwordHash, name, doctorRegion, licenseNumber, specialty);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }



    public boolean createAppointment(String patientId, String doctorId, String symptom, String consultationType,String scheduledAt) {
        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO appointments (id, patientId, doctorId, symptom, status, doctorNotes, consultationType, meetingLink, scheduledAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

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
            pstmt.setString(9, scheduledAt);

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
    public List<Appointment> getRequestsByDoctorId(String doctorId) {
        String sql = "SELECT * FROM appointments WHERE doctorId = ? AND status = '요청'";
        List<Appointment> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment(
                       rs.getString("id"),
                        rs.getString("patientId"),
                        rs.getString("doctorId"),
                        rs.getString("symptom"),
                        rs.getString("consultationType")
                );
                a.setScheduledAt(rs.getString("scheduledAt"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
    public Appointment getNextAppointmentByPatientId(String patientId) {
        String sql = "SELECT * FROM appointments WHERE patientId = ? AND scheduledAt != '' ORDER BY scheduledAt ASC LIMIT 1";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientId);
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
                a.setScheduledAt(rs.getString("scheduledAt"));
                return a;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}