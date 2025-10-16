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
import java.time.LocalDateTime;
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
            // 手机号唯一性校验
            Optional<User> phoneOwner = userRepository.findByUserPhone(request.getUserPhone());
            if (phoneOwner.isPresent() && !phoneOwner.get().getUserId().equals(user.getUserId())) {
                return ResponseEntity.badRequest().build();
            }
            user.setUserPhone(request.getUserPhone());
        }
        if (request.getUserEmail() != null) {
            // 邮箱唯一性校验
            Optional<User> emailOwner = userRepository.findByUserEmail(request.getUserEmail());
            if (emailOwner.isPresent() && !emailOwner.get().getUserId().equals(user.getUserId())) {
                return ResponseEntity.badRequest().build();
            }
            user.setUserEmail(request.getUserEmail());
        }
        if (request.getUserPassword() != null) {
            user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        }
        if (request.getUserLastLoginTime() != null) {
            user.setUserLastLoginTime(request.getUserLastLoginTime());
        }
        if (request.getUserName() != null) {
            // 用户名唯一性校验
            Optional<User> nameOwner = userRepository.findByUserName(request.getUserName());
            if (nameOwner.isPresent() && !nameOwner.get().getUserId().equals(user.getUserId())) {
                return ResponseEntity.badRequest().build();
            }
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

    @PutMapping("/me/avatar")
    public ResponseEntity<User> updateAvatar(@RequestBody AvatarUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        String resolvedUrl = null;
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isBlank()) {
            resolvedUrl = request.getAvatarUrl();
        } else if (request.getFilename() != null && !request.getFilename().isBlank()) {
            // 允许前端只提交文件名，后端按静态资源映射规则拼接访问路径，使用 /avatars/ 前缀
            String fn = request.getFilename();
            if (fn.startsWith("/")) {
                fn = fn.substring(1);
            }
            resolvedUrl = "/avatars/" + fn;
        }

        if (resolvedUrl == null || resolvedUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        user.setUserProfileUrl(resolvedUrl);
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

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public LocalDateTime getUserLastLoginTime() {
            return userLastLoginTime;
        }

        public void setUserLastLoginTime(LocalDateTime userLastLoginTime) {
            this.userLastLoginTime = userLastLoginTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public LocalDate getUserBirthday() {
            return userBirthday;
        }

        public void setUserBirthday(LocalDate userBirthday) {
            this.userBirthday = userBirthday;
        }

        public String getUserIntroduce() {
            return userIntroduce;
        }

        public void setUserIntroduce(String userIntroduce) {
            this.userIntroduce = userIntroduce;
        }

        public String getUserProfileUrl() {
            return userProfileUrl;
        }

        public void setUserProfileUrl(String userProfileUrl) {
            this.userProfileUrl = userProfileUrl;
        }
    }

    @Setter
    @Getter
    public static class AvatarUpdateRequest {
        private String avatarUrl; // 直接传完整 URL（CDN/对象存储/静态资源）
        private String filename;  // 或仅传后端静态目录下的文件名，例如 avatars/avatar_01.png

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }

}

