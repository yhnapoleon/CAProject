package sg.nusiss.t6.caproject.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateMe(@RequestBody UserUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        if (request.getUserPhone() != null) {
            user.setUserPhone(request.getUserPhone());
        }
        if (request.getUserEmail() != null) {
            user.setUserEmail(request.getUserEmail());
        }
        if (request.getUserPassword() != null) {
            user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        }
        if (request.getUserLastLoginTime() != null) {
            user.setUserLastLoginTime(request.getUserLastLoginTime());
        }
        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getUserGender() != null) {
            user.setUserGender(request.getUserGender());
        }
        if (request.getUserBirthday() != null) {
            user.setUserBirthday(request.getUserBirthday());
        }
        if (request.getUserIntroduce() != null) {
            user.setUserIntroduce(request.getUserIntroduce());
        }
        if (request.getUserProfileUrl() != null) {
            user.setUserProfileUrl(request.getUserProfileUrl());
        }

        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @Setter
    @Getter
    public static class UserUpdateRequest {
        private String userPhone;
        private String userEmail;
        private String userPassword;
        private java.time.LocalDateTime userLastLoginTime;
        private String userName;
        private String userGender;
        private LocalDate userBirthday;
        private String userIntroduce;
        private String userProfileUrl;
    }

}

