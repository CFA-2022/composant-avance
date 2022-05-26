package com.cfa;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
@EnableBatchIntegration
public class JobMasterApp {

    public static void main(String[] args) {
        SpringApplication.run(JobMasterApp.class, args);
    }

}
