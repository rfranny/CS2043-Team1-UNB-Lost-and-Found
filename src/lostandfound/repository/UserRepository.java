package lostandfound.repository;

import java.util.ArrayList;
import java.util.List;

import lostandfound.model.User;

public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        users = new ArrayList<>();
    }

    // Save a user; emails are unique (case-insensitive)
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException(
                "User cannot be null"
            );
        }

        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "Email already registered"
            );
        }

        users.add(user);
    }

    // Find a user by email, or null if there is none
    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    // Get all users
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    // Count users
    public int count() {
        return users.size();
    }
}
