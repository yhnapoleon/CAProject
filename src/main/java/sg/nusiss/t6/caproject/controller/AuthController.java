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
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {
        try {
            // 1. 使用 AuthenticationManager 验证用户凭证 (会调用 JwtUserDetailsService)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            // 2. 验证成功后，获取用户详情并生成 JWT token
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String token = jwtTokenUtil.generateToken(userDetails);

            // 3. 获取完整的用户信息
            User user = userService.loadUserByUsername(authenticationRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 4. 构建响应
            LoginResponse response = new LoginResponse();
            response.setSuccess(true);
            response.setMessage("登录成功");
            response.setToken(token);

            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
            userInfo.setUsername(user.getUserName());
            userInfo.setEmail(user.getUserEmail());
            userInfo.setName(user.getUserName()); // 使用用户名作为显示名称
            userInfo.setRole(user.getUserType() == 0 ? "ADMIN" : "USER");
            userInfo.setLoginTime(System.currentTimeMillis());

            response.setUser(userInfo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage("登录失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 用户注册接口：/api/auth/register
     * 注意：这个接口保持向后兼容，但建议使用 /api/register/new 接口
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody LoginRequest registrationRequest) {
        try {
            // 调用 UserService 进行用户注册（使用默认值）
            userService.registerUser(
                registrationRequest.getUsername(), 
                registrationRequest.getUsername(), // 使用用户名作为邮箱
                registrationRequest.getPassword(), 
                registrationRequest.getUsername()  // 使用用户名作为手机号
            );

            // 返回注册成功的消息
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Registration failed");
        }
    }
}