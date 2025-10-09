package sg.nusiss.t6.caproject.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional; // 引入 Optional

// @Component  // 暂时禁用以避免启动时的数据库操作
@Transactional // 确保数据库操作在事务中执行
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        final String ADMIN_USERNAME = "admin";
        final String ADMIN_EMAIL = "admin@caproject.com";
        final String ADMIN_PASSWORD = "password123";

        // 1. 声明 Optional 变量来接收 findByUsername 的结果
        Optional<User> existingUserOptional;

        try {
            // userRepository.findByUsername 返回 Optional<User>
            existingUserOptional = userRepository.findByUserName(ADMIN_USERNAME);
        } catch (Exception e) {
            // 如果查询本身失败，打印错误并退出
            System.err.println("!!! 数据库查询失败，请检查数据库连接和表结构配置 !!!");
            e.printStackTrace();
            return;
        }

        // 2. 修正后的检查：使用 isEmpty() 判断用户是否不存在
        if (existingUserOptional.isEmpty()) {
            System.out.println("--- 尝试创建默认管理员账户 ---");

            try {
                User adminUser = new User();
                adminUser.setUserName(ADMIN_USERNAME);
                adminUser.setUserEmail(ADMIN_EMAIL);
                adminUser.setUserPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                adminUser.setUserType(0); // 0=管理员
                adminUser.setUserRegisterTime(LocalDateTime.now());
                adminUser.setUserLastLoginTime(LocalDateTime.now());
                adminUser.setUserGender("Unknown"); // 确保非空字段有值
                adminUser.setUserIntroduce("System Administrator Account"); // 确保非空字段有值
                adminUser.setUserPhone("0000000000"); // 占位手机号，满足非空与唯一约束

                userRepository.save(adminUser);

                System.out.println("--- 默认管理员账户已成功保存至数据库 ---");
                System.out.println("Username: " + ADMIN_USERNAME);
                System.out.println("Password: " + ADMIN_PASSWORD);
                System.out.println("---------------------------------------");
            } catch (Exception e) {
                System.err.println("!!! 管理员用户保存失败，请检查 User 实体字段 !!!");
                e.printStackTrace();
            }

        } else {
            // 如果用户已存在，打印 ID 以供确认
            System.out.println("--- 管理员账户已存在 (ID: " + existingUserOptional.get().getUserId() + ")，跳过创建。 ---");
        }
    }
}
