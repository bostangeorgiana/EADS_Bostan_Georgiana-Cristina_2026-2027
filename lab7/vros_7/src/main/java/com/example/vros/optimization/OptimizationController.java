package com.example.vros.optimization;

import com.example.vros.optimization.OptimizationDto.JobStatus;
import com.example.vros.optimization.OptimizationDto.OptimizationSolution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/optimizations")
public class OptimizationController {

    private final OptimizationService optimizationService;

    public OptimizationController(OptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @PostMapping("/{scenarioId}")
    public ResponseEntity<JobStatus> submit(@PathVariable Long scenarioId) {
        return ResponseEntity.accepted().body(optimizationService.submit(scenarioId));
    }

    @GetMapping("/{jobId}")
    public JobStatus status(@PathVariable String jobId) {
        return optimizationService.getStatus(jobId);
    }

    @GetMapping("/{jobId}/solution")
    public OptimizationSolution solution(@PathVariable String jobId) {
        return optimizationService.getSolution(jobId);
    }
}