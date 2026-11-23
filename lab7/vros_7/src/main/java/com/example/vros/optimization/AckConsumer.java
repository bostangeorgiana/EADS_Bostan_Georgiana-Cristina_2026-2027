package com.example.vros.optimization;

import com.example.vros.optimization.OptimizationDto.OptimizationAck;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class AckConsumer {

    private final JsonMapper jsonMapper;
    private final OptimizationJobStore jobStore;

    public AckConsumer(JsonMapper jsonMapper, OptimizationJobStore jobStore) {
        this.jsonMapper = jsonMapper;
        this.jobStore = jobStore;
    }

    @KafkaListener(topics = KafkaConfig.ACKS_TOPIC, groupId = "vros")
    public void receive(String message) {
        OptimizationAck ack = jsonMapper.readValue(message, OptimizationAck.class);
        jobStore.updateStatus(ack.jobId(), ack.status(), ack.message());
        System.out.println("VROS a primit confirmarea: job " + ack.jobId() + " -> " + ack.status() + " (" + ack.message() + ")");
    }
}