package mindsync.model;

public class Patient extends User{
    private String region;

    public Patient(String id, String username, String passwordHash, String name, String region) {
        super(id, username, passwordHash, name);
        this.region = region;
    }

    @Override
    public String getRole() {
        return "환자";
    }

    public String getRegion() {
        return region;
    }
}
