package com.thinkconstructive.book_store.feature_transactions.utils;

import com.thinkconstructive.book_store.feature_transactions.models.TransactionDto;
import com.thinkconstructive.book_store.feature_transactions.models.Transaction;


public class TransactionMapper {

    /**
     * Convert Transaction entity to TransactionDto
     *
     * @param transaction  Transaction entity to be converted
     * @return  TransactionDto
     */
    public static TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.transactionId(),
                transaction.amount(),
                transaction.Transaction_Type(),
                transaction.currency(),
                transaction.amountInInr(),
                transaction.userId(),
                transaction.transactionDate()
        );
    }

    /**
     * Convert TransactionDto to Transaction entity
     *
     * @param transactionDto TransactionDto to be converted
     * @return  Transaction entity.
     */
    public static Transaction toEntity(TransactionDto transactionDto) {
        return new Transaction(
                transactionDto.transactionId(),
                transactionDto.amount(),
                transactionDto.Transaction_Type(),
                transactionDto.currency(),
                transactionDto.amountInInr(),
                transactionDto.userId(),
                transactionDto.transactionDate()
        );
    }
}
