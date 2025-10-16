package sg.nusiss.t6.caproject.service.impl;

import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.UserRepository;
import sg.nusiss.t6.caproject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Assume there is a UserService interface; this is its implementation
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // DI for password encoder used for hashing

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Implement user registration logic.
     * In real projects, ensure the UserService interface defines this method; here
     * we focus on registration.
     */
    // Note: Ensure your UserService interface defines registerUser
    public User registerUser(String username, String email, String password, String phone) {
        // 1. Check if username already exists
        if (userRepository.existsByUserName(username)) {
            throw new RuntimeException("Username already taken: " + username);
        }

        // 2. Check if email already exists
        if (userRepository.existsByUserEmail(email)) {
            throw new RuntimeException("Email already taken: " + email);
        }

        // 3. Check if phone already exists
        if (userRepository.existsByUserPhone(phone)) {
            throw new RuntimeException("Phone number already taken: " + phone);
        }

        User newUser = new User();
        newUser.setUserName(username);
        // 4. Critical: hash password before storing
        newUser.setUserPassword(passwordEncoder.encode(password));
        // 5. Set email
        newUser.setUserEmail(email);
        // 6. Set phone number
        newUser.setUserPhone(phone);
        // 7. Set default personal details
        newUser.setUserIntroduce("User account created via registration");
        // 8. Default to normal user (1=USER, 0=ADMIN)
        newUser.setUserType(1);
        newUser.setUserRegisterTime(java.time.LocalDateTime.now());
        newUser.setUserLastLoginTime(java.time.LocalDateTime.now());
        newUser.setUserGender("Unknown");
        // 9. Set default birthday
        newUser.setUserBirthday(java.time.LocalDate.now());
        // 10. Set default wallet balance
        newUser.setWallet(java.math.BigDecimal.ZERO);

        return userRepository.save(newUser);
    }

    // (If your interface also needs loadUserByUsername, implement it as well)
    // Example:
    public java.util.Optional<User> loadUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }
}