package com.thinkconstructive.book_store.feature_transactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkconstructive.book_store.feature_transactions.models.TransactionDto;
import com.thinkconstructive.book_store.feature_transactions.models.Transaction;
import com.thinkconstructive.book_store.feature_transactions.repo.TransactionRepository;
import com.thinkconstructive.book_store.feature_transactions.utils.TransactionMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao {

    private final TransactionRepository transactionRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();


    private static final String CACHE_KEY_PREFIX = "getAllTransactions:";

    /**
     * Constructor to initialize the TransactionDaoImpl
     *
     * @param transactionRepository  repository to access transaction data
     * @param redisTemplate Redis template used for caching
     */
    public TransactionDaoImpl(TransactionRepository transactionRepository,
                              RedisTemplate<String, String> redisTemplate) {
        this.transactionRepository = transactionRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Fetch a transaction by its ID
     *
     * @param transactionId  ID of transaction to retrieve
     * @return TransactionDto representing transaction
     */
    @Override
    public TransactionDto getTransaction(String transactionId) {
        Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);
        return TransactionMapper.toDto(transaction);
    }

    /**
     * Retrieve all transactions of a given user
     * @param userId  ID of user
     * @return list of TransactionDto objects
     */
    @Override
    public List<TransactionDto> getAllTransactions(String userId) {
        String cacheKey = CACHE_KEY_PREFIX + userId;

        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            try {
                List<TransactionDto> transactions = mapper.readValue(
                        cachedJson, new TypeReference<List<TransactionDto>>() {});
                return transactions;
            } catch (JsonProcessingException e) {

                e.printStackTrace();
            }
        }

        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionDtoList.add(TransactionMapper.toDto(transaction));
        }
        try {
            String jsonValue = mapper.writeValueAsString(transactionDtoList);
            redisTemplate.opsForValue().set(cacheKey, jsonValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return transactionDtoList;
    }

    /**
     * Create a new transaction
     *
     * @param transactionDto  DTO of the transaction to be created
     * @return created TransactionDto
     */
    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Double amountInInr = convertToInr(transactionDto.amount(), transactionDto.currency());


        String currentDate = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        TransactionDto dtoWithConversion = new TransactionDto(
                transactionDto.transactionId(),
                transactionDto.amount(),
                transactionDto.Transaction_Type(),
                transactionDto.currency(),
                amountInInr,
                transactionDto.userId(),
                currentDate
        );

        Transaction transaction = transactionRepository.insert(TransactionMapper.toEntity(dtoWithConversion));


        String cacheKey = CACHE_KEY_PREFIX + transactionDto.userId();
        redisTemplate.delete(cacheKey);

        return TransactionMapper.toDto(transaction);
    }

    /**
     * Update an existing transaction
     *
     * @param transactionDto  DTO  transaction with updated details
     * @return updated TransactionDto
     */
    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        Double amountInInr = convertToInr(transactionDto.amount(), transactionDto.currency());


        Transaction existingTransaction = transactionRepository.findTransactionByTransactionId(transactionDto.transactionId());
        String existingDate = (existingTransaction != null) ? existingTransaction.transactionDate() : null;


        TransactionDto dtoWithConversion = new TransactionDto(
                transactionDto.transactionId(),
                transactionDto.amount(),
                transactionDto.Transaction_Type(),
                transactionDto.currency(),
                amountInInr,
                transactionDto.userId(),
                existingDate
        );


        transactionRepository.updateTransactionDetailsByTransactionId(
                transactionDto.transactionId(),
                transactionDto.amount(),
                amountInInr,
                transactionDto.userId()
        );


        Transaction updatedTransaction = transactionRepository.findTransactionByTransactionId(transactionDto.transactionId());


        String cacheKey = CACHE_KEY_PREFIX + transactionDto.userId();
        redisTemplate.delete(cacheKey);

        return TransactionMapper.toDto(updatedTransaction);
    }

    /**
     * Delete a transaction by its ID
     *
     * @param transactionId  ID of transaction to be deleted
     */
    @Override
    public void deleteTransactionByTransactionId(String transactionId) {
        Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);
        transactionRepository.deleteTransactionByTransactionId(transactionId);
        if (transaction != null && transaction.userId() != null) {
            String cacheKey = CACHE_KEY_PREFIX + transaction.userId();
            redisTemplate.delete(cacheKey);
        }
    }


    private Double convertToInr(Double amount, String currency) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.fxratesapi.com/latest";
            ResponseEntity<ExchangeResponse> response = restTemplate.getForEntity(url, ExchangeResponse.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().success) {
                ExchangeResponse body = response.getBody();
                Double rateCurrency = body.rates.get(currency.toUpperCase());
                Double rateINR = body.rates.get("INR");
                if (rateCurrency != null && rateINR != null) {
                    return (amount / rateCurrency) * rateINR;
                } else {
                    System.err.println("Conversion rates for " + currency + " or INR not found.");
                }
            } else {
                System.err.println("FX Rates API response is not OK or has no body.");
            }
        } catch (Exception e) {
            System.err.println("Currency conversion failed: " + e.getMessage());
        }
        return 0.0;
    }


    public static class ExchangeResponse {
        public boolean success;
        public String terms;
        public String privacy;
        public long timestamp;
        public String date;
        public String base;
        public java.util.Map<String, Double> rates;
    }
}
