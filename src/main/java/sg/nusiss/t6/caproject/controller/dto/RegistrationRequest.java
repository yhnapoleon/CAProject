package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
}
