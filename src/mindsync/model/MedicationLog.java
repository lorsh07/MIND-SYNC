package mindsync.model;

public class MedicationLog {
    private String id;
    private String patientId;
    private String prescriptionId;
    private String date;
    private boolean taken;
    private String memo;

    public MedicationLog(String id, String patientId, String prescriptionId, String date, boolean taken, String memo) {
        this.id = id;
        this.patientId = patientId;
        this.prescriptionId = prescriptionId;
        this.date = date;
        this.taken = taken;
        this.memo = memo;
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
    public String getPrescriptionId() {
        return prescriptionId;
    }
    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean isTaken() {
        return taken;
    }
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
}
