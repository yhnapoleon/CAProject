//By Xu Wenzhe

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Getter;

/**
 * JWT token response DTO returned after successful login.
 */
@Getter
public class AuthResponse {
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}