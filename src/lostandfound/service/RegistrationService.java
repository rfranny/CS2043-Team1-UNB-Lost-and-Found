package lostandfound.service;

import lostandfound.model.User;
import lostandfound.repository.UserRepository;

public class RegistrationService {

    private final UserRepository repository;

    public RegistrationService(UserRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                "Repository cannot be null"
            );
        }

        this.repository = repository;
    }

    public User register(
            String name,
            String email,
            String password,
            String confirmPassword) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Name is required"
            );
        }

        validateEmail(email);

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                "Password is required"
            );
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                "Passwords do not match"
            );
        }

        String cleanEmail = email.trim();

        if (repository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException(
                "An account with this email already exists"
            );
        }

        String salt = PasswordHasher.generateSalt();

        User user = new User(
            name.trim(),
            cleanEmail,
            salt,
            PasswordHasher.hash(password, salt)
        );

        repository.save(user);

        return user;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "Email is required"
            );
        }

        if (!email.trim().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException(
                "Please enter a valid email address"
            );
        }
    }
}
