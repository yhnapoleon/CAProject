package sg.nusiss.t6.caproject.service.impl;

import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.User;
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
    public User registerUser(String username, String email, String password, String phone) {
        // 1. 检查用户名是否已存在
        if (userRepository.existsByUserName(username)) {
            throw new RuntimeException("Username already taken: " + username);
        }

        // 2. 检查邮箱是否已存在
        if (userRepository.existsByUserEmail(email)) {
            throw new RuntimeException("Email already taken: " + email);
        }

        // 3. 检查手机号是否已存在
        if (userRepository.existsByUserPhone(phone)) {
            throw new RuntimeException("Phone number already taken: " + phone);
        }

        User newUser = new User();
        newUser.setUserName(username);
        // 4. 关键步骤：存储密码前必须进行加密
        newUser.setUserPassword(passwordEncoder.encode(password));
        // 5. 设置邮箱
        newUser.setUserEmail(email);
        // 6. 设置手机号
        newUser.setUserPhone(phone);
        // 7. 设置默认个人详情
        newUser.setUserIntroduce("User account created via registration");
        // 8. 默认注册为普通用户 (1=用户, 0=管理员)
        newUser.setUserType(1);
        newUser.setUserRegisterTime(java.time.LocalDateTime.now());
        newUser.setUserLastLoginTime(java.time.LocalDateTime.now());
        newUser.setUserGender("Unknown");
        // 9. 设置默认生日
        newUser.setUserBirthday(java.time.LocalDate.now());
        // 10. 设置默认钱包余额
        newUser.setWallet(java.math.BigDecimal.ZERO);

        return userRepository.save(newUser);
    }

    // (如果您的接口还需要 loadUserByUsername, 也要实现)
    // 示例：
    public java.util.Optional<User> loadUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }
}