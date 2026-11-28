package lostandfound.model;

public class Account {

    private final String username;
    private final String email;
    private final String salt;
    private final String passwordHash;

    public Account(String username, String email,
                   String salt, String passwordHash) {

        this.username = username;
        this.email = email;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
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
