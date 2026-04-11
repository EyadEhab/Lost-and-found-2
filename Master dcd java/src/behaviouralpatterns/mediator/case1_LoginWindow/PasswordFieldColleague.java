package behaviouralpatterns.mediator.case1_LoginWindow;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Mediator Pattern - Password Field Colleague
 *
 * Concrete colleague that wraps the password JPasswordField.
 * Notifies the mediator when the password changes,
 * and responds to mediator events.
 *
 * Role: ConcreteColleague
 */
public class PasswordFieldColleague implements LoginColleague {

    private final JPasswordField passwordField;
    private LoginMediator mediator;

    public PasswordFieldColleague(JPasswordField passwordField) {
        this.passwordField = passwordField;
        setupListener();
    }

    /**
     * Sets up a KeyListener to notify mediator on each keystroke.
     * This allows real-time validation if needed.
     */
    private void setupListener() {
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (mediator != null) {
                    mediator.notifyPasswordChanged(getState().toString());
                }
            }
        });
    }

    @Override
    public void setMediator(LoginMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onMediatorEvent(String event, Object data) {
        if ("CLEAR_FIELDS".equals(event)) {
            passwordField.setText("");
        } else if ("FOCUS_PASSWORD".equals(event)) {
            passwordField.requestFocus();
        }
    }

    @Override
    public Object getState() {
        return new String(passwordField.getPassword()).trim();
    }

    /**
     * Gets the password field for initial setup.
     * Should not be used for communication - use mediator instead.
     */
    public JPasswordField getPasswordField() {
        return passwordField;
    }
}
