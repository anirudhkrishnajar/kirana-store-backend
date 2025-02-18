package com.thinkconstructive.book_store.feature_transactions.service;

import com.thinkconstructive.book_store.feature_transactions.TransactionDaoImpl;
import com.thinkconstructive.book_store.feature_transactions.models.TransactionDto;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDaoImpl transactionDaoImpl;

    /**
     * Constructor for TransactionServiceImpl
     *
     * @param transactionDaoImpl DAO implementation for interacting with  transaction data
     */
    public TransactionServiceImpl(TransactionDaoImpl transactionDaoImpl) {
        this.transactionDaoImpl = transactionDaoImpl;
    }

    /**
     * Retrieve transaction by transaction ID
     *
     * @param transactionId unique identifier for the transaction
     * @return transaction  with the given transactionId
     */
    @Override
    public TransactionDto getTransaction(String transactionId) {
        return transactionDaoImpl.getTransaction(transactionId);
    }

    /**
     * Retrieve all transactions of a specific user
     *
     * @param userId  ID of the user
     * @return A list of transactions of a user
     */
    @Override
    public List<TransactionDto> getAllTransactions(String userId) {
        return transactionDaoImpl.getAllTransactions(userId);
    }

    /**
     * Create a new transaction
     *
     * @param transactionDto transaction data to be created
     * @return  created transaction
     */
    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        return transactionDaoImpl.createTransaction(transactionDto);
    }

    /**
     * Update an existing transaction
     *
     * @param transactionDto  updated transaction data
     * @return  updated transaction
     */
    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        return transactionDaoImpl.updateTransaction(transactionDto);
    }

    /**
     * Delete a transaction  transaction ID
     *
     * @param transactionId  transaction ID of the transaction deleted
     */
    @Override
    public void deleteTransactionByTransactionId(String transactionId) {
        transactionDaoImpl.deleteTransactionByTransactionId(transactionId);
    }
}
