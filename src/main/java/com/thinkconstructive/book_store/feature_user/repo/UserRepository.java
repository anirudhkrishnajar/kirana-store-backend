package com.thinkconstructive.book_store.feature_user.repo;

import com.thinkconstructive.book_store.feature_user.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.DeleteQuery;


public interface UserRepository extends MongoRepository<User, String> {


    @Query("{ 'userId' : ?0 }")
    User findUserByUserId(String userId);

    @DeleteQuery
    void deleteUserByUserId(String userId);
}
