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
     *
     * @param userRepository
     */
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * @param userId
     * @return
     */


    @Override
    public UserDto getUser(String userId) {
        User user = userRepository.findUserByUserId(userId);
        return UserMapper.toDto(user);
    }

    /**
     *
     * @return
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
     *
     * @param userDto
     * @return
     */

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.insert(UserMapper.toEntity(userDto));
        return UserMapper.toDto(user);
    }

    /**
     *
     * @param userDto
     * @return
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toEntity(userDto));
        return UserMapper.toDto(user);
    }

    /**
     *
     * @param userId
     */

    @Override
    public void deleteUserByUserId(String userId) {
        userRepository.deleteUserByUserId(userId);
    }

    /**
     *
     *
     * @param userId
     * @return
     */
    @Override
    public UserDto getUserByUsername(String userId) {
        User user = userRepository.findUserByUserId(userId);
        return UserMapper.toDto(user);
    }
}
