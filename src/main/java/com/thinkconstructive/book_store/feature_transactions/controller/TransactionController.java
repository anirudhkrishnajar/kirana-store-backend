package com.thinkconstructive.book_store.feature_transactions.controller;

import com.thinkconstructive.book_store.feature_transactions.models.TransactionDto;
import com.thinkconstructive.book_store.feature_report.repo.ReportRepository;
import com.thinkconstructive.book_store.feature_transactions.service.TransactionService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.Duration;

/**
 * Controller handling transactions in the Kirana
 * Provides endpoints for retrieving, creating, updating, and deleting transactions
 */
@RestController
@RequestMapping("/kirana/transactions")
public class TransactionController {
    private final ReportRepository reportRepository;
    private final Bucket bucket;
    private final Bucket bucket2;
    private final TransactionService transactionService;

    /**
     * Constructor for TransactionController
     * Set up rate limiting using Bucket4j
     *
     * @param transactionService  service for handling transaction logic
     * @param reportRepository  repository for accessing reports
     */
    public TransactionController(TransactionService transactionService, ReportRepository reportRepository) {
        this.transactionService = transactionService;
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
        Bandwidth limit2 = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket2 = Bucket.builder()
                .addLimit(limit2)
                .build();
        this.reportRepository = reportRepository;
    }

    /**
     * Retrieve  specific transaction by  ID
     * Only accessible by the user associated with the transaction
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return ResponseEntity containing the transaction details or unauthorized if not the correct user
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransaction(@PathVariable("transactionId") String transactionId) {
        TransactionDto transactionDto = transactionService.getTransaction(transactionId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        if (transactionDto.userId() == null || !transactionDto.userId().equals(currentUserId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    /**
     * Retrieve   all transactions for the current user
     * Implement rate limiting to prevent excessive requests
     *
     * @return ResponseEntity containing the list of transactions or too many requests if rate limit exceeded
     */
    @GetMapping("/all")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        if (bucket.tryConsume(1)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUserId = auth.getName();
            List<TransactionDto> userTransactions = transactionService.getAllTransactions(currentUserId);
            return new ResponseEntity<>(userTransactions, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    /**
     * Create new transaction for current user
     * Implement rate limiting to prevent excessive requests
     *
     * @param transactionDto details of the transaction
     * @return ResponseEntity containing created transaction or too many requests if rate limit exceeded
     */
    @PostMapping("/")
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        if (bucket2.tryConsume(1)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUserId = auth.getName();
            TransactionDto transactionWithUser = new TransactionDto(
                    transactionDto.transactionId(),
                    transactionDto.amount(),
                    transactionDto.Transaction_Type(),
                    transactionDto.currency(),
                    transactionDto.amountInInr(),
                    currentUserId,
                    transactionDto.transactionDate()
            );
            TransactionDto createdTransaction = transactionService.createTransaction(transactionWithUser);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    /**
     * Update an existing transaction for current user
     *
     * @param transactionDto the updated transaction details
     * @return ResponseEntity containing the updated transaction or unauthorized if user mismatch
     */
    @PutMapping("/")
    public ResponseEntity<?> updateTransaction(@RequestBody TransactionDto transactionDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        if (transactionDto.userId() != null && !transactionDto.userId().equals(currentUserId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        TransactionDto transactionWithUser = new TransactionDto(
                transactionDto.transactionId(),
                transactionDto.amount(),
                transactionDto.Transaction_Type(),
                transactionDto.currency(),
                transactionDto.amountInInr(),
                currentUserId,
                transactionDto.transactionDate()
        );
        TransactionDto updatedTransaction = transactionService.updateTransaction(transactionWithUser);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    /**
     * Delete a specific transaction by ID.
     * Only accessible by the user associated with the transaction.
     *
     * @param transactionId the ID of the transaction to delete
     * @return ResponseEntity with success or unauthorized if not the correct user
     */
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable("transactionId") String transactionId) {
        TransactionDto transactionDto = transactionService.getTransaction(transactionId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        if (transactionDto == null || transactionDto.userId() == null || !transactionDto.userId().equals(currentUserId)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        transactionService.deleteTransactionByTransactionId(transactionId);
        return new ResponseEntity<>("Transaction deleted successfully: " + transactionId, HttpStatus.OK);
    }
}
