package behaviouralpatterns.mediator.case2_UserAdmin;

/**
 * Mediator Pattern - User Admin Mediator Interface
 *
 * Defines the contract for communication between user admin components.
 * All colleagues (table, search, role dropdown, buttons) communicate
 * through this mediator, not directly with each other.
 *
 * Role: Mediator (interface)
 */
public interface UserAdminMediator {

    /**
     * Called when a row is selected in the user table.
     * @param userData the selected user's data array
     */
    void notifyTableSelectionChanged(Object[] userData);

    /**
     * Called when search button is clicked.
     * @param userId the user ID to search for
     */
    void notifySearchRequested(String userId);

    /**
     * Called when role update button is clicked.
     * @param userId the user ID to update
     * @param newRole the new role to assign
     */
    void notifyRoleUpdateRequested(int userId, String newRole);

    /**
     * Called when delete button is clicked.
     * @param userId the user ID to delete
     */
    void notifyUserDeleteRequested(int userId);

    /**
     * Called when add user button is clicked.
     */
    void notifyAddUserRequested();

    /**
     * Called when refresh button is clicked.
     */
    void notifyRefreshRequested();

    /**
     * Registers a colleague with the mediator.
     * @param type the type of colleague
     * @param colleague the colleague instance
     */
    void registerColleague(ColleagueType type, UserAdminColleague colleague);

    /**
     * Enum defining the types of colleagues.
     */
    enum ColleagueType {
        TABLE,
        SEARCH,
        ROLE_DROPDOWN,
        ACTIONS
    }
}
