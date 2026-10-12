package com.example.lab2.runner;

import com.example.lab2.config.DataProperties;
import com.example.lab2.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataRunner implements CommandLineRunner {

    private final StudentService service;
    private final DataProperties props;
    private final Environment env;

    public DataRunner(StudentService service, DataProperties props, Environment env) {
        this.service = service;
        this.props = props;
        this.env = env;
    }

    @Override
    public void run(String... args) {
        System.out.println("Active profile:  " + Arrays.toString(env.getActiveProfiles()));
        System.out.println("Reader used:     " + service.getReader().getClass().getSimpleName());
        System.out.println("File:            " + props.getFile());
        System.out.println("Format:          " + props.getFormat());
        System.out.println("Label:           " + props.getLabel());
        System.out.println("Records loaded:  " + service.getStudents().size());
        service.getStudents().stream().limit(3) // sarim primii 3 elevi
                .forEach(s -> System.out.println("   - " + s.name() + " : " + s.grade()));
    }
}