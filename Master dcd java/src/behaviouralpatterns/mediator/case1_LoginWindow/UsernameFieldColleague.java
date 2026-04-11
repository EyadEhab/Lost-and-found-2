package behaviouralpatterns.mediator.case1_LoginWindow;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Mediator Pattern - Username Field Colleague
 *
 * Concrete colleague that wraps the username JTextField.
 * Notifies the mediator when the username changes,
 * and responds to mediator events.
 *
 * Role: ConcreteColleague
 */
public class UsernameFieldColleague implements LoginColleague {

    private final JTextField usernameField;
    private LoginMediator mediator;

    public UsernameFieldColleague(JTextField usernameField) {
        this.usernameField = usernameField;
        setupListener();
    }

    /**
     * Sets up a KeyListener to notify mediator on each keystroke.
     * This allows real-time validation if needed.
     */
    private void setupListener() {
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (mediator != null) {
                    mediator.notifyUsernameChanged(getState().toString());
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
            usernameField.setText("");
        } else if ("FOCUS_USERNAME".equals(event)) {
            usernameField.requestFocus();
        }
    }

    @Override
    public Object getState() {
        return usernameField.getText().trim();
    }

    /**
     * Gets the username field for initial setup.
     * Should not be used for communication - use mediator instead.
     */
    public JTextField getUsernameField() {
        return usernameField;
    }
}
