package entity;

/**
 * Database Entity representing a User (Student, Officer, or Admin).
 * This class is a pure data entity and does not contain business logic
 * for notifications (delegated to UserObserver).
 */
public class User {

    /**
     * Default constructor
     */
    public User() {
    }

    private int userID;
    private int roleID;
    private String email;

    // --- Basic Getters ---
    public int getUserID() { return userID; }
    public int getRoleID() { return roleID; }
    public String getEmail() { return email; }

    // --- Basic Setters ---
    public void setUserID(int userID) { this.userID = userID; }
    public void setRoleID(int roleID) { this.roleID = roleID; }
    public void setEmail(String email) { this.email = email; }
}