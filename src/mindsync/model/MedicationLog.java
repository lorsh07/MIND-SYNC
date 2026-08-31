package mindsync.model;
// 이 파일이 mindsync.model 패키지에 속한다는 선언

public class MedicationLog {
// MedicationLog(투약 기록) 클래스 시작. 상속 없는 독립적인 데이터 클래스

    private String id;
    // 이 기록의 고유 식별자

    private String patientId;
    // 어떤 환자의 기록인지 나타내는 값

    private String prescriptionId;
    // 어떤 처방에 대한 복약 기록인지 나타내는 값

    private String date;
    // 기록 날짜 (몇 월 며칠에 복용했는지/안 했는지)

    private boolean taken;
    // 복용 여부. true(복용함) 또는 false(복용 안 함) 둘 중 하나만 가능

    private String memo;
    // 추가로 남길 메모 (선택 입력)

    public MedicationLog(String id, String patientId, String prescriptionId, String date, boolean taken, String memo) {
        // 생성자: 객체 생성 시 6개 필드를 한 번에 초기화

        this.id = id;
        // 매개변수 id를 필드에 저장

        this.patientId = patientId;
        // 매개변수 patientId를 필드에 저장

        this.prescriptionId = prescriptionId;
        // 매개변수 prescriptionId를 필드에 저장

        this.date = date;
        // 매개변수 date를 필드에 저장

        this.taken = taken;
        // 매개변수 taken(true/false)을 필드에 저장

        this.memo = memo;
        // 매개변수 memo를 필드에 저장
    }   // 생성자 끝

    public String getId() {
        // id 필드를 읽는 getter

        return id;
        // 현재 객체의 id 값을 돌려줌
    }

    public void setId(String id) {
        // id 필드를 바꾸는 setter

        this.id = id;
        // 새 값으로 id 필드를 덮어씀
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
    public String getPrescriptionId() {
        // prescriptionId 필드를 읽는 getter

        return prescriptionId;
        // 현재 객체의 prescriptionId 값을 돌려줌
    }
    public void setPrescriptionId(String prescriptionId) {
        // prescriptionId 필드를 바꾸는 setter

        this.prescriptionId = prescriptionId;
        // 새 값으로 prescriptionId 필드를 덮어씀
    }
    public String getDate() {
        // date 필드를 읽는 getter

        return date;
        // 현재 객체의 date 값을 돌려줌
    }
    public void setDate(String date) {
        // date 필드를 바꾸는 setter

        this.date = date;
        // 새 값으로 date 필드를 덮어씀
    }
    public boolean isTaken() {
        // taken 필드를 읽는 getter.
        // boolean 타입이라 get이 아니라 is로 시작하는 것이 관례

        return taken;
        // 현재 객체의 taken 값(true/false)을 돌려줌
    }
    public void setTaken(boolean taken) {
        // taken 필드를 바꾸는 setter

        this.taken = taken;
        // 새 값으로 taken 필드를 덮어씀
    }
    public String getMemo() {
        // memo 필드를 읽는 getter

        return memo;
        // 현재 객체의 memo 값을 돌려줌
    }
    public void setMemo(String memo) {
        // memo 필드를 바꾸는 setter

        this.memo = memo;
        // 새 값으로 memo 필드를 덮어씀
    }
}   // MedicationLog 클래스 끝