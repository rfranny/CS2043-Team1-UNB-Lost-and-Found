package lostandfound.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Salted SHA-256 password hashing, shared by sign-up and login so
 * passwords are never stored or compared in plain text.
 */
public final class PasswordHasher {

    private static final int SALT_BYTES = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    public static String generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(
                (salt + password).getBytes(StandardCharsets.UTF_8)
            );
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                "SHA-256 is not available", e
            );
        }
    }

    public static boolean matches(String password, String salt,
                                  String expectedHash) {
        return MessageDigest.isEqual(
            hash(password, salt).getBytes(StandardCharsets.UTF_8),
            expectedHash.getBytes(StandardCharsets.UTF_8)
        );
    }
}
