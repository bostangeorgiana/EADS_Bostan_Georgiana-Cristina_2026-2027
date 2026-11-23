package com.example.vros.optimization;

import com.example.vros.optimization.OptimizationDto.OptimizationRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
public class OptimizationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public OptimizationProducer(KafkaTemplate<String, String> kafkaTemplate, JsonMapper jsonMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }

    public void send(OptimizationRequest request) {
        String message = jsonMapper.writeValueAsString(request);
        kafkaTemplate.send(KafkaConfig.REQUESTS_TOPIC, request.jobId(), message);
        System.out.println("VROS a trimis cererea: " + message);
    }
}