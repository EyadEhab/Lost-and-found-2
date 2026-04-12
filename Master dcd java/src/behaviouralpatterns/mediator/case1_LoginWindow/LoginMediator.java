package behaviouralpatterns.mediator.case1_LoginWindow;

/**
 * Mediator Pattern - Login Mediator Interface
 *
 * Defines the contract for communication between login components.
 * All colleagues (username field, password field, login button)
 * communicate through this mediator, not directly with each other.
 *
 * Role: Mediator (interface)
 */
public interface LoginMediator {

    /**
     * Called when username field value changes.
     * @param username the new username value
     */
    void notifyUsernameChanged(String username);

    /**
     * Called when password field value changes.
     * @param password the new password value
     */
    void notifyPasswordChanged(String password);

    /**
     * Called when login button is clicked.
     * Triggers the authentication process.
     */
    void notifyLoginRequested();

    /**
     * Registers a colleague with the mediator.
     * @param type the type of colleague
     * @param colleague the colleague instance
     */
    void registerColleague(ColleagueType type, LoginColleague colleague);

    /**
     * Enum defining the types of colleagues.
     */
    enum ColleagueType {
        USERNAME_FIELD,
        PASSWORD_FIELD,
        LOGIN_BUTTON
    }
}
