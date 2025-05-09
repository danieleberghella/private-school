package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.TeacherDTO;
import com.daniele.berghella.private_school.model.Teacher;
import com.daniele.berghella.private_school.service.TeacherService;
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
@RequestMapping("/api/teachers")
@Tag(name = "Teacher", description = "Operations related to teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all teachers", description = "Retrieve a list of all teachers")
    public List<TeacherDTO> getAll() {
        log.info("Fetching all teachers");
        return teacherService.findAll().stream()
                .map(t -> mapper.map(t, TeacherDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID", description = "Retrieve a specific teacher by its ID")
    public ResponseEntity<TeacherDTO> getById(@PathVariable Long id) {
        log.info(() -> "Fetching teacher with ID: " + id);
        return teacherService.findById(id)
                .map(t -> ResponseEntity.ok(mapper.map(t, TeacherDTO.class)))
                .orElseGet(() -> {
                    log.warning(() -> "Teacher not found with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create new teacher", description = "Add a new teacher to the system")
    public TeacherDTO create(@RequestBody TeacherDTO dto) {
        log.info("Creating a new teacher");
        Teacher saved = teacherService.save(mapper.map(dto, Teacher.class));
        TeacherDTO responseDTO = mapper.map(saved, TeacherDTO.class);
        log.info(() -> "Teacher created with ID: " + saved.getId());
        return responseDTO;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher", description = "Update an existing teacher by ID")
    public ResponseEntity<TeacherDTO> update(@PathVariable Long id, @RequestBody TeacherDTO dto) {
        log.info(() -> "Updating teacher with ID: " + id);
        return teacherService.findById(id).map(t -> {
            t.setFirstName(dto.getFirstName());
            t.setLastName(dto.getLastName());
            t.setEmail(dto.getEmail());
            Teacher updated = teacherService.save(t);
            TeacherDTO updatedDTO = mapper.map(updated, TeacherDTO.class);
            log.info(() -> "Teacher updated with ID: " + updated.getId());
            return ResponseEntity.ok(updatedDTO);
        }).orElseGet(() -> {
            log.warning(() -> "Teacher not found with ID: " + id);
            return ResponseEntity.notFound().build();
        });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher", description = "Delete a teacher by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(() -> "Deleting teacher with ID: " + id);
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
