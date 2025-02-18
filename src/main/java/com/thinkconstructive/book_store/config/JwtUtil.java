package com.thinkconstructive.book_store.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtil {

    // IMPORTANT: Change this secret for production!
    private final String SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEYEXAMPLEKEY";

    /**
     * Extracts the username (subject) from the given JWT token
     *
     * @param token  JWT token
     * @return The username   embedded in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token
     *
     * @param token  JWT token
     * @return The expiration date of   token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the token
     *
     * @param token The JWT token
     * @param claimsResolver Function to extract a specific claim
     * @param <T> The type of claim to be extracted
     * @return The extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token
     *
     * @param token The JWT token
     * @return The claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the given JWT token has expired
     *
     * @param token The JWT token
     * @return true if the token has expired, false if not expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the given user details
     *
     * @param userDetails The user details used to generate the token
     * @return A signed JWT token valid for an hr
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Token valid for 1 hour
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Validates the JWT token against the provided user details
     *
     * @param token  JWT token.
     * @param userDetails The user details
     * @return true if the token is valid and not expired, otherwise it returns false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
