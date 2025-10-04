package sg.nusiss.t6.caproject.config;

import sg.nusiss.t6.caproject.service.impl.JwtUserDetailsService;
import sg.nusiss.t6.caproject.util.JwtTokenUtil;
// 导入 Jwts 相关的类
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value; // ⬅️ 新增导入，用于注入密钥
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    // 从配置文件中注入密钥，确保与 JwtTokenUtil 中的密钥一致
    @Value("${jwt.secret}")
    private String secret;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 【关键修正】: 针对 public 接口（如登录和注册）跳过 JWT 检查。
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        // 如果路径以 /api/auth/login, /api/auth/register, /api/admin/auth/login 开头，则跳过过滤
        // 请确保这里的路径与 WebSecurityConfig 中的 permitAll() 路径一致
        return path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register") ||
                path.startsWith("/api/admin/auth/login");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 如果 shouldNotFilter 返回 true，该方法将被跳过，请求直接进入下一个过滤器

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // 1. 从请求头中提取 Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            // 尝试从 Token 中解析用户名 (用于加载 UserDetails)
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            }
        }

        // 2. 验证 Token 并设置 Spring Security 上下文
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 从数据库加载用户详情
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // 验证 Token 是否有效
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                // 从 Token 中解析 Claims
                Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();

                // 从 Claims 中读取 "roles" 列表
                List<String> roles = claims.get("roles", List.class);

                // 将字符串角色列表转换为 Spring Security 的 GrantedAuthority 列表
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // 创建认证对象，并将 Authorities 替换为从 Claims 中解析出的列表
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息放入 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}