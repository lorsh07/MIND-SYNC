package mindsync.model;

public class SelfDiagnosisResult {
    private String id;
    private String patientId;
    private String testType;
    private String testDate;
    private int totalScore;
    private String interpretation;

    public SelfDiagnosisResult(String id, String patientId, String testType, String testDate, int totalScore, String interpretation) {
        this.id = id;
        this.patientId = patientId;
        this.testType = testType;
        this.testDate = testDate;
        this.totalScore = totalScore;
        this.interpretation = interpretation;
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
    public String getTestType() {
        return testType;
    }
    public void setTestType(String testType) {
        this.testType = testType;
    }
    public String getTestDate() {
        return testDate;
    }
    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    public String getInterpretation() {
        return interpretation;
    }
    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }
}
