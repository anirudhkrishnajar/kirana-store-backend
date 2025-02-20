package com.thinkconstructive.book_store.feature_user.utils;

import com.thinkconstructive.book_store.feature_user.models.UserDto;
import com.thinkconstructive.book_store.feature_user.models.User;


public class UserMapper {

    /**
     * Convert a user entity to user dto
     *
     * @param user The user entity to be converted
     * @return  UserDto object containing the mapped data
     */
    public static UserDto toDto(User user) {
        return new UserDto(
                user.userId(),
                user.password()
        );
    }

    /**
     * Convert a UserDto to a User entity
     *
     * @param userDto userDto to be converted
     * @return User  entity containing  mapped data
     */
    public static User toEntity(UserDto userDto) {
        return new User(
                userDto.userId(),
                userDto.password()
        );
    }
}
