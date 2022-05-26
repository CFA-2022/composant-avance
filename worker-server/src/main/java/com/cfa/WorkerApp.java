package com.cfa;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@EnableBatchIntegration
@EnableBatchProcessing
public class WorkerApp {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApp.class, args);
    }
}
