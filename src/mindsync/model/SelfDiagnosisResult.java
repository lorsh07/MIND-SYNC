package mindsync.model;
// 이 파일이 mindsync.model 패키지에 속한다는 선언

public class SelfDiagnosisResult {
// SelfDiagnosisResult(자가진단 결과)라는 일반 클래스 시작
// User와 달리 abstract가 아니고, 상속도 안 받음 (독립적인 데이터 클래스)

    private String id;
    // 이 결과의 고유 식별자. private이라 이 클래스 밖에서는 getter/setter를 통해서만 접근 가능

    private String patientId;
    // 어떤 환자의 결과인지 나타내는 값 (환자의 id를 저장)

    private String testType;
    // 검사 종류 (예: "PHQ-9", "GAD-7")

    private String testDate;
    // 검사를 실시한 날짜

    private int totalScore;
    // 검사 총점. 숫자 계산이 필요한 값이라 String이 아니라 int로 선언

    private String interpretation;
    // 점수에 대한 해석 결과 문구 (예: "중등도 우울")

    public SelfDiagnosisResult(String id, String patientId, String testType, String testDate, int totalScore, String interpretation) {
        // 생성자: 객체가 new로 만들어질 때 6개 필드를 한 번에 초기화함

        this.id = id;
        // 매개변수 id 값을 이 객체의 id 필드에 저장

        this.patientId = patientId;
        // 매개변수 patientId 값을 필드에 저장

        this.testType = testType;
        // 매개변수 testType 값을 필드에 저장

        this.testDate = testDate;
        // 매개변수 testDate 값을 필드에 저장

        this.totalScore = totalScore;
        // 매개변수 totalScore 값을 필드에 저장

        this.interpretation = interpretation;
        // 매개변수 interpretation 값을 필드에 저장
    }   // 생성자 끝

    public String getId() {
        // id 필드를 외부에서 읽을 수 있게 해주는 getter

        return id;
        // 현재 객체의 id 값을 돌려줌
    }
    public void setId(String id) {
        // id 필드 값을 외부에서 바꿀 수 있게 해주는 setter

        this.id = id;
        // 매개변수로 들어온 새 값을 이 객체의 id 필드에 대입 (기존 값을 덮어씀)
    }

    public String getPatientId() {
        // patientId 필드를 읽는 getter

        return patientId;
        // 현재 객체의 patientId 값을 돌려줌
    }
    public void setPatientId(String patientId) {
        // patientId 필드를 바꾸는 setter

        this.patientId = patientId;
        // 새 값으로 patientId 필드를 덮어씀
    }

    public String getTestType() {
        // testType 필드를 읽는 getter

        return testType;
        // 현재 객체의 testType 값을 돌려줌
    }
    public void setTestType(String testType) {
        // testType 필드를 바꾸는 setter

        this.testType = testType;
        // 새 값으로 testType 필드를 덮어씀
    }

    public String getTestDate() {
        // testDate 필드를 읽는 getter

        return testDate;
        // 현재 객체의 testDate 값을 돌려줌
    }
    public void setTestDate(String testDate) {
        // testDate 필드를 바꾸는 setter

        this.testDate = testDate;
        // 새 값으로 testDate 필드를 덮어씀
    }

    public int getTotalScore() {
        // totalScore 필드를 읽는 getter (int 타입이라 boolean처럼 is를 안 붙이고 그냥 get을 씀)

        return totalScore;
        // 현재 객체의 totalScore 값을 돌려줌
    }
    public void setTotalScore(int totalScore) {
        // totalScore 필드를 바꾸는 setter

        this.totalScore = totalScore;
        // 새 값으로 totalScore 필드를 덮어씀
    }

    public String getInterpretation() {
        // interpretation 필드를 읽는 getter

        return interpretation;
        // 현재 객체의 interpretation 값을 돌려줌
    }
    public void setInterpretation(String interpretation) {
        // interpretation 필드를 바꾸는 setter

        this.interpretation = interpretation;
        // 새 값으로 interpretation 필드를 덮어씀
    }
}   // SelfDiagnosisResult 클래스 끝