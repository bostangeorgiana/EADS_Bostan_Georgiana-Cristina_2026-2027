package com.example.lab2.reader;

import com.example.lab2.config.DataProperties;
import com.example.lab2.model.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Profile("dev")
@ConditionalOnExpression("'${app.data.format:none}'.equalsIgnoreCase('csv')")
public class CsvDataReader implements DataReader {

    private final DataProperties props;

    // Spring injecteaza -> DataProperties (Dependency Injection)
    public CsvDataReader(DataProperties props) {
        this.props = props;
    }

    @Override
    public List<Student> read() {
        try {
            List<String> lines = Files.readAllLines(
                    Path.of(props.getFile()), Charset.forName(props.getEncoding()));

            // ignoram antetul
            if (props.isSkipHeader() && !lines.isEmpty()) {
                lines = lines.subList(1, lines.size());
            }

            List<Student> students = new ArrayList<>();
            for (String line : lines) {
                if (line.isBlank()) continue;
                String[] parts = line.split(Pattern.quote(props.getDelimiter()));
                students.add(new Student(parts[0].trim(), Double.parseDouble(parts[1].trim())));
            }
            return students;
        } catch (IOException e) {
            throw new UncheckedIOException("Nu pot citi fișierul " + props.getFile(), e);
        }
    }
}