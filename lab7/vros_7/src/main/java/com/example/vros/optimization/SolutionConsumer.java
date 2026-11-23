package com.example.vros.optimization;

import com.example.vros.optimization.OptimizationDto.OptimizationSolution;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class SolutionConsumer {

    private final JsonMapper jsonMapper;
    private final OptimizationJobStore jobStore;

    public SolutionConsumer(JsonMapper jsonMapper, OptimizationJobStore jobStore) {
        this.jsonMapper = jsonMapper;
        this.jobStore = jobStore;
    }

    @KafkaListener(topics = KafkaConfig.SOLUTIONS_TOPIC, groupId = "vros")
    public void receive(String message) {
        OptimizationSolution solution = jsonMapper.readValue(message, OptimizationSolution.class);
        jobStore.saveSolution(solution);
        System.out.println("VROS a primit solutia pentru job " + solution.jobId() + ": " + solution.routes().size()
                + " rute, " + solution.totalDistanceKm() + " km");
    }
}