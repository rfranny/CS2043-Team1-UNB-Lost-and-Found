package lostandfound.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.User;
import lostandfound.repository.UserRepository;

class LoginServiceTest {

    private static final String EMAIL = "kamsi@unb.ca";
    private static final String PASSWORD = "secret123";

    private UserRepository repository;
    private LoginService service;
    private User registered;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
        service = new LoginService(repository);

        // Create the account the same way the Sign Up screen does
        registered = new RegistrationService(repository)
            .register("Kamsi", EMAIL, PASSWORD, PASSWORD);
    }

    @Test
    void successfulLoginReturnsUserAndRecordsCurrentUser() {
        User user = service.login(EMAIL, PASSWORD);

        assertSame(registered, user);
        assertSame(registered, service.getCurrentUser());
        assertTrue(service.isLoggedIn());
    }

    @Test
    void incorrectPasswordIsRejected() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.login(EMAIL, "wrongpass")
        );

        assertEquals(LoginService.LOGIN_FAILED, ex.getMessage());
        assertNull(service.getCurrentUser());
    }

    @Test
    void unknownEmailIsRejected() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.login("nobody@unb.ca", PASSWORD)
        );

        assertEquals(LoginService.LOGIN_FAILED, ex.getMessage());
        assertNull(service.getCurrentUser());
    }

    @Test
    void emailIsMatchedIgnoringCaseAndSpaces() {
        User user = service.login("  KAMSI@unb.ca ", PASSWORD);

        assertSame(registered, user);
    }

    @Test
    void blankEmailOrPasswordIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.login("", PASSWORD)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.login(EMAIL, "")
        );

        assertFalse(service.isLoggedIn());
    }

    @Test
    void logoutClearsCurrentUser() {
        service.login(EMAIL, PASSWORD);
        service.logout();

        assertNull(service.getCurrentUser());
        assertFalse(service.isLoggedIn());
    }
}
