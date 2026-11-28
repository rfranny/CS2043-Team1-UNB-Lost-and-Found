package lostandfound.service;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Salted PBKDF2 password hashing from the JDK, shared by registration
 * and login so passwords are never stored or compared in plain text.
 */
public final class PasswordHasher {

    private static final int SALT_BYTES = 16;
    private static final int ITERATIONS = 65_536;
    private static final int KEY_BITS = 256;
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
            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                Base64.getDecoder().decode(salt),
                ITERATIONS,
                KEY_BITS
            );

            byte[] hashed = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .getEncoded();

            return Base64.getEncoder().encodeToString(hashed);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                "Password hashing is not available", e
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
