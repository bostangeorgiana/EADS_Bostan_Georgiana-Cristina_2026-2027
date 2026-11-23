package com.example.vros.optimization;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String REQUESTS_TOPIC = "optimization-requests";
    public static final String ACKS_TOPIC = "optimization-acks";
    public static final String SOLUTIONS_TOPIC = "optimization-solutions";

    @Bean
    public NewTopic requestsTopic() {
        return TopicBuilder.name(REQUESTS_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic acksTopic() {
        return TopicBuilder.name(ACKS_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic solutionsTopic() {
        return TopicBuilder.name(SOLUTIONS_TOPIC).partitions(1).replicas(1).build();
    }
}