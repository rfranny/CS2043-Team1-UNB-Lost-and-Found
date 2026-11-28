package lostandfound.ui;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.swing.*;

import lostandfound.model.User;
import lostandfound.service.LoginService;

public class LoginPanel extends JPanel {

    private final LoginService service;
    private final Consumer<User> onLoginSuccess;
    private final Runnable onGoToSignUp;

    private final JTextField emailField;
    private final JPasswordField passwordField;

    private final JButton logInButton;
    private final JButton goToSignUpButton;

    /**
     * @param onLoginSuccess run with the logged-in user, e.g. to open
     *                       the Lost and Found screen
     * @param onGoToSignUp   run when the user wants the Sign Up screen
     */
    public LoginPanel(LoginService service,
                      Consumer<User> onLoginSuccess,
                      Runnable onGoToSignUp) {
        this.service = service;
        this.onLoginSuccess = onLoginSuccess;
        this.onGoToSignUp = onGoToSignUp;

        setLayout(new GridLayout(0, 1, 5, 5));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        logInButton = new JButton("Log In");
        goToSignUpButton = new JButton("Don't have an account? Sign Up");

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(logInButton);
        add(goToSignUpButton);

        logInButton.addActionListener(e -> submitLogin());
        passwordField.addActionListener(e -> submitLogin());
        goToSignUpButton.addActionListener(e -> onGoToSignUp.run());
    }

    private void submitLogin() {
        char[] password = passwordField.getPassword();

        try {
            User user = service.login(
                emailField.getText(),
                new String(password)
            );

            clearForm();
            onLoginSuccess.accept(user);

        } catch (IllegalArgumentException ex) {
            passwordField.setText("");

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Log In Failed",
                JOptionPane.ERROR_MESSAGE
            );

        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private void clearForm() {
        emailField.setText("");
        passwordField.setText("");
    }
}
