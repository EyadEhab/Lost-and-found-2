package behaviouralpatterns.mediator.case1_LoginWindow;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Mediator Pattern - Login Button Colleague
 *
 * Concrete colleague that wraps the login JButton.
 * Notifies the mediator when clicked,
 * and responds to success/failure events from mediator.
 *
 * Role: ConcreteColleague
 */
public class LoginButtonColleague implements LoginColleague {

    private final JButton loginButton;
    private LoginMediator mediator;

    public LoginButtonColleague(JButton loginButton) {
        this.loginButton = loginButton;
        setupListener();
    }

    /**
     * Sets up an ActionListener to notify mediator when button is clicked.
     */
    private void setupListener() {
        loginButton.addActionListener((e) -> {
            if (mediator != null) {
                mediator.notifyLoginRequested();
            }
        });
    }

    @Override
    public void setMediator(LoginMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onMediatorEvent(String event, Object data) {
        if ("LOGIN_SUCCESS".equals(event)) {
            String message = (String) data;
            loginButton.setEnabled(false);
            JOptionPane.showMessageDialog(
                loginButton,
                message,
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else if ("LOGIN_FAILURE".equals(event)) {
            String message = (String) data;
            JOptionPane.showMessageDialog(
                loginButton,
                message,
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );
            loginButton.setEnabled(true);
        } else if ("VALIDATION_ERROR".equals(event)) {
            String message = (String) data;
            JOptionPane.showMessageDialog(
                loginButton,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    @Override
    public Object getState() {
        return loginButton.isEnabled();
    }

    /**
     * Gets the login button for initial setup.
     * Should not be used for communication - use mediator instead.
     */
    public JButton getLoginButton() {
        return loginButton;
    }
}
