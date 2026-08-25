package org.dean.borrower.Auth;

import org.dean.borrower.Auth.dto.SignupRequest;
import org.dean.borrower.dto.UserResponse;
import org.dean.borrower.entity.User;
import org.dean.borrower.enums.Role;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    //toresponse

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getLastname(),
                user.getFirstname(),
                user.getRole(),
                user.getEmail()
        );
    }
    //hash password

    //create
    public UserResponse signUp(SignupRequest request) {

        //check if email exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return null;
        }

        User newUser = new User();

        newUser.setFirstname(request.getFirstName());
        newUser.setLastname(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setRole(Role.BORROWER);

        //password hashing
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(hashedPassword);

        User user = userRepository.save(newUser);
        return toResponse(user);
    }

}
