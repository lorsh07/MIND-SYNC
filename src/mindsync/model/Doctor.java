package mindsync.model;
// 이 파일이 mindsync.model 패키지에 속한다는 선언

public class Doctor extends User {
// Doctor 클래스 시작. extends User이므로 User를 상속받음
// User가 가진 id, username, passwordHash, name 필드와 getter들을 그대로 물려받음

    private String hospitalRegion;
    // Doctor만 추가로 가지는 필드: 소속 병원 지역

    private String licenseNumber;
    // Doctor만 추가로 가지는 필드: 면허번호

    private String specialty;
    // Doctor만 추가로 가지는 필드: 전문 분야

    public Doctor(String id, String username, String passwordHash, String name, String region, String licenseNumber, String specialty) {
        // 생성자: Doctor 객체가 만들어질 때 실행됨
        // User의 4개 필드 + Doctor만의 3개(region, licenseNumber, specialty)까지 총 7개를 받음

        super(id, username, passwordHash, name);
        // 부모 클래스(User)의 생성자를 호출함.
        // 이 한 줄로 id, username, passwordHash, name 4개 필드가 초기화됨
        // super()는 반드시 생성자의 첫 줄에 와야 함

        this.hospitalRegion = region;
        // 매개변수로 받은 region 값을, Doctor의 필드인 hospitalRegion에 저장
        // (매개변수명은 region인데 필드명은 hospitalRegion이라 이름이 다름, 값 자체는 똑같이 전달됨)

        this.licenseNumber = licenseNumber;
        // 매개변수 licenseNumber를 필드에 저장

        this.specialty = specialty;
        // 매개변수 specialty를 필드에 저장

    }   // 생성자 끝

    @Override
    // 부모(User)의 메서드를 재정의한다는 표시

    public String getRole() {
        // User에 있던 추상 메서드 getRole()을 Doctor 나름의 방식으로 구현함

        return "의사";
        // Doctor 객체의 getRole()을 호출하면 항상 "의사"라는 문자열을 돌려줌
    }

    public String getHospitalRegion() {
        // hospitalRegion 필드를 읽는 getter

        return hospitalRegion;
        // 현재 객체의 hospitalRegion 값을 돌려줌
    }

    public String getLicenseNumber() {
        // licenseNumber 필드를 읽는 getter

        return licenseNumber;
        // 현재 객체의 licenseNumber 값을 돌려줌
    }
    public String getSpecialty() {
        // specialty 필드를 읽는 getter

        return specialty;
        // 현재 객체의 specialty 값을 돌려줌
    }
}   // Doctor 클래스 끝