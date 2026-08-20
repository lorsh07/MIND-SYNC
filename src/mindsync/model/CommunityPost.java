package mindsync.model;

public class CommunityPost {
    private String id;
    private String patientId;
    private String title;
    private String content;
    private String createdAt;
    private boolean anonymous;

    public CommunityPost(String id, String patientId, String title, String content, String createdAt, boolean anonymous) {
        this.id = id;
        this.patientId = patientId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.anonymous = anonymous;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public boolean isAnonymous() {
        return anonymous;
    }
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
