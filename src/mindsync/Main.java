package mindsync;

import mindsync.db.DatabaseManager;
public class Main {
    public static void main(String[] args) {
        DatabaseManager.createTables();
        System.out.println("테이블 생성 완료");
    }
}
