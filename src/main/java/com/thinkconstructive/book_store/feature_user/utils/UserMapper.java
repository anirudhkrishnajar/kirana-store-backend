package com.thinkconstructive.book_store.feature_user.utils;

import com.thinkconstructive.book_store.feature_user.models.UserDto;
import com.thinkconstructive.book_store.feature_user.models.User;
public class UserMapper {


    public static UserDto toDto(User user) {
        return new UserDto(
                user.userId(),
                user.password()
        );
    }
    public static User toEntity(UserDto userDto) {
        return new User(
                userDto.userId(),
                userDto.password()
        );
    }
}
