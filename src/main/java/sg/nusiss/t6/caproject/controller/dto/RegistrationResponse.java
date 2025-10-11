package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegistrationResponse {
    private boolean success;
    private String message;
    private UserInfo user;

    @Data
    @Getter
    @Setter
    public static class UserInfo {
        private String username;
        private String email;
        private Integer userType;
    }
}
