package sg.nusiss.t6.caproject.service.impl;

import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 实现了 UserDetailsService 接口，用于 Spring Security 框架从数据库加载用户信息。
 * 它是登录认证流程的第一步。
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 根据用户名加载用户。这是 Spring Security 进行身份验证时的关键方法。
     * @param username 用户在登录时提交的用户名
     * @return 包含用户详细信息（如加密密码、角色）的 UserDetails 对象
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查找用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. 将 User 实体转换为 Spring Security 要求的 UserDetails
        // 创建权限列表。注意：权限/角色必须以 "ROLE_" 开头
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        // 3. 返回 Spring Security 内部的 UserDetails 对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),      // 用户名
                user.getPassword(),      // 数据库中存储的加密后的密码
                authorities              // 用户权限（ROLE_USER 或 ROLE_ADMIN）
        );
    }
}