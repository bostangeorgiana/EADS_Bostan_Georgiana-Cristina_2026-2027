package com.example.lab2.actuator;

import com.example.lab2.config.DataProperties;
import com.example.lab2.service.StudentService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Map;

@Component
@Endpoint(id = "datasource") // exposed at /actuator/datasource
public class DataSourceEndpoint {

    private final DataProperties props;
    private final StudentService service;

    public DataSourceEndpoint(DataProperties props, StudentService service) {
        this.props = props;
        this.service = service;
    }

    @ReadOperation
    public Map<String, Object> dataSourceInfo() {
        return Map.of(
                "file", props.getFile(),
                "absolutePath", Path.of(props.getFile()).toAbsolutePath().toString(),
                "format", props.getFormat(),
                "reader", service.getReader().getClass().getSimpleName(),
                "recordsLoaded", service.getStudents().size()
        );
    }
}

// java -jar target/lab2-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
// java -jar target/lab2-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod