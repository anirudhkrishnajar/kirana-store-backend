package com.thinkconstructive.book_store.config;

import com.thinkconstructive.book_store.feature_report.models.ReportRequestDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class ReportKafkaProducerConfig {

    /**
     * Creates  ProducerFactory for producing messages
     * and a value of type ReportRequestDto.
     *
     * @return A ProducerFactory configured with serialization settings.
     */
    @Bean
    public ProducerFactory<String, ReportRequestDto> reportProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates a KafkaTemplate
     *
     * @return A KafkaTemplate for producing messages of type ReportRequestDto.
     */
    @Bean
    public KafkaTemplate<String, ReportRequestDto> reportKafkaTemplate() {
        return new KafkaTemplate<>(reportProducerFactory());
    }
}
