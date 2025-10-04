package sg.nusiss.t6.caproject.controller;

import sg.nusiss.t6.caproject.controller.dto.LoginRequest;
import sg.nusiss.t6.caproject.controller.dto.AuthResponse;
import sg.nusiss.t6.caproject.model.User.Role;
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

    public AdminAuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    /**
     * 管理员登录接口：/api/admin/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAdminAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {

        // 1. 验证用户凭证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // 2. 额外检查角色：只有 ADMIN 角色才能登录后台
        // 从 UserService 加载原始 User 实体，检查角色
        boolean isAdmin = userService.loadUserByUsername(authenticationRequest.getUsername())
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);

        if (!isAdmin) {
            // 如果不是管理员，返回 403 Forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Only administrators can log in here.");
        }

        // 3. 生成 JWT token
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails);

        // 4. 返回 token
        return ResponseEntity.ok(new AuthResponse(token));
    }
}