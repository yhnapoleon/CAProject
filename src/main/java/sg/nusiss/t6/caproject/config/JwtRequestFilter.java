//By Xu Wenzhe

package sg.nusiss.t6.caproject.config;

import sg.nusiss.t6.caproject.service.impl.JwtUserDetailsService;
import sg.nusiss.t6.caproject.util.JwtTokenUtil;
// Import JJWT related classes
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys; // Newly added: key utilities
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.PostConstruct; // Newly added: for key initialization
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key; // Newly added
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT request filter: intercepts all requests except login/register, validates
 * the JWT,
 * and sets the Spring Security context.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    // Inject Base64-encoded secret string from configuration
    @Value("${jwt.secret}")
    private String secretString;

    // Store the converted Key object
    private Key signingKey;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Initialize: convert Base64-encoded secret string into a Key object.
     */
    @PostConstruct
    public void init() {
        // Use Keys.hmacShaKeyFor to safely convert Base64 string to a Key object
        // Ensure jwt.secret is a Base64-encoded string
        this.signingKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    /**
     * Skip JWT checks for public endpoints (e.g., login and register).
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();

        // Skip filtering if the path starts with /api/auth/login, /api/auth/register,
        // /api/register,
        // or /api/admin/auth/login
        return path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register") ||
                path.startsWith("/api/register") ||
                path.startsWith("/api/admin/auth/login") ||
                // Allow static image and avatar resources
                path.startsWith("/images/") ||
                path.startsWith("/avatars/") ||
                // Allow Swagger/OpenAPI docs and UI to avoid blocking access without token
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

        // 1. Extract token from the Authorization header
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            // Try to parse username from token (used to load UserDetails)
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            }
        }

        // 2. Validate token and set Spring Security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from database
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                // Adjustments for JJWT 0.12.x API
                try {
                    // Use verifyWith(Key) instead of setSigningKey(secret)
                    // and call build() before parse
                    Claims claims = Jwts.parser()
                            .verifyWith((SecretKey) this.signingKey)
                            .build()
                            .parseSignedClaims(jwtToken)
                            .getPayload();

                    // Extract "roles" list from claims
                    List<String> roles = claims.get("roles", List.class);

                    // Convert role strings to Spring Security GrantedAuthority list
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // Create authentication token with authorities parsed from claims
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Put authentication into SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                } catch (Exception e) {
                    logger.error("JWT Token parsing failed or is invalid", e);
                }
            }
        }

        chain.doFilter(request, response);
    }
}