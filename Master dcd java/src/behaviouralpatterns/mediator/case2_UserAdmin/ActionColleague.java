package behaviouralpatterns.mediator.case2_UserAdmin;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Mediator Pattern - Action Colleague
 *
 * Concrete colleague that wraps all action buttons:
 * - Refresh button
 * - Add User button
 * - Update Role button
 * - Remove User button
 *
 * Notifies mediator when any button is clicked.
 *
 * Role: ConcreteColleague
 */
public class ActionColleague implements UserAdminColleague {

    private final JButton refreshButton;
    private final JButton addUserButton;
    private final JButton updateRoleButton;
    private final JButton removeUserButton;
    private UserAdminMediator mediator;

    public ActionColleague(JButton refreshButton, JButton addUserButton,
                           JButton updateRoleButton, JButton removeUserButton) {
        this.refreshButton = refreshButton;
        this.addUserButton = addUserButton;
        this.updateRoleButton = updateRoleButton;
        this.removeUserButton = removeUserButton;
        setupListeners();
    }

    /**
     * Sets up ActionListeners on all buttons.
     */
    private void setupListeners() {
        refreshButton.addActionListener((e) -> {
            if (mediator != null) {
                mediator.notifyRefreshRequested();
            }
        });

        addUserButton.addActionListener((e) -> {
            if (mediator != null) {
                mediator.notifyAddUserRequested();
            }
        });

        updateRoleButton.addActionListener((e) -> {
            if (mediator != null) {
                mediator.notifyRoleUpdateRequested(-1, null); // Will get actual values from mediator
            }
        });

        removeUserButton.addActionListener((e) -> {
            if (mediator != null) {
                mediator.notifyUserDeleteRequested(-1); // Will get actual ID from mediator
            }
        });
    }

    @Override
    public void setMediator(UserAdminMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onMediatorEvent(String event, Object data) {
        if ("ENABLE_ACTIONS".equals(event)) {
            updateRoleButton.setEnabled(true);
            removeUserButton.setEnabled(true);
        } else if ("DISABLE_ACTIONS".equals(event)) {
            updateRoleButton.setEnabled(false);
            removeUserButton.setEnabled(false);
        } else if ("SHOW_SUCCESS".equals(event) && data instanceof String) {
            JOptionPane.showMessageDialog(
                refreshButton,
                (String) data,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else if ("SHOW_ERROR".equals(event) && data instanceof String) {
            JOptionPane.showMessageDialog(
                refreshButton,
                (String) data,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        } else if ("SHOW_WARNING".equals(event) && data instanceof String) {
            JOptionPane.showMessageDialog(
                refreshButton,
                (String) data,
                "Warning",
                JOptionPane.WARNING_MESSAGE
            );
        } else if ("SHOW_CONFIRM_AND_EXECUTE".equals(event) && data instanceof ConfirmData) {
            ConfirmData confirmData = (ConfirmData) data;
            int result = JOptionPane.showConfirmDialog(
                refreshButton,
                confirmData.message,
                confirmData.title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION && confirmData.callback != null) {
                confirmData.callback.run();
            }
        }
    }

    @Override
    public Object getState() {
        return updateRoleButton.isEnabled();
    }

    /**
     * Helper class for confirm dialog data.
     */
    public static class ConfirmData {
        public final String message;
        public final String title;
        public final Runnable callback;

        public ConfirmData(String message, String title, Runnable callback) {
            this.message = message;
            this.title = title;
            this.callback = callback;
        }
    }

    // Getters for initial setup
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getAddUserButton() { return addUserButton; }
    public JButton getUpdateRoleButton() { return updateRoleButton; }
    public JButton getRemoveUserButton() { return removeUserButton; }
}
