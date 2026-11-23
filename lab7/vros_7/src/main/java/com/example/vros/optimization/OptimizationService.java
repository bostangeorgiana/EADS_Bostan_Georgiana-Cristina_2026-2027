package com.example.vros.optimization;

import com.example.vros.common.ResourceNotFoundException;
import com.example.vros.delivery.Delivery;
import com.example.vros.optimization.OptimizationDto.DeliveryData;
import com.example.vros.optimization.OptimizationDto.JobStatus;
import com.example.vros.optimization.OptimizationDto.OptimizationRequest;
import com.example.vros.optimization.OptimizationDto.OptimizationSolution;
import com.example.vros.optimization.OptimizationDto.VehicleData;
import com.example.vros.scenario.Scenario;
import com.example.vros.scenario.ScenarioRepository;
import com.example.vros.vehicle.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OptimizationService {

    private final ScenarioRepository scenarioRepository;
    private final OptimizationProducer producer;
    private final OptimizationJobStore jobStore;

    public OptimizationService(ScenarioRepository scenarioRepository, OptimizationProducer producer,
                               OptimizationJobStore jobStore) {
        this.scenarioRepository = scenarioRepository;
        this.producer = producer;
        this.jobStore = jobStore;
    }

    @Transactional(readOnly = true)
    public JobStatus submit(Long scenarioId) {
        Scenario scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Scenariul cu id " + scenarioId + " nu exista"));

        List<VehicleData> vehicles = new ArrayList<>();
        Double depotLatitude = null;
        Double depotLongitude = null;
        for (Vehicle vehicle : scenario.getVehicles()) {
            vehicles.add(new VehicleData(vehicle.getId(), vehicle.getCapacity()));
            if (depotLatitude == null && vehicle.getDepot() != null) {
                depotLatitude = vehicle.getDepot().getLatitude();
                depotLongitude = vehicle.getDepot().getLongitude();
            }
        }

        List<DeliveryData> deliveries = new ArrayList<>();
        for (Delivery delivery : scenario.getDeliveries()) {
            deliveries.add(new DeliveryData(delivery.getId(), delivery.getCustomer().getLatitude(),
                    delivery.getCustomer().getLongitude(), delivery.getQuantity()));
        }

        String jobId = UUID.randomUUID().toString();
        JobStatus job = new JobStatus(jobId, scenarioId, "SUBMITTED", "Cererea a fost trimisa catre Worker");
        jobStore.saveStatus(job);
        producer.send(new OptimizationRequest(jobId, scenarioId, depotLatitude, depotLongitude, vehicles, deliveries));
        return job;
    }

    public JobStatus getStatus(String jobId) {
        JobStatus job = jobStore.getStatus(jobId);
        if (job == null) {
            throw new ResourceNotFoundException("Job-ul " + jobId + " nu exista");
        }
        return job;
    }

    public OptimizationSolution getSolution(String jobId) {
        getStatus(jobId);
        OptimizationSolution solution = jobStore.getSolution(jobId);
        if (solution == null) {
            throw new ResourceNotFoundException("Solutia pentru job-ul " + jobId + " nu este gata");
        }
        return solution;
    }
}