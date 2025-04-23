package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.StudentDTO;
import com.daniele.berghella.private_school.model.Student;
import com.daniele.berghella.private_school.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        return studentService.findAll().stream()
                .map(s -> mapper.map(s, StudentDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by its ID")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        return studentService.findById(id)
                .map(s -> mapper.map(s, StudentDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new student", description = "Add a new student to the system")
    public StudentDTO create(@RequestBody StudentDTO dto) {
        Student saved = studentService.save(mapper.map(dto, Student.class));
        return mapper.map(saved, StudentDTO.class);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Update an existing student by ID")
    public ResponseEntity<StudentDTO> update(@PathVariable Long id, @RequestBody StudentDTO dto) {
        return studentService.findById(id).map(s -> {
            s.setFirstName(dto.getFirstName());
            s.setLastName(dto.getLastName());
            s.setEmail(dto.getEmail());
            Student updated = studentService.save(s);
            return ResponseEntity.ok(mapper.map(updated, StudentDTO.class));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete a student by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
