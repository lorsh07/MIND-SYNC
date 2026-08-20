package mindsync.model;

public class Doctor extends User {
    private String hospitalRegion;
    private String licenseNumber;
    private String specialty;

    public Doctor(String id, String username, String passwordHash, String name, String region, String licenseNumber, String specialty) {
      super(id, username, passwordHash, name);
        this.hospitalRegion = region;
        this.licenseNumber = licenseNumber;
        this.specialty = specialty;
    }

    @Override
    public String getRole() {
        return "의사";
    }

    public String getHospitalRegion() {
        return hospitalRegion;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public String getSpecialty() {
        return specialty;
    }
}
