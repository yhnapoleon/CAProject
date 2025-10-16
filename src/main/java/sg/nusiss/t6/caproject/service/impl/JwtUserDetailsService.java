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
 * Implements UserDetailsService for Spring Security to load user info from DB.
 * This is the first step in the login authentication flow.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by username. Key method used by Spring Security for authentication.
     * 
     * @param username username submitted at login
     * @return UserDetails containing encrypted password and roles
     * @throws UsernameNotFoundException if user does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Find user entity from database
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Convert User entity to Spring Security's UserDetails
        // Create authorities; note: roles must start with "ROLE_"
        String role = user.getUserType() == 0 ? "ADMIN" : "USER";
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role));

        // 3. Return Spring Security internal UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(), // username
                user.getUserPassword(), // encrypted password stored in DB
                authorities // authorities (ROLE_USER or ROLE_ADMIN)
        );
    }
}