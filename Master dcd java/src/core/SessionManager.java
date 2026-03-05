package core;

public class SessionManager {

    private static SessionManager instance;

    private int userId;
    private String username;
    private String role;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setSession(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void clearSession() {
        userId = 0;
        username = null;
        role = null;
    }
}

