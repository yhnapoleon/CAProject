package sg.nusiss.t6.caproject.controller.dto;

/**
 * 用户登录和注册请求的 DTO。
 */
public class LoginRequest {
    private String username;
    private String password;

    // 必须有默认构造函数
    public LoginRequest() {}

    // Getter 和 Setter (Jackson 库需要它们来反序列化 JSON)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}