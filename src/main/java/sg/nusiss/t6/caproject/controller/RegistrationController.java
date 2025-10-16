package sg.nusiss.t6.caproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.controller.dto.RegistrationRequest;
import sg.nusiss.t6.caproject.controller.dto.RegistrationResponse;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.service.UserService;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * User registration endpoint: /api/register/new
     */
    @PostMapping("/new")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody RegistrationRequest request) {
        try {
            // Call UserService to register the user
            User newUser = userService.registerUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone());

            // Build success response
            RegistrationResponse response = new RegistrationResponse();
            response.setSuccess(true);
            response.setMessage("User registered successfully");

            RegistrationResponse.UserInfo userInfo = new RegistrationResponse.UserInfo();
            userInfo.setUsername(newUser.getUserName());
            userInfo.setEmail(newUser.getUserEmail());
            userInfo.setUserType(newUser.getUserType());
            response.setUser(userInfo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Handle business exceptions (e.g., username already exists)
            RegistrationResponse response = new RegistrationResponse();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Handle other exceptions
            RegistrationResponse response = new RegistrationResponse();
            response.setSuccess(false);
            response.setMessage("Registration failed, please try again later");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
