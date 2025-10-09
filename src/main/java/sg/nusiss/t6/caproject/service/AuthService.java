package sg.nusiss.t6.caproject.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    // 模拟用户数据库 - 在实际应用中应该连接到真实的数据库
    private static final Map<String, String> users = new HashMap<>();
    
    static {
        // 添加一些测试用户
        users.put("admin", "admin123");
        users.put("user", "user123");
        users.put("tina", "tina123");
        users.put("test@example.com", "password123");
        users.put("demo", "demo123");
        users.put("john", "john123");
    }

    /**
     * 验证用户登录
     * @param username 用户名或邮箱
     * @param password 密码
     * @return 验证结果
     */
    public boolean validateUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        String storedPassword = users.get(username.toLowerCase());
        return storedPassword != null && storedPassword.equals(password);
    }

    /**
     * 获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", username);
        
        if (username.contains("@")) {
            userInfo.put("email", username);
            userInfo.put("name", extractNameFromEmail(username));
        } else {
            userInfo.put("email", username + "@example.com");
            userInfo.put("name", capitalizeFirst(username));
        }
        
        userInfo.put("role", "USER");
        userInfo.put("loginTime", System.currentTimeMillis());
        
        return userInfo;
    }

    /**
     * 检查用户是否存在
     * @param username 用户名
     * @return 是否存在
     */
    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    /**
     * 添加新用户（用于测试）
     * @param username 用户名
     * @param password 密码
     * @return 是否添加成功
     */
    public boolean addUser(String username, String password) {
        if (username == null || password == null || users.containsKey(username.toLowerCase())) {
            return false;
        }
        
        users.put(username.toLowerCase(), password);
        return true;
    }

    private String extractNameFromEmail(String email) {
        String name = email.substring(0, email.indexOf('@'));
        return capitalizeFirst(name);
    }

    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
