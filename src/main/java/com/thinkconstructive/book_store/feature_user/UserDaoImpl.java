package com.thinkconstructive.book_store.feature_user;

import com.thinkconstructive.book_store.feature_user.models.UserDto;
import com.thinkconstructive.book_store.feature_user.models.User;
import com.thinkconstructive.book_store.feature_user.repo.UserRepository;
import com.thinkconstructive.book_store.feature_user.utils.UserMapper;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    /**
     * Constructor for UserDaoImpl
     *
     * @param userRepository  repository to interact with  User collection
     */
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Fetches a User by ID and maps it to a UserDto object
     *
     * @param userId  ID of the user to fetch
     * @return  UserDto object representing user
     */
    @Override
    public UserDto getUser(String userId) {
        User user = userRepository.findUserByUserId(userId);
        return UserMapper.toDto(user);
    }

    /**
     * Retrieve  list of all users from the database
     *
     * @return  list of UserDto objects representing all users
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            userDtoList.add(UserMapper.toDto(user));
        }
        return userDtoList;
    }

    /**
     * Create a new user in the database
     *
     * @param userDto Dto for user
     * @return  created UserDto
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.insert(UserMapper.toEntity(userDto));
        return UserMapper.toDto(user);
    }

    /**
     * Update an existing user
     *
     * @param userDto User dto
     * @return  updated UserDto object after saving  changes
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toEntity(userDto));
        return UserMapper.toDto(user);
    }

    /**
     * Delete a user from  database by user ID
     *
     * @param userId  ID of  user to delete
     */
    @Override
    public void deleteUserByUserId(String userId) {
        userRepository.deleteUserByUserId(userId);
    }

    /**
     * Fetch a User by their username
     *
     * @param userId  username
     * @return  UserDto  the user
     */
    @Override
    public UserDto getUserByUsername(String userId) {
        User user = userRepository.findUserByUserId(userId);
        return UserMapper.toDto(user);
    }
}
