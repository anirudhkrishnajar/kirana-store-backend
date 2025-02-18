package com.thinkconstructive.book_store.feature_user.controller;

import com.thinkconstructive.book_store.config.JwtUtil;
import com.thinkconstructive.book_store.config.CustomUserDetailsService;
import com.thinkconstructive.book_store.feature_user.models.UserDto;
import com.thinkconstructive.book_store.feature_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/kirana/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor to initialize the UserController with the necessary service
     *
     * @param userService service responsible for user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieve a user by user ID
     *
     * @param userId  ID of user to be fetched
     * @return  ResponseEntity containing UserDto and a status of OK
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUser(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Retrieve all users from system
     *
     * @return  ResponseEntity containing a list of all UserDto objects and a status  OK
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Create a new user with the provided details
     * @param userDto  UserDto object containing the user's details
     * @return  ResponseEntity containing  JwtAuthResponse
     */
    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {

        String encodedPassword = passwordEncoder.encode(userDto.password());
        UserDto userToSave = new UserDto(userDto.userId(), encodedPassword);

        UserDto createdUser = userService.createUser(userToSave);


        UserDetails userDetails = customUserDetailsService.loadUserByUsername(createdUser.userId());
        String token = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new JwtAuthResponse(createdUser, token), HttpStatus.CREATED);
    }

    /**
     * Update an existing user with new details
     *
     * @param userDto  UserDto object containing  updated user details
     * @return ResponseEntity containing updated UserDto and a status  OK
     */
    @PutMapping("/")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Delete a user by their user ID
     *
     * @param userId  ID of user to be deleted
     * @return ResponseEntity with a success message and a status  OK
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUserByUserId(userId);
        return new ResponseEntity<>("User deleted successfully: " + userId, HttpStatus.OK);
    }
}

class JwtAuthResponse {
    private final UserDto user;
    private final String token;

    public JwtAuthResponse(UserDto user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
