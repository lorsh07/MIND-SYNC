package mindsync.model;

public abstract class User {
    protected String id;
    protected String username;
    protected String passwordHash;
    protected String name;

    public User(String id, String username, String passwordHash, String name) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public String getName() {
        return name;
    }
    public abstract String getRole();

}
