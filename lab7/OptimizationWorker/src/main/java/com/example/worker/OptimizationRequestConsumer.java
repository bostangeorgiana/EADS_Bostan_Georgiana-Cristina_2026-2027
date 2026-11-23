package com.example.worker;

import com.example.worker.OptimizationDto.OptimizationAck;
import com.example.worker.OptimizationDto.OptimizationRequest;
import com.example.worker.OptimizationDto.OptimizationSolution;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class OptimizationRequestConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;
    private final VrpSolver solver;

    public OptimizationRequestConsumer(KafkaTemplate<String, String> kafkaTemplate, JsonMapper jsonMapper,
                                       VrpSolver solver) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
        this.solver = solver;
    }

    @KafkaListener(topics = "optimization-requests", groupId = "optimization-worker")
    public void receive(String message) {
        System.out.println("Worker incearca sa proceseze: " + message);
        OptimizationRequest request = jsonMapper.readValue(message, OptimizationRequest.class);

        if (request.vehicles() == null || request.vehicles().isEmpty() || request.depotLatitude() == null) {
            throw new IllegalArgumentException("Scenariul " + request.scenarioId() + " nu are vehicule");
        }

        OptimizationAck ack = new OptimizationAck(request.jobId(), "RECEIVED",
                "Worker a primit scenariul " + request.scenarioId());
        kafkaTemplate.send("optimization-acks", request.jobId(), jsonMapper.writeValueAsString(ack));
        System.out.println("Worker a trimis confirmarea pentru job " + request.jobId());

        OptimizationSolution solution = solver.solve(request);
        kafkaTemplate.send("optimization-solutions", request.jobId(), jsonMapper.writeValueAsString(solution));
        System.out.println("Worker a trimis solutia: " + solution.routes().size() + " rute, "
                + solution.totalDistanceKm() + " km");
    }
}