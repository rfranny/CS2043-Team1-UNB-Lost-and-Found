package lostandfound.service;

import lostandfound.model.Account;
import lostandfound.repository.AccountRepository;

public class SignUpService {

    static final int MIN_USERNAME_LENGTH = 3;
    static final int MIN_PASSWORD_LENGTH = 8;

    private final AccountRepository repository;

    public SignUpService(AccountRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                "Repository cannot be null"
            );
        }

        this.repository = repository;
    }

    public Account signUp(
            String username,
            String email,
            String password,
            String confirmPassword) {

        validateUsername(username);
        validateEmail(email);
        validatePassword(password, confirmPassword);

        String cleanUsername = username.trim();

        if (repository.existsByUsername(cleanUsername)) {
            throw new IllegalArgumentException(
                "Username is already taken"
            );
        }

        String salt = PasswordHasher.generateSalt();

        Account account = new Account(
            cleanUsername,
            email.trim(),
            salt,
            PasswordHasher.hash(password, salt)
        );

        repository.save(account);

        return account;
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                "Username is required"
            );
        }

        String trimmed = username.trim();

        if (trimmed.length() < MIN_USERNAME_LENGTH) {
            throw new IllegalArgumentException(
                "Username must be at least "
                    + MIN_USERNAME_LENGTH + " characters"
            );
        }

        if (trimmed.contains(" ")) {
            throw new IllegalArgumentException(
                "Username cannot contain spaces"
            );
        }
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

    private void validatePassword(String password,
                                  String confirmPassword) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                "Password is required"
            );
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                "Password must be at least "
                    + MIN_PASSWORD_LENGTH + " characters"
            );
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                "Passwords do not match"
            );
        }
    }
}
