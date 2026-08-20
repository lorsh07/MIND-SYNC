package mindsync.model;

public class Prescription {
    private String id;
    private String appointmentId;
    private String medicationName;
    private String dosage;
    private String instructions;
    private String issuedDate;

    public Prescription(String id, String appointmentId, String medicationName, String dosage, String instructions, String issuedDate) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.instructions = instructions;
        this.issuedDate = issuedDate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String  appointmentId ) {
        this.appointmentId = appointmentId;
    }
    public String getMedicationName() {
        return medicationName;
    }
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
    public String getDosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public String getIssuedDate() {
        return issuedDate;
    }
    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }
}
