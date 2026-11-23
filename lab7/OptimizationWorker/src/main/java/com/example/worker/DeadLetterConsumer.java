package com.example.worker;

import com.example.worker.OptimizationDto.OptimizationAck;
import com.example.worker.OptimizationDto.OptimizationRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class DeadLetterConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public DeadLetterConsumer(KafkaTemplate<String, String> kafkaTemplate, JsonMapper jsonMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "optimization-requests-dlt", groupId = "optimization-worker-dlt")
    public void receive(String message) {
        System.out.println("DLQ: mesaj esuat dupa 3 incercari: " + message);
        try {
            OptimizationRequest request = jsonMapper.readValue(message, OptimizationRequest.class);
            OptimizationAck ack = new OptimizationAck(request.jobId(), "FAILED",
                    "Optimizarea a esuat dupa 3 incercari");
            kafkaTemplate.send("optimization-acks", request.jobId(), jsonMapper.writeValueAsString(ack));
        } catch (Exception e) {
            System.out.println("DLQ: mesajul nu poate fi citit");
        }
    }
}