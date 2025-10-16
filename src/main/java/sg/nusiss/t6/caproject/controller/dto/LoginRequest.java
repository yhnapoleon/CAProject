package sg.nusiss.t6.caproject.controller.dto;

/**
 * DTO for user login and registration requests.
 */
public class LoginRequest {
    private String username;
    private String password;

    // Must have a default constructor
    public LoginRequest() {
    }

    // Getters and setters (required by Jackson for JSON deserialization)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}