package mindsync.dao;

import mindsync.db.DatabaseManager;
import mindsync.model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.HashSet;
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
            return list;
        }
        return list;
    }

    // 한 행(ResultSet)을 Appointment 객체로 바꿔주는 도우미 메서드
    private Appointment mapRow(ResultSet rs) throws SQLException {
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
    }

    // 의사의 예약 목록 (상태별: 요청 / 확정 / 완료)
    public List<Appointment> getAppointmentsByDoctorIdAndStatus(String doctorId, String status) {
        String sql = "SELECT * FROM appointments WHERE doctorId = ? AND status = ? ORDER BY scheduledAt ASC";
        List<Appointment> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctorId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 환자의 전체 진료 내역 (마이페이지용, 최신순)
    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        String sql = "SELECT * FROM appointments WHERE patientId = ? ORDER BY scheduledAt DESC";
        List<Appointment> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 진료를 끝냈을 때 상태를 '완료'로 바꾸고 의사 메모를 남긴다
    public boolean completeAppointment(String appointmentId, String doctorNotes) {
        String sql = "UPDATE appointments SET status = '완료', doctorNotes = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctorNotes);
            pstmt.setString(2, appointmentId);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Appointment getNextAppointmentByPatientId(String patientId) {
        // 날짜 형식(20xx-..)으로 저장된 '요청/확정' 상태 예약 중 가장 빠른 것
        String sql = "SELECT * FROM appointments WHERE patientId = ? AND scheduledAt LIKE '20%' " +
                "AND status IN ('요청', '확정') ORDER BY scheduledAt ASC LIMIT 1";

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

    // 해당 의사가 그 날짜에 이미 예약(요청/확정)된 시간 목록 (예: "10:30")
    public Set<String> getBookedTimes(String doctorId, String date) {
        Set<String> times = new HashSet<>();
        String sql = "SELECT scheduledAt FROM appointments WHERE doctorId = ? AND scheduledAt LIKE ? " +
                "AND status IN ('요청','확정')";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctorId);
            pstmt.setString(2, date + " %");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String v = rs.getString("scheduledAt");
                times.add(v.substring(date.length() + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return times;
    }
}
