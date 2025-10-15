package sg.nusiss.t6.caproject.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTool {
    public static void main(String[] args) {
        String raw = "adminpass"; // 把这里换成你想要的明文口令
        System.out.println(new BCryptPasswordEncoder().encode(raw));
    }
}