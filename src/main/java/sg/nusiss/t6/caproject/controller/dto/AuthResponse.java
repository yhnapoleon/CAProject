package sg.nusiss.t6.caproject.controller.dto;

/**
 * JWT token response DTO returned after successful login.
 */
public class AuthResponse {
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}