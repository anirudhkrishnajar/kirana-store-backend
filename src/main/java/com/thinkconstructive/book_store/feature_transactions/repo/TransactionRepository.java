package com.thinkconstructive.book_store.feature_transactions.repo;

import com.thinkconstructive.book_store.feature_transactions.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.mongodb.repository.DeleteQuery;
import java.util.List;


public interface TransactionRepository extends MongoRepository<Transaction, String> {

    @Query("{ 'transactionId' : ?0 }")
    Transaction findTransactionByTransactionId(String transactionId);


    @Query("{ 'userId' : ?0 }")
    List<Transaction> findByUserId(String userId);

    @Query(value = "{ 'transactionId' : ?0 }")
    @Update(pipeline = { "{ '$set' : { 'amount' : ?1, 'amountInInr' : ?2, 'userId': ?3 } }" })
    void updateTransactionDetailsByTransactionId(String transactionId, Double amount, Double amountInInr, String userId);

    @DeleteQuery
    void deleteTransactionByTransactionId(String transactionId);
}
