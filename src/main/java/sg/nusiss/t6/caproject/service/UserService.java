package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.model.User;
import java.util.Optional;

/**
 * 用户服务接口。定义了用户模块的业务操作。
 */
public interface UserService {

    /**
     * 注册新用户。
     * * @param username 用户名
     * @param password 用户的明文密码（Impl 中会进行加密）
     * @return 注册成功的 User 实体
     */
    User registerUser(String username, String password);

    /**
     * 根据用户名加载用户（供管理员登录等逻辑使用）。
     * * @param username 用户名
     * @return 包含 User 实体的 Optional 对象
     */
    Optional<User> loadUserByUsername(String username);
}