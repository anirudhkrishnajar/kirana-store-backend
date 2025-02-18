package com.thinkconstructive.book_store.feature_user.service;

import com.thinkconstructive.book_store.feature_user.models.UserDto;
import java.util.List;

public interface UserService {
    UserDto getUser(String userId);
    List<UserDto> getAllUsers();
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUserByUserId(String userId);
}
