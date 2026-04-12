package behaviouralpatterns.mediator.case1_LoginWindow;

import dataaccess.DBConnection;
import core.SessionManager;
import factory.DashboardCreator;
import factory.AdminDashboardCreator;
import factory.OfficerDashboardCreator;
import factory.StudentDashboardCreator;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Mediator Pattern - Login Mediator Implementation
 *
 * Concrete mediator that coordinates all login UI components.
 * Handles authentication logic, session management, and dashboard opening.
 *
 * Role: ConcreteMediator
 */
public class LoginMediatorImpl implements LoginMediator {

    private final Map<ColleagueType, LoginColleague> colleagues;
    private final JFrame parentWindow;

    public LoginMediatorImpl(JFrame parentWindow) {
        this.colleagues = new EnumMap<>(ColleagueType.class);
        this.parentWindow = parentWindow;
    }

    @Override
    public void registerColleague(ColleagueType type, LoginColleague colleague) {
        colleagues.put(type, colleague);
        colleague.setMediator(this);
    }

    @Override
    public void notifyUsernameChanged(String username) {
        // Could add real-time validation here if needed
        // For now, we just acknowledge the change
    }

    @Override
    public void notifyPasswordChanged(String password) {
        // Could add real-time validation here if needed
    }

    @Override
    public void notifyLoginRequested() {
        // Get credentials from colleagues
        UsernameFieldColleague usernameColleague =
            (UsernameFieldColleague) colleagues.get(ColleagueType.USERNAME_FIELD);
        PasswordFieldColleague passwordColleague =
            (PasswordFieldColleague) colleagues.get(ColleagueType.PASSWORD_FIELD);
        LoginButtonColleague buttonColleague =
            (LoginButtonColleague) colleagues.get(ColleagueType.LOGIN_BUTTON);

        String username = (String) usernameColleague.getState();
        String password = (String) passwordColleague.getState();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            buttonColleague.onMediatorEvent("VALIDATION_ERROR",
                "Please enter both username and password.");
            return;
        }

        // Authenticate with DBConnection
        String role = DBConnection.authenticateUser(username, password);

        if (role != null) {
            // Login successful - get user ID and set session
            int userId = fetchUserIdByUsername(username);
            SessionManager.getInstance().setSession(userId, username, role);

            // Open appropriate dashboard using Factory pattern
            openDashboardForRole(role);

            // Notify success and close window
            buttonColleague.onMediatorEvent("LOGIN_SUCCESS",
                "Login Successful! Role: " + role);

            // Clear fields
            usernameColleague.onMediatorEvent("CLEAR_FIELDS", null);
            passwordColleague.onMediatorEvent("CLEAR_FIELDS", null);

            // Close the login window
            parentWindow.dispose();
        } else {
            // Login failed
            buttonColleague.onMediatorEvent("LOGIN_FAILURE",
                "Invalid username or password.");
        }
    }

    /**
     * Fetches the user ID for the authenticated user.
     * Used to populate the SessionManager.
     */
    private int fetchUserIdByUsername(String username) {
        java.util.List<Object[]> users = DBConnection.getAllUsersWithRoles();
        if (users != null) {
            for (Object[] row : users) {
                if (row != null && row.length >= 2) {
                    int id = (Integer) row[0];
                    String name = (String) row[1];
                    if (name != null && name.equals(username)) {
                        return id;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Uses the Factory pattern to create and open the appropriate dashboard.
     *
     * @param role the user's role (Admin, Officer, Student)
     */
    private void openDashboardForRole(String role) {
        DashboardCreator creator;

        if (role.equalsIgnoreCase("Admin")) {
            creator = new AdminDashboardCreator();
        } else if (role.equalsIgnoreCase("Officer")) {
            creator = new OfficerDashboardCreator();
        } else if (role.equalsIgnoreCase("Student")) {
            creator = new StudentDashboardCreator();
        } else {
            JOptionPane.showMessageDialog(
                parentWindow,
                "Unknown role: " + role,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JFrame dashboard = creator.createDashboard();
        dashboard.setVisible(true);
    }
}
