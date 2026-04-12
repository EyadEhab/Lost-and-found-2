package behaviouralpatterns.mediator.case2_UserAdmin;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Mediator Pattern - Role Dropdown Colleague
 *
 * Concrete colleague that wraps the role JComboBox.
 * Provides selected role to mediator for updates.
 *
 * Role: ConcreteColleague
 */
public class RoleDropdownColleague implements UserAdminColleague {

    private final JComboBox<String> roleDropdown;
    private UserAdminMediator mediator;

    public RoleDropdownColleague(JComboBox<String> roleDropdown) {
        this.roleDropdown = roleDropdown;
        setupListener();
    }

    /**
     * Sets up an ActionListener on the dropdown.
     */
    private void setupListener() {
        roleDropdown.addActionListener((e) -> {
            // Could notify mediator of selection change if needed
        });
    }

    @Override
    public void setMediator(UserAdminMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onMediatorEvent(String event, Object data) {
        if ("ENABLE_DROPDOWN".equals(event)) {
            roleDropdown.setEnabled(true);
        } else if ("DISABLE_DROPDOWN".equals(event)) {
            roleDropdown.setEnabled(false);
        } else if ("SET_ROLE".equals(event) && data instanceof String) {
            roleDropdown.setSelectedItem((String) data);
        }
    }

    @Override
    public Object getState() {
        return roleDropdown.getSelectedItem();
    }

    /**
     * Gets the role dropdown for initial setup.
     */
    public JComboBox<String> getRoleDropdown() {
        return roleDropdown;
    }

    /**
     * Gets the selected role as a string.
     */
    public String getSelectedRole() {
        Object selected = roleDropdown.getSelectedItem();
        return selected != null ? selected.toString() : null;
    }
}
