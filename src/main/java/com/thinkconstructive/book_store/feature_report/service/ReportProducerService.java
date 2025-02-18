package com.thinkconstructive.book_store.feature_report.service;

import com.thinkconstructive.book_store.feature_report.models.ReportRequestDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ReportProducerService {


    private static final String TOPIC = "report_topic";


    private final KafkaTemplate<String, ReportRequestDto> kafkaTemplate;

    /**
     * Constructor for ReportProducerService
     *
     * @param kafkaTemplate KafkaTemplate to interact with Kafka
     */
    public ReportProducerService(KafkaTemplate<String, ReportRequestDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a report request to the Kafka topic
     *
     * @param requestDto data transfer object containing the report request details
     */
    public void sendReportRequest(ReportRequestDto requestDto) {
        kafkaTemplate.send(TOPIC, requestDto);
        System.out.println("Sent report request: " + requestDto);
    }
}
