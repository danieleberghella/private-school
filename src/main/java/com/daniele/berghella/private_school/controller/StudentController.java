package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.StudentDTO;
import com.daniele.berghella.private_school.model.Student;
import com.daniele.berghella.private_school.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/api/students")
@Tag(name = "Student", description = "Operations related to students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    public List<StudentDTO> getAll() {
        log.info("Fetching all students");
        return studentService.findAll().stream()
                .map(s -> mapper.map(s, StudentDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by its ID")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        log.info(() -> "Fetching student with ID: " + id);
        return studentService.findById(id)
                .map(s -> mapper.map(s, StudentDTO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warning(() -> "Student not found with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create new student", description = "Add a new student to the system")
    public StudentDTO create(@RequestBody StudentDTO dto) {
        log.info("Creating a new student");
        Student saved = studentService.save(mapper.map(dto, Student.class));
        log.info(() -> "Student created with ID: " + saved.getId());
        return mapper.map(saved, StudentDTO.class);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Update an existing student by ID")
    public ResponseEntity<StudentDTO> update(@PathVariable Long id, @RequestBody StudentDTO dto) {
        log.info(() -> "Updating student with ID: " + id);
        return studentService.findById(id).map(s -> {
            s.setFirstName(dto.getFirstName());
            s.setLastName(dto.getLastName());
            s.setEmail(dto.getEmail());
            Student updated = studentService.save(s);
            log.info(() -> "Student updated with ID: " + updated.getId());
            return ResponseEntity.ok(mapper.map(updated, StudentDTO.class));
        }).orElseGet(() -> {
            log.warning(() -> "Student not found with ID: " + id);
            return ResponseEntity.notFound().build();
        });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete a student by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(() -> "Deleting student with ID: " + id);
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
