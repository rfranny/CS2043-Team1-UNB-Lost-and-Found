package lostandfound.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.Account;

class AccountRepositoryTest {

    private AccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new AccountRepository();
    }

    private Account account(String username) {
        return new Account(username, username + "@unb.ca", "salt", "hash");
    }

    @Test
    void savedAccountCanBeFoundByUsername() {
        Account saved = account("nathan");
        repository.save(saved);

        assertSame(saved, repository.findByUsername("NATHAN"));
        assertTrue(repository.existsByUsername("nathan"));
        assertEquals(1, repository.count());
    }

    @Test
    void unknownUsernameReturnsNull() {
        assertNull(repository.findByUsername("nobody"));
        assertFalse(repository.existsByUsername("nobody"));
    }

    @Test
    void duplicateUsernameIsRejected() {
        repository.save(account("nathan"));

        assertThrows(
            IllegalArgumentException.class,
            () -> repository.save(account("Nathan"))
        );
    }

    @Test
    void nullAccountIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> repository.save(null)
        );
    }
}
