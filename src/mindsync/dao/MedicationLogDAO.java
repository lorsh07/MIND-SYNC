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
import mindsync.model.Prescription;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class MedicationLogDAO {
    public boolean createLog(String patientId, String prescriptionId, String date, boolean taken, String note) {
        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO medicationLogs (id, patientId, prescriptionId, date, taken, memo) " +
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
                String note = rs.getString("memo");

                MedicationLog m = new MedicationLog(id, patientId, prescriptionId, date, taken, note);
                list.add(m);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }

    // 이 처방을 해당 날짜에 이미 복용 체크했는지
    public boolean isTakenOn(String prescriptionId, String date) {
        String sql = "SELECT COUNT(*) FROM medicationLogs WHERE prescriptionId = ? AND date = ? AND taken = 1";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, prescriptionId);
            pstmt.setString(2, date);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 복약 순응도(%) : 첫 처방일부터 오늘까지(최대 30일) 중 복용 체크한 날의 비율
    // 처방이 하나도 없으면 -1
    public int getAdherencePercent(String patientId) {
        List<Prescription> prescriptions = new PrescriptionDAO().getPrescriptionsByPatientId(patientId);
        if (prescriptions.isEmpty()) {
            return -1;
        }

        LocalDate today = LocalDate.now();
        LocalDate first = today;
        for (Prescription p : prescriptions) {
            try {
                LocalDate issued = LocalDate.parse(p.getIssuedDate());
                if (issued.isBefore(first)) {
                    first = issued;
                }
            } catch (Exception e) {
                // 날짜 형식이 이상한 처방은 건너뛴다
            }
        }

        long days = ChronoUnit.DAYS.between(first, today) + 1;
        if (days > 30) {
            days = 30;
        }
        LocalDate from = today.minusDays(days - 1);

        Set<String> takenDates = new HashSet<>();
        for (MedicationLog m : getLogsByPatientId(patientId)) {
            if (!m.isTaken()) {
                continue;
            }
            try {
                LocalDate d = LocalDate.parse(m.getDate());
                if (!d.isBefore(from) && !d.isAfter(today)) {
                    takenDates.add(m.getDate());
                }
            } catch (Exception e) {
            }
        }
        int percent = (int) (takenDates.size() * 100 / days);
        return Math.min(percent, 100);
    }
}
