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
     * 用户注册接口：/api/register/new
     */
    @PostMapping("/new")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody RegistrationRequest request) {
        try {
            // 调用 UserService 进行用户注册
            User newUser = userService.registerUser(
                request.getUsername(), 
                request.getEmail(), 
                request.getPassword(), 
                request.getPhone()
            );

            // 构建成功响应
            RegistrationResponse response = new RegistrationResponse();
            response.setSuccess(true);
            response.setMessage("用户注册成功");
            
            RegistrationResponse.UserInfo userInfo = new RegistrationResponse.UserInfo();
            userInfo.setUsername(newUser.getUserName());
            userInfo.setEmail(newUser.getUserEmail());
            userInfo.setUserType(newUser.getUserType());
            response.setUser(userInfo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 处理业务异常（如用户名已存在等）
            RegistrationResponse response = new RegistrationResponse();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // 处理其他异常
            RegistrationResponse response = new RegistrationResponse();
            response.setSuccess(false);
            response.setMessage("注册失败，请稍后重试");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
