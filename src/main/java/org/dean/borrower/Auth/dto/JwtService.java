package org.dean.borrower.Auth.dto;

import org.dean.borrower.entity.User;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

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

    //parse token
    public Claims parseToken(String token) {
        return Jwts.parser()
                // Verify the JWT signature using our signing key.
                // If verification FAILS, parsing throws an exception.
                .verifyWith(getSigningKey())
                //finish configure
                .build()
                //parse the token as a signed jwt containing claims
                .parseSignedClaims(token)
                //return claims gathered
                .getPayload();
    }

    //get subject
    public String extractEmail(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();

    }

    //is token expired
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);

        return claims.getExpiration().before(new Date());
    }

    //is token valid
    public boolean isTokenValid(String token, User user) {

        //get token subject
        String email = extractEmail(token);
        //get user email
        return email.equals(user.getEmail()) && !isTokenExpired(token);

    }
}
