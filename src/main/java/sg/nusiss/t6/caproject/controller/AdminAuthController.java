package sg.nusiss.t6.caproject.controller;

import sg.nusiss.t6.caproject.controller.dto.LoginRequest;
import sg.nusiss.t6.caproject.controller.dto.AuthResponse;
import sg.nusiss.t6.caproject.service.UserService;
import sg.nusiss.t6.caproject.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AdminAuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    /**
     * Admin login endpoint: /api/admin/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAdminAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {

        // 1. Validate user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        // 2. Extra role check: only ADMIN role can log into the admin portal
        // Load raw User entity from UserService and check role
        boolean isAdmin = userService.loadUserByUsername(authenticationRequest.getUsername())
                .map(user -> user.getUserType() == 0) // 0=ADMIN, 1=USER
                .orElse(false);

        if (!isAdmin) {
            // If not admin, return 403 Forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: Only administrators can log in here.");
        }

        // 3. Generate JWT token
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails);

        // 4. Return token
        return ResponseEntity.ok(new AuthResponse(token));
    }
}