package org.dean.borrower.Auth.dto;

import org.dean.borrower.entity.User;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //declare secret_key
    private static final String SECRET_KEY =
            "your-super-long-secret-key-that-must-be-at-least-32-bytes";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    //create signing key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }
    //generate token
    public String generateToken(User user) {
        //build payload
        return Jwts.builder()
                //subject
                .subject(user.getEmail())
                //claim
                .claim("role", user.getRole().name())
                //iat
                .issuedAt(new Date())
                //exp
                .expiration(
                        new Date(
                                System.currentTimeMillis() + EXPIRATION_TIME
                        )
                )
                //signingKey
                .signWith(getSigningKey())
                //finalize
                .compact();
    }
}
