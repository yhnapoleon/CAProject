package sg.nusiss.t6.caproject.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {

    // Token的有效期（例如 5 小时，单位秒）
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // 从配置文件加载 JWT 密钥。实际项目中需配置在 application.properties
    @Value("${jwt.secret:defaultSecretKeyForDevelopment}")
    private String secret;

    // --- 1. 生成 Token ---
    /**
     * 根据用户详情生成 JWT Token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // 1. 获取用户权限列表，并转换为字符串列表 (ROLE_ADMIN, ROLE_USER 等)
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 2. 将权限列表以 "roles" 为键名添加到 Claims 中
        claims.put("roles", roles);

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // Token 的主题，通常是用户名
                .setIssuedAt(new Date(System.currentTimeMillis())) // 签发时间
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret) // 使用 HS512 算法和密钥签名
                .compact(); // 压缩成字符串
    }

    // --- 2. 验证 Token ---
    /**
     * 验证 Token 是否有效：未过期且用户名匹配
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // --- 3. 解析 Token 辅助方法 ---

    // 从 token 获取用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 检查 token 是否过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 从 token 获取过期时间
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 从 token 中提取 Claim
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 获取 token 中的所有 Claims
    private Claims getAllClaimsFromToken(String token) {
        // 使用密钥解析 Token
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}