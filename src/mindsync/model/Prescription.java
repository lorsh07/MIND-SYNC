package mindsync.model;
// 이 파일이 mindsync.model 패키지에 속한다는 선언

public class Prescription {
// Prescription(처방) 클래스 시작. 상속 관계 없는 독립적인 데이터 클래스

    private String id;
    // 이 처방의 고유 식별자

    private String appointmentId;
    // 이 처방이 어떤 진료(Appointment)에서 나온 것인지 나타내는 값

    private String medicationName;
    // 처방된 약품명

    private String dosage;
    // 용법·용량 (예: "1일 1회, 아침 식후")

    private String instructions;
    // 추가 지시사항 (복용 시 주의할 점 등)

    private String issuedDate;
    // 처방이 발급된 날짜

    public Prescription(String id, String appointmentId, String medicationName, String dosage, String instructions, String issuedDate) {
        // 생성자: 객체 생성 시 6개 필드를 한 번에 초기화

        this.id = id;
        // 매개변수 id를 이 객체의 id 필드에 저장

        this.appointmentId = appointmentId;
        // 매개변수 appointmentId를 필드에 저장

        this.medicationName = medicationName;
        // 매개변수 medicationName을 필드에 저장

        this.dosage = dosage;
        // 매개변수 dosage를 필드에 저장

        this.instructions = instructions;
        // 매개변수 instructions를 필드에 저장

        this.issuedDate = issuedDate;
        // 매개변수 issuedDate를 필드에 저장
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

    public String getAppointmentId() {
        // appointmentId 필드를 읽는 getter

        return appointmentId;
        // 현재 객체의 appointmentId 값을 돌려줌
    }
    public void setAppointmentId(String  appointmentId ) {
        // appointmentId 필드를 바꾸는 setter

        this.appointmentId = appointmentId;
        // 새 값으로 appointmentId 필드를 덮어씀
    }

    public String getMedicationName() {
        // medicationName 필드를 읽는 getter

        return medicationName;
        // 현재 객체의 medicationName 값을 돌려줌
    }
    public void setMedicationName(String medicationName) {
        // medicationName 필드를 바꾸는 setter

        this.medicationName = medicationName;
        // 새 값으로 medicationName 필드를 덮어씀
    }

    public String getDosage() {
        // dosage 필드를 읽는 getter

        return dosage;
        // 현재 객체의 dosage 값을 돌려줌
    }
    public void setDosage(String dosage) {
        // dosage 필드를 바꾸는 setter

        this.dosage = dosage;
        // 새 값으로 dosage 필드를 덮어씀
    }

    public String getInstructions() {
        // instructions 필드를 읽는 getter

        return instructions;
        // 현재 객체의 instructions 값을 돌려줌
    }
    public void setInstructions(String instructions) {
        // instructions 필드를 바꾸는 setter

        this.instructions = instructions;
        // 새 값으로 instructions 필드를 덮어씀
    }

    public String getIssuedDate() {
        // issuedDate 필드를 읽는 getter

        return issuedDate;
        // 현재 객체의 issuedDate 값을 돌려줌
    }
    public void setIssuedDate(String issuedDate) {
        // issuedDate 필드를 바꾸는 setter

        this.issuedDate = issuedDate;
        // 새 값으로 issuedDate 필드를 덮어씀
    }
}   // Prescription 클래스 끝