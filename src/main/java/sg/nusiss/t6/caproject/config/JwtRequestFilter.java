package sg.nusiss.t6.caproject.config;

import sg.nusiss.t6.caproject.service.impl.JwtUserDetailsService;
import sg.nusiss.t6.caproject.util.JwtTokenUtil;
// 导入 Jwts 相关的类
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys; // ⬅️ 新增导入：用于密钥工具类
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.PostConstruct; // ⬅️ 新增导入：用于密钥初始化
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key; // ⬅️ 新增导入
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT 请求过滤器：拦截除登录/注册外的所有请求，验证 JWT 并设置 Spring Security 上下文。
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    // 从配置文件中注入 Base64 密钥字符串
    @Value("${jwt.secret}")
    private String secretString;

    // 用于存储转换后的 Key 对象
    private Key signingKey;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 初始化：将 Base64 编码的密钥字符串转换为 Key 对象
     */
    @PostConstruct
    public void init() {
        // 使用 Keys.hmacShaKeyFor 方法将 Base64 字符串安全地转换为 Key 对象
        // 确保你的 jwt.secret 是一个 Base64 编码的字符串
        this.signingKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    /**
     * 针对 public 接口（如登录和注册）跳过 JWT 检查。
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        // 如果路径以 /api/auth/login, /api/auth/register, /api/register,
        // /api/admin/auth/login 开头，则跳过过滤
        return path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register") ||
                path.startsWith("/api/register") ||
                path.startsWith("/api/admin/auth/login") ||
                // Swagger/OpenAPI 文档与 UI 放行，避免无 token 访问被拦截
                path.equals("/swagger-ui.html") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

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

                // 【关键修正区域开始】: 适应 JJWT 0.12.x 新 API
                try {
                    // 使用 verifyWith(Key) 替换 setSigningKey(secret)
                    // 并且必须在 parse 之前调用 build()
                    Claims claims = Jwts.parser()
                            .verifyWith((SecretKey) this.signingKey) // ⬅️ 修正点 1: 使用 Key 对象和 verifyWith
                            .build() // ⬅️ 修正点 2: 必须调用 build()
                            .parseSignedClaims(jwtToken)
                            .getPayload();

                    // 【关键修正区域结束】

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

                } catch (Exception e) {
                    logger.error("JWT Token parsing failed or is invalid", e);
                }
            }
        }

        chain.doFilter(request, response);
    }
}