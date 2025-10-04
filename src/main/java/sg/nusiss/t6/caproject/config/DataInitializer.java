package sg.nusiss.t6.caproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sg.nusiss.t6.caproject.model.User; // 请替换为您的用户实体路径
import sg.nusiss.t6.caproject.repository.UserRepository; // 请替换为您的用户 Repository 路径

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 假设您的 User 实体有一个构造函数或 set 方法来设置用户名、密码和角色
    // 请确保您的用户实体支持设置这些字段。
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 应用程序启动后立即执行
    @Override
    public void run(String... args) throws Exception {
        // 1. 定义管理员账户信息
        final String ADMIN_USERNAME = "admin";
        final String ADMIN_EMAIL = "admin@caproject.com";
        final String ADMIN_PASSWORD = "password123"; // 生产环境中请使用更复杂的密码

        // 2. 检查 Admin 用户是否存在
        if (userRepository.findByUsername(ADMIN_USERNAME) == null) {

            // 3. 创建 Admin 用户实体
            User adminUser = new User();
            adminUser.setUsername(ADMIN_USERNAME);
            adminUser.setEmail(ADMIN_EMAIL);
            // 密码必须经过加密存储
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));

            // 4. 设置角色为 ADMIN
            // 注意：您可能需要根据您的 User 实体中如何存储角色进行调整
            // 比如，如果角色是 Set<Role> 或 List<String>
            adminUser.setRole(User.Role.ADMIN);
            // 如果您的 User 实体有其他必需字段，请在这里设置。

            // 5. 保存到数据库
            userRepository.save(adminUser);

            System.out.println("--- 默认管理员账户已创建 ---");
            System.out.println("Username: " + ADMIN_USERNAME);
            System.out.println("Password: " + ADMIN_PASSWORD);
            System.out.println("---------------------------");
        }
    }
}