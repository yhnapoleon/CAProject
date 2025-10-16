//By Xu Wenzhe

package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.model.User;
import java.util.Optional;

//User registration and logging in
public interface UserService {
    //Register new user
    User registerUser(String username, String email, String password, String phone);

    //Load users by username
    Optional<User> loadUserByUsername(String username);
}