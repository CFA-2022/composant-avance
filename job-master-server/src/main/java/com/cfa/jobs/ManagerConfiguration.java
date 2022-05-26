package com.cfa.jobs;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
public class ManagerConfiguration {

    public static String TOPIC = "step-execution-eventslol";
    public static String GROUP_ID = "stepresponse_partition";
    @Autowired
    private RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public KafkaTemplate kafkaTemplate;

    @Autowired
    private ConsumerFactory consumerFactory;

    @Bean
    public Job remoteChunkingJob() {
        return jobBuilderFactory
                .get("remoteChunkingJob")
                .start(managerStep())
                .build();
    }

    @Bean
    public Step managerStep() {
        return this.managerStepBuilderFactory.get("masterStep")
                .chunk(10)
                .reader(reader())
                .outputChannel(requests()) // requests sent to workers
                .inputChannel(replies()) // replies received from workers
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
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        return IntegrationFlows
                .from(requests())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate).topic(TOPIC))
                .get();

        /*
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(TOPIC));

        return IntegrationFlows
                .from(requests())
                .handle(kafkaMessageHandler)
                .get();
         */
    }

    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties("step-execution-eventslol");
        containerProps.setGroupId("stepresponse_partition");

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(consumerFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                //.from(kafkaMessageChannel)
                .from(Kafka.inboundChannelAdapter(consumerFactory, "step-execution-eventslol"))
                .channel(replies())
                .get();
    }
}