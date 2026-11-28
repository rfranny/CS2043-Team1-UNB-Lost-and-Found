package lostandfound.service;

import lostandfound.model.User;
import lostandfound.repository.UserRepository;

public class LoginService {

    // Same message for a wrong password and an unknown email,
    // so the screen does not reveal which emails are registered
    public static final String LOGIN_FAILED =
        "Incorrect email or password.";

    private final UserRepository repository;
    private User currentUser;

    public LoginService(UserRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                "Repository cannot be null"
            );
        }

        this.repository = repository;
    }

    public User login(String email, String password) {
        if (email == null || email.isBlank()
                || password == null || password.isEmpty()) {
            throw new IllegalArgumentException(LOGIN_FAILED);
        }

        User user = repository.findByEmail(email.trim());

        if (user == null || !PasswordHasher.matches(
                password, user.getSalt(), user.getPasswordHash())) {
            throw new IllegalArgumentException(LOGIN_FAILED);
        }

        currentUser = user;

        return user;
    }

    // The logged-in user, or null if nobody is logged in
    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
    }
}
