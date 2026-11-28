package lostandfound.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.User;
import lostandfound.repository.UserRepository;

class RegistrationServiceTest {

    private static final String PASSWORD = "secret123";

    private UserRepository repository;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
        service = new RegistrationService(repository);
    }

    @Test
    void successfulRegistrationIsSaved() {
        User user = service.register(
            "  Nathan Britto  ", " nathan@unb.ca ", PASSWORD, PASSWORD
        );

        assertEquals(1, repository.count());
        assertEquals("Nathan Britto", user.getName());
        assertEquals("nathan@unb.ca", user.getEmail());
        assertSame(user, repository.findByEmail("nathan@unb.ca"));
    }

    @Test
    void passwordIsStoredHashedNotPlainText() {
        User user = service.register(
            "Nathan", "nathan@unb.ca", PASSWORD, PASSWORD
        );

        assertNotEquals(PASSWORD, user.getPasswordHash());
        assertTrue(PasswordHasher.matches(
            PASSWORD, user.getSalt(), user.getPasswordHash()
        ));
        assertFalse(PasswordHasher.matches(
            "wrongpass", user.getSalt(), user.getPasswordHash()
        ));
    }

    @Test
    void samePasswordGetsDifferentHashes() {
        User first = service.register(
            "Nathan", "nathan@unb.ca", PASSWORD, PASSWORD
        );
        User second = service.register(
            "Kamsi", "kamsi@unb.ca", PASSWORD, PASSWORD
        );

        assertNotEquals(first.getPasswordHash(), second.getPasswordHash());
    }

    @Test
    void duplicateEmailIsRejectedIgnoringCase() {
        service.register("Nathan", "nathan@unb.ca", PASSWORD, PASSWORD);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.register(
                "Other", "NATHAN@unb.ca", PASSWORD, PASSWORD
            )
        );

        assertEquals(1, repository.count());
    }

    @Test
    void mismatchedPasswordsAreRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.register(
                "Nathan", "nathan@unb.ca", PASSWORD, "different1"
            )
        );

        assertEquals(0, repository.count());
    }

    @Test
    void missingNameIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.register("   ", "a@unb.ca", PASSWORD, PASSWORD)
        );

        assertEquals(0, repository.count());
    }

    @Test
    void missingEmailIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.register("Nathan", "", PASSWORD, PASSWORD)
        );

        assertEquals(0, repository.count());
    }

    @Test
    void invalidEmailIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.register(
                "Nathan", "not-an-email", PASSWORD, PASSWORD
            )
        );

        assertEquals(0, repository.count());
    }

    @Test
    void missingPasswordIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.register("Nathan", "a@unb.ca", "", "")
        );

        assertEquals(0, repository.count());
    }
}
