package com.cfa.remotechunking;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.core.step.item.ChunkProcessor;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
public class WorkerConfiguration {

    public static String TOPIC = "step-execution-eventslol";
    public static String GROUP_ID = "stepresponse_partition";

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ConsumerFactory kafkaFactory;

    @Autowired
    private RemoteChunkingWorkerBuilder workerBuilder;

    @Bean
    public IntegrationFlow workerFlow() {
        return this.workerBuilder
                .itemProcessor(itemProcessor())
                .itemWriter(itemWriter())
                .inputChannel(requests()) // requests received from the manager
                .outputChannel(requestsOut()) // replies sent to the manager
                .build();
    }

    @Bean
    public ItemWriter<Letter> itemWriter() {
        return items -> {
            for (Letter item : items) {
                System.out.println("writing item " + item);
            }
        };
    }

    @Bean
    public ItemProcessor<Letter, Letter> itemProcessor() {
        return item -> {
            System.out.println("processing item " + item);
            return item;
        };
    }

    @Bean
    public IntegrationFlow workerIntegrationFlow() {
        return this.workerBuilder.itemProcessor(itemProcessor()).itemWriter(itemWriter())
                .inputChannel(requests()).outputChannel(requestsOut()).build();
    }

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties(TOPIC);
        containerProps.setGroupId(GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(requests())
                .get();

    }

    @Bean
    public DirectChannel requestsOut() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(TOPIC));
        return IntegrationFlows
                .from(requestsOut())
                .handle(kafkaMessageHandler)
                .get();
    }

    /*
     * Configure the ChunkProcessorChunkHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "requests", outputChannel = "requestsOut")
    public ChunkProcessorChunkHandler<Letter> chunkProcessorChunkHandler() {
        ChunkProcessor<Letter> chunkProcessor
                = new SimpleChunkProcessor<>(itemProcessor(), itemWriter());
        ChunkProcessorChunkHandler<Letter> chunkProcessorChunkHandler
                = new ChunkProcessorChunkHandler<>();
        chunkProcessorChunkHandler.setChunkProcessor(chunkProcessor);
        return chunkProcessorChunkHandler;
    }
}
