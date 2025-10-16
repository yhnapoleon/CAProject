//By Xu Wenzhe

package sg.nusiss.t6.caproject.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
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

    // Token validity (e.g., 5 hours, in seconds)
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // Load JWT secret from configuration. Configure in application.properties for
    // real projects
    @Value("${jwt.secret:defaultSecretKeyForDevelopment}")
    private String secret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Generate JWT token based on user details
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        //Get authorities and convert to a list of strings (ROLE_ADMIN, ROLE_USER, etc.)
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        //Put roles into claims with key "roles"
        claims.put("roles", roles);

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject) // Token subject, usually the username
                .issuedAt(new Date(System.currentTimeMillis())) // Issued at
                // Set expiration time
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSigningKey()) // Sign with secret key
                .compact(); // Compact to string
    }

    //Validate token: not expired and username matches
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extract username from token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Check whether token is expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Get token expiration time
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extract a claim from token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Get all claims from token
    private Claims getAllClaimsFromToken(String token) {
        // Parse token using secret key
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }
}