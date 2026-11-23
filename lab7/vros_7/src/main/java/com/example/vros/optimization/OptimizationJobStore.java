package com.example.vros.optimization;

import com.example.vros.optimization.OptimizationDto.JobStatus;
import com.example.vros.optimization.OptimizationDto.OptimizationSolution;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OptimizationJobStore {

    private final Map<String, JobStatus> jobs = new ConcurrentHashMap<>();
    private final Map<String, OptimizationSolution> solutions = new ConcurrentHashMap<>();

    public void saveStatus(JobStatus job) {
        jobs.put(job.jobId(), job);
    }

    public void updateStatus(String jobId, String status, String message) {
        JobStatus current = jobs.get(jobId);
        if (current == null) {
            return;
        }
        if (current.status().equals("COMPLETED") || current.status().equals("FAILED")) {
            return;
        }
        jobs.put(jobId, new JobStatus(jobId, current.scenarioId(), status, message));
    }

    public void saveSolution(OptimizationSolution solution) {
        solutions.put(solution.jobId(), solution);
        updateStatus(solution.jobId(), "COMPLETED", "Solutia a fost calculata");
    }

    public JobStatus getStatus(String jobId) {
        return jobs.get(jobId);
    }

    public OptimizationSolution getSolution(String jobId) {
        return solutions.get(jobId);
    }
}