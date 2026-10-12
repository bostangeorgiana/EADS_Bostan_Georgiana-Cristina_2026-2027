package com.example.lab2.service;

import com.example.lab2.model.Student;
import com.example.lab2.reader.DataReader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final DataReader reader;     // Spring pune Csv / TxtDataReader
    private final List<Student> students;

    public StudentService(DataReader reader) {
        this.reader = reader;
        this.students = reader.read();   // se citeste doar la pornire
    }

    public List<Student> getStudents() { return students; }
    public DataReader getReader() { return reader; }
}