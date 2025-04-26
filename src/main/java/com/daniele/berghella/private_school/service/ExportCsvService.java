package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.dto.EnrollmentCsvDTO;
import com.daniele.berghella.private_school.dto.StudentDTO;
import com.daniele.berghella.private_school.dto.TeacherDTO;
import com.daniele.berghella.private_school.model.Enrollment;
import com.daniele.berghella.private_school.model.Student;
import com.daniele.berghella.private_school.model.Teacher;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class ExportCsvService {

    @Autowired
    @Qualifier("csvMapper")
    private CsvMapper csvMapper;

    @Autowired private StudentService studentService;
    @Autowired private TeacherService teacherService;
    @Autowired private EnrollmentService enrollmentService;
    @Autowired private ModelMapper mapper;

    public void exportStudentsToCsv(String filename) throws IOException {
        log.info(() -> "Exporting students to CSV file: " + filename);

        List<Student> students = studentService.findAll();
        List<StudentDTO> dtos = students.stream()
                .map(s -> mapper.map(s, StudentDTO.class))
                .collect(Collectors.toList());

        CsvSchema schema = csvMapper.schemaFor(StudentDTO.class).withHeader();
        csvMapper.writerFor(List.class)
                .with(schema)
                .writeValue(new File(filename), dtos);

        log.info(() -> "Students exported successfully to: " + filename);
    }

    public void exportTeachersToCsv(String filename) throws IOException {
        log.info(() -> "Exporting teachers to CSV file: " + filename);

        List<Teacher> teachers = teacherService.findAll();
        List<TeacherDTO> dtos = teachers.stream()
                .map(t -> mapper.map(t, TeacherDTO.class))
                .collect(Collectors.toList());

        CsvSchema schema = csvMapper.schemaFor(TeacherDTO.class).withHeader();
        csvMapper.writerFor(List.class)
                .with(schema)
                .writeValue(new File(filename), dtos);

        log.info(() -> "Teachers exported successfully to: " + filename);
    }

    public void exportEnrollmentsToCsv(String filename) throws IOException {
        log.info(() -> "Exporting enrollments to CSV file: " + filename);

        List<Enrollment> enrollments = enrollmentService.findAll();
        List<EnrollmentCsvDTO> dtos = enrollments.stream()
                .map(e -> mapper.map(e, EnrollmentCsvDTO.class))
                .collect(Collectors.toList());

        CsvSchema schema = csvMapper.schemaFor(EnrollmentCsvDTO.class).withHeader();
        csvMapper.writerFor(List.class)
                .with(schema)
                .writeValue(new File(filename), dtos);

        log.info(() -> "Enrollments exported successfully to: " + filename);
    }
}
