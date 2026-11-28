package lostandfound.ui;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.*;

import lostandfound.service.RegistrationService;

public class RegisterPanel extends JPanel {

    private final RegistrationService service;
    private final Runnable onGoToLogin;

    private final JTextField nameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    private final JButton signUpButton;
    private final JButton goToLoginButton;

    /**
     * @param onGoToLogin run when the user wants the Login screen
     *                    (after signing up, or via the Log In button)
     */
    public RegisterPanel(RegistrationService service,
                         Runnable onGoToLogin) {
        this.service = service;
        this.onGoToLogin = onGoToLogin;

        setLayout(new GridLayout(0, 1, 5, 5));

        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        signUpButton = new JButton("Sign Up");
        goToLoginButton = new JButton("Already have an account? Log In");

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        add(confirmPasswordField);

        add(signUpButton);
        add(goToLoginButton);

        signUpButton.addActionListener(e -> submitRegistration());
        goToLoginButton.addActionListener(e -> onGoToLogin.run());
    }

    private void submitRegistration() {
        char[] password = passwordField.getPassword();
        char[] confirmPassword = confirmPasswordField.getPassword();

        try {
            service.register(
                nameField.getText(),
                emailField.getText(),
                new String(password),
                new String(confirmPassword)
            );

            JOptionPane.showMessageDialog(
                this,
                "Account created successfully"
            );

            clearForm();
            onGoToLogin.run();

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
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
