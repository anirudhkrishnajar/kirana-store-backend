package com.thinkconstructive.book_store.config;

import com.thinkconstructive.book_store.feature_user.models.UserDto;
import com.thinkconstructive.book_store.feature_user.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDaoImpl userDaoImpl;

    /**
     * Locates the user based on the username (userId) provided during login.
     *
     * @param userId The username identifying the user whose data is required.
     * @return A UserDetails object containing user information like username, password, and authorities.
     * @throws UsernameNotFoundException If the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDto userDto = userDaoImpl.getUserByUsername(userId);
        if (userDto == null) {
            throw new UsernameNotFoundException("User not found: " + userId);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(userDto.userId()) // using userId as the principal
                .password(userDto.password())
                .authorities("USER")
                .build();
    }
}
