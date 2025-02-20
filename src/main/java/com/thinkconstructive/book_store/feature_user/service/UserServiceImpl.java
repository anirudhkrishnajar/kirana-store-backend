package com.thinkconstructive.book_store.feature_user.service;

import com.thinkconstructive.book_store.feature_user.UserDaoImpl;
import com.thinkconstructive.book_store.feature_user.models.UserDto;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserDaoImpl userDaoImpl;

    /**
     * Constructor
     *
     * @param userDaoImpl data access object implementation for user operations
     */
    public UserServiceImpl(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    /**
     * Retrieve a user by unique ID
     *
     * @param userId  ID of  user to fetch
     * @return  UserDto object representing  user
     */
    @Override
    public UserDto getUser(String userId) {
        return userDaoImpl.getUser(userId);
    }

    /**
     * Retrieve all users from database
     *
     * @return A list of UserDto objects
     */
    @Override
    public List<UserDto> getAllUsers() {
        return userDaoImpl.getAllUsers();
    }

    /**
     * Create new user and stores it in database
     *
     * @param userDto user details to be created
     * @return  newly created UserDto object
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        return userDaoImpl.createUser(userDto);
    }

    /**
     * Update an existing user information
     *
     * @param userDto  updated user details
     * @return updated UserDto object
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        return userDaoImpl.updateUser(userDto);
    }

    /**
     * Delete a user from database using unique ID
     *
     * @param userId ID of  user to be deleted
     */
    @Override
    public void deleteUserByUserId(String userId) {
        userDaoImpl.deleteUserByUserId(userId);
    }
}
