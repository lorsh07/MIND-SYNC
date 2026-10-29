package mindsync.dao;

import mindsync.db.DatabaseManager;
import mindsync.model.Patient;
import mindsync.model.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class UserDAO {
    public boolean registerPatient(Patient patient) {
        String sql = "INSERT INTO users (id, username, password_hash, name, role, region) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getId());
            pstmt.setString(2, patient.getUsername());
            pstmt.setString(3, patient.getPasswordHash());
            pstmt.setString(4, patient.getName());
            pstmt.setString(5, patient.getRole());
            pstmt.setString(6, patient.getRegion());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Patient loginPatient(String username, String passwordHash) {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ? AND role = '환자'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String region = rs.getString("region");
                return new Patient(id, username, passwordHash, name, region);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }
    }

    public boolean registerDoctor(Doctor doctor) {
        String sql = "INSERT INTO users (id, username, password_hash, name, role, region, license_number, specialty) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctor.getId());
            pstmt.setString(2, doctor.getUsername());
            pstmt.setString(3, doctor.getPasswordHash());
            pstmt.setString(4, doctor.getName());
            pstmt.setString(5, doctor.getRole());
            pstmt.setString(6, doctor.getHospitalRegion());
            pstmt.setString(7, doctor.getLicenseNumber());
            pstmt.setString(8, doctor.getSpecialty());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Doctor loginDoctor(String username, String passwordHash) {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ? AND role = '의사'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String region = rs.getString("region");
                String licenseNumber = rs.getString("license_number");
                String specialty = rs.getString("specialty");
                return new Doctor(id, username, passwordHash, name, region, licenseNumber, specialty);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }

    }
    public String getRegionByUserId(String id) {
        String sql = "SELECT region FROM users WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("region");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getNameByUserId(String id) {
        String sql = "SELECT name FROM users WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 가입된 의사 전체 (이름순)
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = '의사' ORDER BY name";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Doctor(rs.getString("id"), rs.getString("username"),
                        rs.getString("password_hash"), rs.getString("name"),
                        rs.getString("region"), rs.getString("license_number"),
                        rs.getString("specialty")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
