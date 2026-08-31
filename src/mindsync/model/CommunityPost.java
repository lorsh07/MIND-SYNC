package mindsync.model;
// 이 파일이 mindsync.model 패키지에 속한다는 선언

public class CommunityPost {
// CommunityPost(게시글) 클래스 시작. 상속 없는 독립적인 데이터 클래스

    private String id;
    // 이 게시글의 고유 식별자

    private String patientId;
    // 이 글을 작성한 환자의 id

    private String title;
    // 게시글 제목

    private String content;
    // 게시글 본문 내용

    private String createdAt;
    // 게시글이 작성된 시각/날짜

    private boolean anonymous;
    // 익명 작성 여부. true(익명) 또는 false(닉네임/이름 표시) 둘 중 하나

    public CommunityPost(String id, String patientId, String title, String content, String createdAt, boolean anonymous) {
        // 생성자: 객체 생성 시 6개 필드를 한 번에 초기화

        this.id = id;
        // 매개변수 id를 필드에 저장

        this.patientId = patientId;
        // 매개변수 patientId를 필드에 저장

        this.title = title;
        // 매개변수 title을 필드에 저장

        this.content = content;
        // 매개변수 content를 필드에 저장

        this.createdAt = createdAt;
        // 매개변수 createdAt을 필드에 저장

        this.anonymous = anonymous;
        // 매개변수 anonymous(true/false)를 필드에 저장
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
    public String getTitle() {
        // title 필드를 읽는 getter

        return title;
        // 현재 객체의 title 값을 돌려줌
    }
    public void setTitle(String title) {
        // title 필드를 바꾸는 setter

        this.title = title;
        // 새 값으로 title 필드를 덮어씀
    }
    public String getContent() {
        // content 필드를 읽는 getter

        return content;
        // 현재 객체의 content 값을 돌려줌
    }
    public void setContent(String content) {
        // content 필드를 바꾸는 setter

        this.content = content;
        // 새 값으로 content 필드를 덮어씀
    }
    public String getCreatedAt() {
        // createdAt 필드를 읽는 getter

        return createdAt;
        // 현재 객체의 createdAt 값을 돌려줌
    }
    public void setCreatedAt(String createdAt) {
        // createdAt 필드를 바꾸는 setter

        this.createdAt = createdAt;
        // 새 값으로 createdAt 필드를 덮어씀
    }
    public boolean isAnonymous() {
        // anonymous 필드를 읽는 getter.
        // boolean 타입이라 get이 아니라 is로 시작

        return anonymous;
        // 현재 객체의 anonymous 값(true/false)을 돌려줌
    }
    public void setAnonymous(boolean anonymous) {
        // anonymous 필드를 바꾸는 setter

        this.anonymous = anonymous;
        // 새 값으로 anonymous 필드를 덮어씀
    }
}   // CommunityPost 클래스 끝