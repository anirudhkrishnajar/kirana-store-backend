package com.thinkconstructive.book_store.feature_transactions.service;

import com.thinkconstructive.book_store.feature_transactions.models.TransactionDto;
import java.util.List;

public interface TransactionService {
    TransactionDto getTransaction(String transactionId);
    List<TransactionDto> getAllTransactions(String userId);
    TransactionDto createTransaction(TransactionDto transactionDto);
    TransactionDto updateTransaction(TransactionDto transactionDto);
    void deleteTransactionByTransactionId(String transactionId);
}
