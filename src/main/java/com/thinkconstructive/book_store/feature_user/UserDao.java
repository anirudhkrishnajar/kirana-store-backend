package com.thinkconstructive.book_store.feature_user;

import com.thinkconstructive.book_store.feature_user.models.UserDto;

import java.util.List;

public interface UserDao {
    UserDto getUser(String userId);
    List<UserDto> getAllUsers();
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUserByUserId(String userId);


    UserDto getUserByUsername(String userId);
}
