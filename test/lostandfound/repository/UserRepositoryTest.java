package lostandfound.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.User;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
    }

    private User user(String email) {
        return new User("Test User", email, "salt", "hash");
    }

    @Test
    void savedUserCanBeFoundByEmail() {
        User saved = user("nathan@unb.ca");
        repository.save(saved);

        assertSame(saved, repository.findByEmail("NATHAN@unb.ca"));
        assertTrue(repository.existsByEmail("nathan@unb.ca"));
        assertEquals(1, repository.count());
    }

    @Test
    void unknownEmailReturnsNull() {
        assertNull(repository.findByEmail("nobody@unb.ca"));
        assertFalse(repository.existsByEmail("nobody@unb.ca"));
    }

    @Test
    void duplicateEmailIsRejected() {
        repository.save(user("nathan@unb.ca"));

        assertThrows(
            IllegalArgumentException.class,
            () -> repository.save(user("Nathan@unb.ca"))
        );
    }

    @Test
    void nullUserIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> repository.save(null)
        );
    }
}
