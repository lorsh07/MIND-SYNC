package mindsync;

import mindsync.dao.AppointmentDAO;
import mindsync.dao.UserDAO;
import mindsync.db.DatabaseManager;
import mindsync.model.Doctor;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.createTables();

        UserDAO userDAO = new UserDAO();
        Doctor doc = new Doctor("d001", "kimdoctor", "1234", "김의사", "서울 강남구", "12345", "정신건강의학과");
        userDAO.registerDoctor(doc);

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        String foundDoctorId = appointmentDAO.findDoctorIdByRegion("서울 강남구");
        System.out.println("찾은 의사 id: " + foundDoctorId);

        boolean created = appointmentDAO.createAppointment("p001", foundDoctorId, "최근 무기력하고 잠을 못 잠");
        System.out.println("예약 생성 결과: " + created);
    }
}