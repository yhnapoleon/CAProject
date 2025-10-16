//By Xu Wenzhe

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for user login and registration requests.
 */
@Setter
@Getter
public class LoginRequest {
    // Getters and setters (required by Jackson for JSON deserialization)
    private String username;
    private String password;

    // Must have a default constructor
    public LoginRequest() {
    }

}