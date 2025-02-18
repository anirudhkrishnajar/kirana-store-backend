package com.thinkconstructive.book_store.feature_user.service;

import com.thinkconstructive.book_store.feature_user.UserDaoImpl;
import com.thinkconstructive.book_store.feature_user.models.UserDto;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserDaoImpl userDaoImpl;

    public UserServiceImpl(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }


    @Override
    public UserDto getUser(String userId) {
        return userDaoImpl.getUser(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDaoImpl.getAllUsers();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return userDaoImpl.createUser(userDto);
    }


    @Override
    public UserDto updateUser(UserDto userDto) {
        return userDaoImpl.updateUser(userDto);
    }


    @Override
    public void deleteUserByUserId(String userId) {
        userDaoImpl.deleteUserByUserId(userId);
    }
}
