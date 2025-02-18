package com.thinkconstructive.book_store.feature_transactions.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public record Transaction(

        String transactionId,

        Double amount,

        String Transaction_Type,

        String currency,

        Double amountInInr,

        String userId,

        String transactionDate
) {}
