package com.thinkconstructive.book_store.feature_transactions;

import com.thinkconstructive.book_store.feature_transactions.models.TransactionDto;

import java.util.List;

public interface TransactionDao {
    TransactionDto getTransaction(String transactionId);
    List<TransactionDto> getAllTransactions(String userId);
    TransactionDto createTransaction(TransactionDto transactionDto);
    TransactionDto updateTransaction(TransactionDto transactionDto);
    void deleteTransactionByTransactionId(String transactionId);
}
