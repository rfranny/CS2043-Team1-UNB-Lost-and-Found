package lostandfound.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.Account;
import lostandfound.repository.AccountRepository;

class SignUpServiceTest {

    private static final String PASSWORD = "secret123";

    private AccountRepository repository;
    private SignUpService service;

    @BeforeEach
    void setUp() {
        repository = new AccountRepository();
        service = new SignUpService(repository);
    }

    private Account signUpValid(String username) {
        return service.signUp(
            username, username + "@unb.ca", PASSWORD, PASSWORD
        );
    }

    @Test
    void validSignUpIsSaved() {
        Account account = service.signUp(
            "  nathan  ", " nathan@unb.ca ", PASSWORD, PASSWORD
        );

        assertEquals(1, repository.count());
        assertEquals("nathan", account.getUsername());
        assertEquals("nathan@unb.ca", account.getEmail());
        assertSame(account, repository.findByUsername("nathan"));
    }

    @Test
    void passwordIsStoredHashedNotPlainText() {
        Account account = signUpValid("nathan");

        assertNotEquals(PASSWORD, account.getPasswordHash());
        assertTrue(PasswordHasher.matches(
            PASSWORD, account.getSalt(), account.getPasswordHash()
        ));
        assertFalse(PasswordHasher.matches(
            "wrongpass1", account.getSalt(), account.getPasswordHash()
        ));
    }

    @Test
    void samePasswordGetsDifferentHashes() {
        Account first = signUpValid("nathan");
        Account second = signUpValid("kamsi");

        assertNotEquals(first.getPasswordHash(), second.getPasswordHash());
    }

    @Test
    void emptyUsernameIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.signUp("   ", "a@unb.ca", PASSWORD, PASSWORD)
        );

        assertEquals(0, repository.count());
    }

    @Test
    void shortUsernameIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.signUp("ab", "a@unb.ca", PASSWORD, PASSWORD)
        );
    }

    @Test
    void usernameWithSpacesIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.signUp("na than", "a@unb.ca", PASSWORD, PASSWORD)
        );
    }

    @Test
    void invalidEmailIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.signUp("nathan", "not-an-email", PASSWORD, PASSWORD)
        );

        assertEquals(0, repository.count());
    }

    @Test
    void shortPasswordIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.signUp("nathan", "a@unb.ca", "short", "short")
        );

        assertEquals(0, repository.count());
    }

    @Test
    void mismatchedPasswordsAreRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.signUp("nathan", "a@unb.ca", PASSWORD, "different1")
        );

        assertEquals(0, repository.count());
    }

    @Test
    void duplicateUsernameIsRejectedIgnoringCase() {
        signUpValid("nathan");

        assertThrows(
            IllegalArgumentException.class,
            () -> signUpValid("NATHAN")
        );

        assertEquals(1, repository.count());
    }
}
