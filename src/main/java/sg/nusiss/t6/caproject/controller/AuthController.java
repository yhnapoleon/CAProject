package sg.nusiss.t6.caproject.controller;

import sg.nusiss.t6.caproject.controller.dto.LoginRequest;
import sg.nusiss.t6.caproject.controller.dto.LoginResponse;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.service.UserService;
import sg.nusiss.t6.caproject.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager; // Authentication manager for validating
                                                               // username/password
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    /**
     * Standard user login endpoint: /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {
        try {
            // 1. Validate user credentials using AuthenticationManager (invokes
            // JwtUserDetailsService)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            // 2. On success, get user details and generate JWT token
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String token = jwtTokenUtil.generateToken(userDetails);

            // 3. Fetch full user information
            User user = userService.loadUserByUsername(authenticationRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 4. Build response
            LoginResponse response = new LoginResponse();
            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setToken(token);

            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
            userInfo.setUsername(user.getUserName());
            userInfo.setEmail(user.getUserEmail());
            userInfo.setName(user.getUserName()); // Use username as display name
            userInfo.setRole(user.getUserType() == 0 ? "ADMIN" : "USER");
            userInfo.setLoginTime(System.currentTimeMillis());

            response.setUser(userInfo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage("Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * User registration endpoint: /api/auth/register
     * Note: Kept for backward compatibility; prefer /api/register/new
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody LoginRequest registrationRequest) {
        try {
            // Call UserService to register user (using defaults)
            userService.registerUser(
                    registrationRequest.getUsername(),
                    registrationRequest.getUsername(), // Use username as email
                    registrationRequest.getPassword(),
                    registrationRequest.getUsername() // Use username as phone
            );

            // Return success message
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Registration failed");
        }
    }
}