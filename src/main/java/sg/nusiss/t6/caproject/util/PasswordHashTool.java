//prestore the password for user and admin
package sg.nusiss.t6.caproject.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTool {
    public static void main(String[] args) {
        String raw = "adminpass"; // Replace this with the plaintext password you want to hash
        System.out.println(new BCryptPasswordEncoder().encode(raw));
    }
}