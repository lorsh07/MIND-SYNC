package mindsync;

import mindsync.dao.UserDAO;
import mindsync.db.DatabaseManager;
import mindsync.model.Patient;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.createTables();

        UserDAO userDAO = new UserDAO();

        Patient p = new Patient("p001", "honggildong", "1234", "홍길동", "서울 강남구");
        boolean success = userDAO.registerPatient(p);
        System.out.println("회원가입 결과: " + success);

        Patient loggedIn = userDAO.loginPatient("honggildong", "1234");
        if (loggedIn != null) {
            System.out.println("로그인 성공! 환영합니다, " + loggedIn.getName() + "님 (" + loggedIn.getRegion() + ")");
        } else {
            System.out.println("로그인 실패");
        }
    }
}