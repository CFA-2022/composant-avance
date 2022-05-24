package com.cfa.jobs.simplejob;

import com.cfa.jobs.jobexample.SimpleTaskletSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private Source sources;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory
                .get("simpleJob")
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1() {
        return this.stepBuilderFactory
                .get("simpleStep")
                .tasklet(new SimpleTaskletSource(sources))
                .build();
    }

    @Bean
    public Step simpleStep2() {
        return this.stepBuilderFactory
                .get("simpleStep")
                .tasklet(new SimpleTaskletSource(sources))
                .build();
    }
}
