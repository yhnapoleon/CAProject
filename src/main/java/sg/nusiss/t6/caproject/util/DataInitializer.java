//By Xu Wenzhe

package sg.nusiss.t6.caproject.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional; // Import Optional

@Component
@Transactional // Ensure DB operations run within a transaction
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        final String ADMIN_USERNAME = "admin";
        final String ADMIN_EMAIL = "admin@caproject.com";
        final String ADMIN_PASSWORD = "password123";

        // 1. Declare Optional to hold the result of findByUsername
        Optional<User> existingUserOptional;

        try {
            // userRepository.findByUsername returns Optional<User>
            existingUserOptional = userRepository.findByUserName(ADMIN_USERNAME);
        } catch (Exception e) {
            // If the query fails, print error and exit
            System.err.println("!!! Database query failed, please check DB connection and schema configuration !!!");
            e.printStackTrace();
            return;
        }

        // 2. Check whether the user does not exist using isEmpty()
        if (existingUserOptional.isEmpty()) {
            System.out.println("--- Attempting to create default admin account ---");

            try {
                User adminUser = new User();
                adminUser.setUserName(ADMIN_USERNAME);
                adminUser.setUserEmail(ADMIN_EMAIL);
                adminUser.setUserPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                adminUser.setUserType(0); // 0=ADMIN
                adminUser.setUserRegisterTime(LocalDateTime.now());
                adminUser.setUserLastLoginTime(LocalDateTime.now());
                adminUser.setUserGender("Unknown"); // Ensure non-nullable fields have values
                adminUser.setUserIntroduce("System Administrator Account"); // Ensure non-nullable fields have values
                adminUser.setUserPhone("0000000000"); // Placeholder phone to satisfy non-null and uniqueness
                                                      // constraints

                userRepository.save(adminUser);

                System.out.println("--- Default admin account saved to database ---");
                System.out.println("Username: " + ADMIN_USERNAME);
                System.out.println("Password: " + ADMIN_PASSWORD);
                System.out.println("---------------------------------------");
            } catch (Exception e) {
                System.err.println("!!! Failed to save admin user, please check User entity fields !!!");
                e.printStackTrace();
            }

        } else {
            // If user already exists, print ID for confirmation
            System.out.println("--- Admin account already exists (ID: " + existingUserOptional.get().getUserId()
                    + "), skipping creation. ---");
        }
    }
}
