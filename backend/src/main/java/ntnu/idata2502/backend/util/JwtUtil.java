package ntnu.idata2502.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "z6Z8BuOvUR3Y7iOm0laqFcibgNOWbsOVvhdHJ0g3hIo=";
    private final long EXPIRATION_TIME = 86400000;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Generates a JWT token for a given username.
     *
     * @param username the username to include in the token
     * @return a JWT token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates the JWT token and checks if it has expired.
     *
     * @param token the JWT token to validate
     * @param username the username to validate against the token's subject
     * @return true if the token is valid and matches the username
     */
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }


    /**
     * Extracts the username from a JWT token.
     *
     * @param token the JWT token
     * @return the username in the token's subject
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Checks if the token has expired.
     *
     * @param token the JWT token
     * @return true if the token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token
     * @return the claims in the token
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
