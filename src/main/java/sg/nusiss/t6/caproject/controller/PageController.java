package sg.nusiss.t6.caproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("/component1")
    public String component1() {
        return "registration"; // Redirect old URL to new registration page
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin-login";
    }

    @GetMapping("/personal-info")
    public String personalInfo() {
        return "personal-info";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/password-reset-confirmation")
    public String passwordResetConfirmation() {
        return "password-reset-confirmation";
    }

    @GetMapping("/")
    public String home() {
        return "registration"; // Default to registration page
    }
}