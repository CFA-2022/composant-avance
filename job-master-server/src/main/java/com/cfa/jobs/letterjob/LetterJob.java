package com.cfa.jobs.letterjob;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchIntegration
public class LetterJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public LetterWriter letterWriter;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory
                .get("simpleJob")
                .start(letterStep())
                .build();
    }

    @Bean
    public FlatFileItemReader<Letter> reader() {
        //Create reader instance
        FlatFileItemReader<Letter> reader = new FlatFileItemReader<>();

        //Set input file location
        reader.setResource(new FileSystemResource("letter.csv"));

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper<>() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("message", "creation_date", "treatment_date");
                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(Letter.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public Step letterStep() {
        return this.stepBuilderFactory
                .get("letterStep")
                .<Letter, Letter>chunk(1)
                .reader(reader())
                .writer(letterWriter)
                .build();
    }
}
