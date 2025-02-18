package com.thinkconstructive.book_store.feature_report.service;

import com.thinkconstructive.book_store.feature_report.models.ReportRequestDto;
import com.thinkconstructive.book_store.feature_report.models.Report;
import com.thinkconstructive.book_store.feature_transactions.models.Transaction;
import com.thinkconstructive.book_store.feature_report.repo.ReportRepository;
import com.thinkconstructive.book_store.feature_transactions.repo.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Service
public class ReportConsumerService {

    private final TransactionRepository transactionRepository;
    private final ReportRepository reportRepository;

    /**
     * Constructor for ReportConsumerService
     *
     * @param transactionRepository  repository for transaction data
     * @param reportRepository       repository for generated reports
     */
    public ReportConsumerService(TransactionRepository transactionRepository, ReportRepository reportRepository) {
        this.transactionRepository = transactionRepository;
        this.reportRepository = reportRepository;
    }

    /**
     * Kafka listener method that listens to the "report_topic"
     *
     * @param requestDto data transfer object containing the report request details
     */
    @KafkaListener(topics = "report_topic", groupId = "report_group")
    public void listenReportRequests(ReportRequestDto requestDto) {
        System.out.println("Received report request: " + requestDto);
        generateAndSaveReport(requestDto);
    }

    /**
     * Generates report and saves in repository
     *
     * @param requestDto data transfer object containing the report request details
     */
    private void generateAndSaveReport(ReportRequestDto requestDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate start = LocalDate.parse(requestDto.startDate(), formatter);
        LocalDate end = LocalDate.parse(requestDto.endDate(), formatter);


        List<Transaction> transactions = transactionRepository.findAll();
        double totalCredit = 0.0;
        double totalDebit = 0.0;


        for (Transaction txn : transactions) {
            LocalDate txnDate = LocalDate.parse(txn.transactionDate(), formatter);
            if (!txnDate.isBefore(start) && !txnDate.isAfter(end)) {
                if ("credit".equalsIgnoreCase(txn.Transaction_Type())) {
                    totalCredit += txn.amountInInr();
                } else if ("debit".equalsIgnoreCase(txn.Transaction_Type())) {
                    totalDebit += txn.amountInInr();
                }
            }
        }
        double netflow = totalCredit - totalDebit;


        String reportId = requestDto.reportType() + "_" + UUID.randomUUID();
        Report report = new Report(
                reportId,
                requestDto.startDate(),
                requestDto.endDate(),
                totalCredit,
                totalDebit,
                netflow
        );
        reportRepository.save(report);
        System.out.println("Report generated and saved: " + report);
    }
}
