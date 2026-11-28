package lostandfound.repository;

import java.util.ArrayList;
import java.util.List;

import lostandfound.model.Account;

public class AccountRepository {

    private final List<Account> accounts;

    public AccountRepository() {
        accounts = new ArrayList<>();
    }

    // Save an account; usernames are unique (case-insensitive)
    public void save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException(
                "Account cannot be null"
            );
        }

        if (existsByUsername(account.getUsername())) {
            throw new IllegalArgumentException(
                "Username already exists"
            );
        }

        accounts.add(account);
    }

    // Find an account by username, or null if there is none
    public Account findByUsername(String username) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }

        return null;
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    // Get all accounts
    public List<Account> findAll() {
        return new ArrayList<>(accounts);
    }

    // Count accounts
    public int count() {
        return accounts.size();
    }
}
