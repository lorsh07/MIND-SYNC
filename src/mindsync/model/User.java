package mindsync.model;
// 이 파일이 mindsync.model 패키지에 속한다는 선언

public abstract class User {
// User라는 이름의 "추상 클래스"를 시작함.
// abstract이므로 new User(...)로 직접 객체를 만들 수 없고,
// 반드시 이 클래스를 상속받은 자식(Patient, Doctor)을 통해서만 쓸 수 있음

    protected String id;
    // 사용자 고유 식별자. protected라서 이 클래스와 자식 클래스(Patient, Doctor)에서 직접 접근 가능

    protected String username;
    // 로그인할 때 쓰는 아이디

    protected String passwordHash;
    // 비밀번호 (암호화된 형태로 저장할 값)

    protected String name;
    // 사용자의 실제 이름

    public User(String id, String username, String passwordHash, String name) {
        // 생성자: User(또는 그 자식) 객체가 만들어질 때 실행되어 위 4개 필드를 초기화함

        this.id = id;
        // 매개변수로 받은 id 값을 이 객체의 id 필드에 저장
        // this.id는 "이 객체의 id 필드", 그냥 id는 "매개변수로 들어온 값"

        this.username = username;
        // 매개변수로 받은 username을 필드에 저장

        this.passwordHash = passwordHash;
        // 매개변수로 받은 passwordHash를 필드에 저장

        this.name = name;
        // 매개변수로 받은 name을 필드에 저장
    }   // 생성자 끝

    public String getId() {
        // id 필드 값을 외부에서 꺼내볼 수 있게 해주는 getter 메서드

        return id;
        // 현재 객체의 id 값을 돌려줌
    }

    public String getUsername() {
        // username 필드를 꺼내는 getter

        return username;
        // 현재 객체의 username 값을 돌려줌
    }

    public String getPasswordHash() {
        // passwordHash 필드를 꺼내는 getter

        return passwordHash;
        // 현재 객체의 passwordHash 값을 돌려줌
    }

    public String getName() {
        // name 필드를 꺼내는 getter

        return name;
        // 현재 객체의 name 값을 돌려줌
    }

    public abstract String getRole();
    // 추상 메서드: 몸통({ }) 없이 선언만 되어 있음.
    // "역할이 뭔지 답해야 하는데, User 자신은 답을 모르니
    //  이걸 상속받는 자식(Patient, Doctor)이 반드시 직접 구현해서 답하라"는 뜻.
    // 예: Patient는 "환자", Doctor는 "의사"를 반환하도록 구현함

}   // User 클래스 끝