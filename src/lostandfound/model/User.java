package lostandfound.model;

public class User {

    private final String name;
    private final String email;
    private final String salt;
    private final String passwordHash;

    public User(String name, String email,
                String salt, String passwordHash) {

        this.name = name;
        this.email = email;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSalt() {
        return salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
