package mindsync.dao;
// 이 파일이 mindsync.dao 패키지에 속한다는 선언

import mindsync.db.DatabaseManager;   // DB 연결을 얻기 위해 가져옴

import java.sql.Connection;            // DB와의 연결을 표현하는 타입
import java.sql.PreparedStatement;     // 값을 안전하게 채워서 SQL 실행하는 도구
import java.sql.SQLException;          // DB 작업 실패 시 발생하는 예외 타입
import java.util.UUID;                 // 고유 id를 자동 생성하기 위해
import java.sql.ResultSet;             // SELECT 결과(표 형태)를 담는 그릇
import java.util.List;                 // 여러 개를 순서대로 담는 목록 타입
import java.util.ArrayList;            // List의 실제 구현체

import mindsync.model.MedicationLog;         // (이 파일에서 실제로는 안 쓰이지만 미리 가져와둔 import)
import mindsync.model.SelfDiagnosisResult;   // 자가진단 결과 모델 클래스

public class SelfDiagnosisDAO {
// 자가진단 결과 관련 DB 작업을 모아둔 클래스 시작

    public boolean createResult(String patientId, String testType, String testDate, int totalScore, String interpretation) {
        // 새 자가진단 결과 하나를 저장하는 메서드. 성공하면 true, 실패하면 false

        String id = UUID.randomUUID().toString();
        // 이 결과의 고유 id를 무작위로 자동 생성

        String sql = "INSERT INTO selfDiagnosisResults (id, patientId, testType, testDate, totalScore, interpretation)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        // selfDiagnosisResults 테이블에 새 행 추가할 SQL 틀.
        // 컬럼 6개에 대응하는 ? 자리표시자 6개

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // DB 연결을 열고, 위 SQL을 실행 준비 상태로 만듦
            // 이 블록이 끝나면 conn, pstmt 자동으로 닫힘

            pstmt.setString(1, id);
            // 1번째 ? 자리에 새로 만든 id를 채움

            pstmt.setString(2, patientId);
            // 2번째 ? 자리에 환자 id를 채움

            pstmt.setString(3, testType);
            // 3번째 ? 자리에 검사 종류("PHQ-9" 등)를 채움

            pstmt.setString(4, testDate);
            // 4번째 ? 자리에 검사 날짜를 채움

            pstmt.setInt(5, totalScore);
            // 5번째 ? 자리에 총점을 채움. int 타입이라 setString이 아니라 setInt 사용

            pstmt.setString(6, interpretation);
            // 6번째 ? 자리에 해석 결과 문구를 채움

            pstmt.executeUpdate();
            // 완성된 SQL을 실제로 DB에 실행시켜서 데이터를 진짜로 저장함

            return true;
            // 여기까지 에러 없이 왔으니 성공했다는 뜻으로 true 반환

        } catch (SQLException e) {
            // try 블록 안에서 DB 관련 문제가 생기면 여기로 옴

            e.printStackTrace();
            // 무슨 에러였는지 콘솔에 자세히 출력

            return false;
            // 실패했으니 false 반환

        }
    }   // createResult 메서드 끝

    public List<SelfDiagnosisResult> getLogsByPatientId(String patientId) {
        // 특정 환자의 자가진단 결과 목록 전체를 조회하는 메서드

        String sql = "SELECT * FROM selfDiagnosisResults WHERE patientId = ?";
        // selfDiagnosisResults 테이블에서, patientId가 일치하는 모든 행을 가져오는 SQL

        List<SelfDiagnosisResult> list = new ArrayList<>();
        // 결과들을 담을 빈 리스트(상자)를 미리 준비

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // DB 연결을 열고, SQL 실행 준비

            pstmt.setString(1, patientId);
            // 1번째 ? 자리에 조회하려는 환자의 id를 채움

            ResultSet rs = pstmt.executeQuery();
            // SELECT니까 executeQuery()로 실행하고, 결과(표)를 rs로 받음

            while (rs.next()) {
                // 결과표에 다음 행이 있는 한 계속 반복
                // (환자의 자가진단 결과가 여러 개일 수 있으므로 while로 전부 순회)

                String id = rs.getString("id");
                // 현재 행의 id 컬럼 값을 꺼냄

                String testType = rs.getString("testType");
                // 현재 행의 testType 컬럼 값을 꺼냄

                String testDate = rs.getString("testDate");
                // 현재 행의 testDate 컬럼 값을 꺼냄

                int totalScore = rs.getInt("totalScore");
                // 현재 행의 totalScore 컬럼 값을 정수로 꺼냄 (getInt 사용)

                String interpretation = rs.getString("interpretation");
                // 현재 행의 interpretation 컬럼 값을 꺼냄

                SelfDiagnosisResult s = new SelfDiagnosisResult(id, patientId, testType, testDate, totalScore, interpretation);
                // 꺼낸 값들로 SelfDiagnosisResult 객체 하나를 새로 만듦

                list.add(s);
                // 만든 객체를 리스트 상자에 추가
            }
            return list;
            // 반복이 다 끝나면, 지금까지 모은 리스트 전체를 돌려줌

        }catch (SQLException e){
            // try 블록 안에서 DB 관련 문제가 생기면 여기로 옴

            e.printStackTrace();
            // 무슨 에러였는지 콘솔에 출력

            return list;
            // 에러가 나도 (지금까지 담긴 만큼, 혹은 빈 상태의) 리스트를 돌려줌
        }
    }   // getLogsByPatientId 메서드 끝

}   // SelfDiagnosisDAO 클래스 끝