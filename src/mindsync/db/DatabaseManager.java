package mindsync.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:mindsync.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    public static void createTables() {
        String userSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id TEXT PRIMARY KEY, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password_hash TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "region TEXT, " +
                "license_number TEXT, " +
                "specialty TEXT" +
                ")";
        String appointmentSql = "CREATE TABLE IF NOT EXISTS appointments ("+
                "id TEXT PRIMARY KEY, "+
                "patientId TEXT NOT NULL, "+
                "doctorId TEXT NOT NULL, "+
                "symptom TEXT, "+
                "status TEXT NOT NULL, "+
                "doctorNotes TEXT" +
                ")";
        String prescriptionSql = "CREATE TABLE IF NOT EXISTS prescriptions ("+
                "id TEXT PRIMARY KEY, "+
                "appointmentId TEXT NOT NULL, "+
                "medicationName TEXT NOT NULL, "+
                "dosage TEXT, "+
                "instructions TEXT, "+
                "issuedDate TEXT" +
                ")";
        String medicationLogSql = "CREATE TABLE IF NOT EXISTS medicationLogs ("+
                "id TEXT PRIMARY KEY, "+
                "patientId TEXT NOT NULL, "+
                "prescriptionId TEXT NOT NULL, "+
                "date TEXT NOT NULL, "+
                "taken INTEGER NOT NULL, "+
                "memo TEXT"+
                ")";
        String selfDiagnosisResultSql = "CREATE TABLE IF NOT EXISTS selfDiagnosisResults ("+
                "id TEXT PRIMARY KEY, "+
                "patientId TEXT NOT NULL, "+
                "testType TEXT NOT NULL, "+
                "testDate TEXT NOT NULL, "+
                "totalScore INTEGER NOT NULL, "+
                "interpretation TEXT"+
                ")";

        String communityPostSql = "CREATE TABLE IF NOT EXISTS communityPosts (" +
                "id TEXT PRIMARY KEY, " +
                "patientId TEXT NOT NULL, " +
                "title TEXT NOT NULL, " +
                "content TEXT, " +
                "createdAt TEXT NOT NULL, " +
                "anonymous INTEGER NOT NULL" +
                ")";

        String communityCommentSql = "CREATE TABLE IF NOT EXISTS communityComments (" +
                "id TEXT PRIMARY KEY, " +
                "postId TEXT NOT NULL, " +
                "patientId TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "createdAt TEXT NOT NULL, " +
                "anonymous INTEGER NOT NULL" +
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(userSql);
            stmt.execute(appointmentSql);
            stmt.execute(prescriptionSql);
            stmt.execute(medicationLogSql);
            stmt.execute(selfDiagnosisResultSql);
            stmt.execute(communityPostSql);
            stmt.execute(communityCommentSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
