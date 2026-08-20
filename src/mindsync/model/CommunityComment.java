package mindsync.model;

public class CommunityComment {
    private String id;
    private String postId;
    private String patientId;
    private String content;
    private String createdAt;
    private boolean anonymous;

    public CommunityComment(String id, String postId, String patientId, String content, String createdAt, boolean anonymous){
        this.id = id;
        this.postId = postId;
        this.patientId = patientId;
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
    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
