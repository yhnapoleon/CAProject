package sg.nusiss.t6.caproject.controller.dto;

/**
 * 登录成功后返回的 JWT Token 响应 DTO。
 */
public class AuthResponse {
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}