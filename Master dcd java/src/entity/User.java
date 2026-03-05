package entity;

/**
 * 
 */
public class User {

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * 
     */
    private int userID;

    /**
     * 
     */
    private int roleID;

    /**
     * 
     */
    private String email;

    // Getters
    public int getUserID() {
        return userID;
    }

    public int getRoleID() {
        return roleID;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}