package sg.nusiss.t6.caproject.controller;

import sg.nusiss.t6.caproject.controller.dto.LoginRequest;
import sg.nusiss.t6.caproject.controller.dto.AuthResponse;
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

    private final AuthenticationManager authenticationManager; // 认证管理器，用于验证用户名密码
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    /**
     * 普通用户登录接口：/api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {

        // 1. 使用 AuthenticationManager 验证用户凭证 (会调用 JwtUserDetailsService)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // 2. 验证成功后，获取用户详情并生成 JWT token
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails);

        // 3. 返回包含 Token 的响应
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * 用户注册接口：/api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody LoginRequest registrationRequest) {
        // 调用 UserService 进行用户注册
        userService.registerUser(registrationRequest.getUsername(), registrationRequest.getPassword());

        // 返回注册成功的消息
        return ResponseEntity.ok("User registered successfully");
    }
}