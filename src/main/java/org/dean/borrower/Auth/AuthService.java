package org.dean.borrower.Auth;

import org.dean.borrower.Auth.dto.JwtService;
import org.dean.borrower.Auth.dto.LoginRequest;
import org.dean.borrower.Auth.dto.LoginResponse;
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
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    //login
    public LoginResponse login(LoginRequest request) {

        //get email
        //check if email exists
        if(userRepository.findByEmail(request.getEmail()).isEmpty()) {
            return null;
        }

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user == null) {
            return null;
        }

        //get user
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!passwordMatches) {
            return null;
        }

        //get token
        String token = jwtService.generateToken(user);

        //return new loginResponse
        return new LoginResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole(),
                user.getEmail(),
                token
        );
    }
}
