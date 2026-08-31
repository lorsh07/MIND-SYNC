package mindsync.model;
// 이 파일이 mindsync.model 패키지에 속한다는 선언

public class Patient extends User{
// Patient 클래스 시작. extends User이므로 User를 상속받음
// User가 가진 id, username, passwordHash, name 필드와 그 getter들을 그대로 물려받음

    private String region;
    // Patient만 추가로 가지는 필드: 거주 지역
    // (User에는 없고, Patient에서 새로 추가한 것)

    public Patient(String id, String username, String passwordHash, String name, String region) {
        // 생성자: Patient 객체가 만들어질 때 실행됨
        // User의 4개 필드 + Patient만의 region까지 총 5개를 받음

        super(id, username, passwordHash, name);
        // 부모 클래스(User)의 생성자를 호출함.
        // 이 한 줄로 id, username, passwordHash, name 4개 필드가 초기화됨
        // (User.java에 있던 그 생성자가 실제로 실행되는 것)
        // super()는 반드시 생성자의 첫 줄에 와야 함

        this.region = region;
        // User가 처리해주지 못하는, Patient만의 region 필드는 직접 초기화

    }   // 생성자 끝

    @Override
    // 이 메서드가 부모(User)의 메서드를 "재정의(오버라이드)"하는 것임을 명시하는 표시
    // (없어도 동작은 하지만, 실수로 오타 내는 걸 컴파일러가 잡아주는 안전장치 역할)

    public String getRole() {
        // User에 있던 추상 메서드 getRole()을 Patient 나름의 방식으로 구현함
        // User는 이걸 비워뒀었고, Patient가 반드시 채워야 했음

        return "환자";
        // Patient 객체의 getRole()을 호출하면 항상 "환자"라는 문자열을 돌려줌
    }

    public String getRegion() {
        // region 필드를 읽는 getter (Patient만 가지는 필드라 User에는 없고 여기 새로 만듦)

        return region;
        // 현재 객체의 region 값을 돌려줌
    }
}   // Patient 클래스 끝
