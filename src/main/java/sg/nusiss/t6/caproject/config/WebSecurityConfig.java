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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 核心方法：定义安全过滤器链。
     * 替代旧版本的 configure(HttpSecurity http) 方法。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF
                .csrf(csrf -> csrf.disable())

		// 放行公开接口与文档；仅后台其余接口要求 ADMIN 角色
		.authorizeHttpRequests(auth -> auth
				.requestMatchers(
						"/api/auth/**",
						"/api/admin/auth/login",
						"/swagger-ui.html",
						"/swagger-ui/**",
						"/v3/api-docs/**"
				).permitAll()
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll()
		)

                // 设置会话策略为无状态
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 设置身份认证提供者
                .authenticationProvider(authenticationProvider());

        // 添加 JWT 过滤器
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 返回配置完成的过滤器链
        return http.build();
    }
}