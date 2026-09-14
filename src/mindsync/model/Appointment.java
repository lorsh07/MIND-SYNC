package mindsync.model;

public class Appointment {
   private String id;
   private String patientId;
   private String doctorId;
   private String symptom;
   private String status;
   private String doctorNotes;
   private String meetingLink;
   private String consultationType;
   private String scheduledAt;

    public Appointment(String id, String patientId, String doctorId, String symptom, String consultationType) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.symptom = symptom;
        this.status = "요청";
        this.doctorNotes = "";
        this.consultationType = consultationType;
        this.meetingLink = "";
        this.scheduledAt = "";
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
    public String getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    public String getSymptom() {
        return symptom;
    }
    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDoctorNotes() {
        return doctorNotes;
    }
    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
    public String getConsultationType() {
        return consultationType;
    }
    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }
    public String getMeetingLink() {
        return meetingLink;
    }
    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }
    public String getScheduledAt() {
        return scheduledAt;
    }
    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}