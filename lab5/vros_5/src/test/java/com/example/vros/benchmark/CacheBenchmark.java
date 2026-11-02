package com.example.vros.benchmark;

import com.example.vros.VrosApplication;
import com.example.vros.entity.Delivery;
import com.example.vros.service.DeliveryService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class CacheBenchmark {

    private ConfigurableApplicationContext context;
    private DeliveryService deliveryService;

    @Setup(Level.Trial)
    public void setup() {
        context = SpringApplication.run(VrosApplication.class);
        deliveryService = context.getBean(DeliveryService.class);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        context.close();
    }

    @Benchmark
    public List<Delivery> withoutCache() {
        return deliveryService.findByCustomer(3L);
    }

    @Benchmark
    public List<Delivery> withCache() {
        return deliveryService.findByCustomerCached(3L);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(CacheBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}