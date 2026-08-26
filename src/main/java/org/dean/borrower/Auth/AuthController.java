package org.dean.borrower.Auth;

import jakarta.validation.Valid;
import org.dean.borrower.Auth.dto.LoginRequest;
import org.dean.borrower.Auth.dto.LoginResponse;
import org.dean.borrower.Auth.dto.SignupRequest;
import org.dean.borrower.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid SignupRequest request) {
        UserResponse response = authService.signUp(request);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);

        if(response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }
}
