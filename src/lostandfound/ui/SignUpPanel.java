package lostandfound.ui;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.*;

import lostandfound.model.Account;
import lostandfound.service.SignUpService;

public class SignUpPanel extends JPanel {

    private final SignUpService service;

    private final JTextField usernameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    private final JButton signUpButton;

    public SignUpPanel(SignUpService service) {
        this.service = service;

        setLayout(new GridLayout(0, 1, 5, 5));

        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        signUpButton = new JButton("Sign Up");

        add(new JLabel("Username:"));
        add(usernameField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        add(confirmPasswordField);

        add(signUpButton);

        signUpButton.addActionListener(e -> submitSignUp());
    }

    private void submitSignUp() {
        char[] password = passwordField.getPassword();
        char[] confirmPassword = confirmPasswordField.getPassword();

        try {
            Account account = service.signUp(
                usernameField.getText(),
                emailField.getText(),
                new String(password),
                new String(confirmPassword)
            );

            JOptionPane.showMessageDialog(
                this,
                "Account created successfully!\n"
                    + "Welcome, " + account.getUsername()
            );

            clearForm();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Sign Up Failed",
                JOptionPane.ERROR_MESSAGE
            );

        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmPassword, '\0');
        }
    }

    private void clearForm() {
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
