package com.thinkconstructive.book_store.feature_transactions.models;

public record TransactionDto(
        String transactionId,
        Double amount,
        String Transaction_Type,
        String currency,
        Double amountInInr,
        String userId,
        String transactionDate
) {}
