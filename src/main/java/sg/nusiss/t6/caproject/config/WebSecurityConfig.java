package sg.nusiss.t6.caproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// 导入新的类：用于获取 AuthenticationManager 的配置
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 安全配置类，适配 Spring Boot 3 / Spring Security 6 的写法。
 */
@Configuration
@EnableWebSecurity // 启用 Spring Security 的 Web 安全功能
@EnableMethodSecurity(prePostEnabled = true) // 启用方法级别的权限注解（如 @PreAuthorize）
public class WebSecurityConfig {

    private final UserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;

    /**
     * 构造函数注入：用户服务、JWT 过滤器、密码加密器
     */
    public WebSecurityConfig(UserDetailsService jwtUserDetailsService,
                             JwtRequestFilter jwtRequestFilter,
                             PasswordEncoder passwordEncoder) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 定义身份验证提供者（AuthenticationProvider），
     * 用于根据用户名加载用户信息，并验证密码。
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * 【修正】暴露 AuthenticationManager Bean，以供登录控制器使用。
     * 在 Spring Security 6 中，推荐通过 AuthenticationConfiguration 来获取它。
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 核心方法：定义安全过滤器链。
     * 替代旧版本的 configure(HttpSecurity http) 方法。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF（因为使用的是基于 Token 的认证）
                .csrf(csrf -> csrf.disable())

                // 设置接口访问权限
                .authorizeHttpRequests(auth -> auth
                        // 登录、注册接口放行（所有用户都可访问）
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/admin/auth/login").permitAll()

                        // 产品浏览接口放行
                        .requestMatchers("/api/products/**").permitAll()

                        // 静态资源放行
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // 页面访问放行
                        .requestMatchers("/", "/login", "/registration", "/admin-login", "/forgot-password", 
                                        "/password-reset-confirmation", "/logout-success", "/easter-egg").permitAll()

                        // 管理后台接口仅允许 ADMIN 角色访问
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                        // 其他所有接口都需要认证后才能访问
                        .anyRequest().authenticated()
                )

                // 设置会话策略为无状态（Stateless），不会使用 Session 保存用户状态
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 设置身份认证提供者
                .authenticationProvider(authenticationProvider());

        // 将自定义的 JWT 过滤器添加到 Spring Security 的过滤器链中
        // 确保在用户名密码认证过滤器之前执行
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 返回配置完成的过滤器链
        return http.build();
    }
}