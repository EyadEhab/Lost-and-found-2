package behaviouralpatterns.mediator.case1_LoginWindow;

import javax.swing.*;
import java.awt.*;
import core.ThemeManager;
import factory.ui.UIFactory;

/**
 * Mediator Pattern - Login Application Main Class
 *
 * This is the main entry point that demonstrates the Mediator pattern.
 * It creates the UI components, wires them together through the mediator,
 * and launches the login window.
 *
 * This is NOT a demo - it's a fully functional login system that:
 * - Authenticates real users against the database
 * - Sets up sessions via SessionManager
 * - Opens the appropriate dashboard based on user role
 */
public class LoginMediatorApp extends JFrame {

    private LoginMediator mediator;
    private UsernameFieldColleague usernameColleague;
    private PasswordFieldColleague passwordColleague;
    private LoginButtonColleague loginButtonColleague;

    public LoginMediatorApp() {
        setTitle("Lost and Found Login - Mediator Pattern");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeMediator();
        initComponents();
        setupLayout();
    }

    /**
     * Creates the mediator instance.
     * The mediator coordinates all UI components.
     */
    private void initializeMediator() {
        mediator = new LoginMediatorImpl(this);
    }

    /**
     * Creates UI components and wraps them in colleagues.
     * Uses the UIFactory for theme-aware component creation.
     */
    private void initComponents() {
        UIFactory factory = ThemeManager.getInstance().getFactory();

        // Create username field and wrap in colleague
        JTextField usernameField = factory.createTextField(15);
        usernameColleague = new UsernameFieldColleague(usernameField);
        mediator.registerColleague(LoginMediator.ColleagueType.USERNAME_FIELD, usernameColleague);

        // Create password field and wrap in colleague
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(factory.getSurfaceColor());
        passwordField.setForeground(factory.getTextColor());
        passwordField.setCaretColor(factory.getTextColor());
        passwordColleague = new PasswordFieldColleague(passwordField);
        mediator.registerColleague(LoginMediator.ColleagueType.PASSWORD_FIELD, passwordColleague);

        // Create login button and wrap in colleague
        JButton loginButton = factory.createButton("Login");
        loginButtonColleague = new LoginButtonColleague(loginButton);
        mediator.registerColleague(LoginMediator.ColleagueType.LOGIN_BUTTON, loginButtonColleague);

        // Create theme toggle button (not part of mediator - independent)
        JButton toggleThemeButton = factory.createButton("Toggle Theme");
        toggleThemeButton.addActionListener(e -> {
            ThemeManager.getInstance().toggleTheme();
            dispose();
            new LoginMediatorApp().setVisible(true);
        });

        // Store toggle button for layout
        this.toggleThemeButton = toggleThemeButton;
    }

    // Keep reference to toggle button for layout
    private JButton toggleThemeButton;

    /**
     * Sets up the layout with all UI components.
     * Uses GridLayout for a clean, simple login form.
     */
    private void setupLayout() {
        UIFactory factory = ThemeManager.getInstance().getFactory();
        getContentPane().setBackground(factory.getBackgroundColor());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(factory.getBackgroundColor());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add username label and field
        panel.add(factory.createLabel("Username:"));
        panel.add(usernameColleague.getUsernameField());

        // Add password label and field
        panel.add(factory.createLabel("Password:"));
        panel.add(passwordColleague.getPasswordField());

        // Add theme toggle and login buttons
        panel.add(toggleThemeButton);
        panel.add(loginButtonColleague.getLoginButton());

        add(panel);
    }

    /**
     * Main method - launches the login application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginMediatorApp().setVisible(true);
        });
    }
}
