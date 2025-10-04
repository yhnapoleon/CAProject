package sg.nusiss.t6.caproject.config;

import sg.nusiss.t6.caproject.service.impl.JwtUserDetailsService;
import sg.nusiss.t6.caproject.util.JwtTokenUtil;
// 请确保已添加 io.jsonwebtoken 依赖
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 核心过滤方法：对每一个进来的 HTTP 请求进行 Token 检查和用户认证。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // 1. 从请求头中提取 Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); // "Bearer " 后面的字符串才是 Token
            try {
                // 尝试从 Token 中解析用户名
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            }
        }

        // 2. 验证 Token 并设置 Spring Security 上下文
        // 只有当用户名存在且安全上下文（SecurityContext）中没有认证信息时才进行认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 使用 UserDetailsService 加载用户详情
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // 验证 Token 是否有效
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                // 创建认证对象
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息放入 SecurityContext，表示当前请求已被认证
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // 继续执行过滤器链中的下一个过滤器或目标 Controller
        chain.doFilter(request, response);
    }
}