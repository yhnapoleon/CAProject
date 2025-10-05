package sg.nusiss.t6.caproject.service.impl;

import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.model.User.Role;
import sg.nusiss.t6.caproject.repository.UserRepository;
import sg.nusiss.t6.caproject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


// 假设您有一个 UserService 接口，这里是它的实现
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 依赖注入密码编码器，用于加密

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 实现用户注册逻辑。
     * 实际项目中，您还需要处理 UserService 接口的定义，这里我们只关注注册实现。
     */
    // 注意：您需要确保您的 UserService 接口中定义了 registerUser 方法
    // 假设：User registerUser(String username, String password);
    public User registerUser(String username, String password) {
        // 1. 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            // 在实际项目中，应抛出自定义异常
            throw new RuntimeException("Username already taken: " + username);
        }

        User newUser = new User();
        newUser.setUsername(username);
        // 2. 关键步骤：存储密码前必须进行加密
        newUser.setPassword(passwordEncoder.encode(password));
        // 3. 设置默认邮箱（用户名@example.com）
        newUser.setEmail(username + "@example.com");
        // 4. 设置默认个人详情
        newUser.setPersonalDetails("User account created via registration");
        // 5. 默认注册为普通用户
        newUser.setRole(Role.USER);

        return userRepository.save(newUser);
    }

    // (如果您的接口还需要 loadUserByUsername, 也要实现)
    // 示例：
    public java.util.Optional<User> loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}