package com.thinkconstructive.book_store.feature_auth.controller;

import com.thinkconstructive.book_store.feature_auth.models.AuthResponse;
import com.thinkconstructive.book_store.feature_auth.models.AuthRequest;
import com.thinkconstructive.book_store.config.JwtUtil;
import com.thinkconstructive.book_store.config.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kirana/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Handles user login by authenticating the credentials
     *
     * @param authRequest  authentication request containing userId and password.
     * @return ResponseEntity containing the generated token, success message, and userId.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserId(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect userId or password");
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUserId());
        final String token = jwtUtil.generateToken(userDetails);


        return ResponseEntity.ok(new AuthResponse(token, "Login successful", authRequest.getUserId()));
    }

    /**
     * Handles user logout by instructing the client to discard the JWT token.
     *
     * @return A ResponseEntity containing a logout success message.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        return ResponseEntity.ok("Logout successful. Discard your token on the client side.");
    }
}
