package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.ClassroomDTO;
import com.daniele.berghella.private_school.model.Classroom;
import com.daniele.berghella.private_school.service.ClassroomService;
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
@RequestMapping("/api/classrooms")
@Tag(name = "Classroom", description = "Operations related to classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all classrooms", description = "Retrieve a list of all classrooms")
    public List<ClassroomDTO> getAll() {
        log.info("Fetching all classrooms");
        return classroomService.findAll().stream()
                .map(c -> mapper.map(c, ClassroomDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get classroom by ID", description = "Retrieve a specific classroom by its ID")
    public ResponseEntity<ClassroomDTO> getById(@PathVariable Long id) {
        log.info("Fetching classroom with id: " + id);
        return classroomService.findById(id)
                .map(c -> mapper.map(c, ClassroomDTO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warning("Classroom not found with id: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create new classroom", description = "Add a new classroom to the system")
    public ClassroomDTO create(@RequestBody ClassroomDTO dto) {
        log.info("Creating new classroom: " + dto.getName());
        Classroom saved = classroomService.save(mapper.map(dto, Classroom.class));
        return mapper.map(saved, ClassroomDTO.class);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update classroom", description = "Update an existing classroom by ID")
    public ResponseEntity<ClassroomDTO> update(@PathVariable Long id, @RequestBody ClassroomDTO dto) {
        log.info("Updating classroom with id: " + id);
        return classroomService.findById(id).map(c -> {
            c.setName(dto.getName());
            c.setCapacity(dto.getCapacity());
            Classroom updated = classroomService.save(c);
            return ResponseEntity.ok(mapper.map(updated, ClassroomDTO.class));
        }).orElseGet(() -> {
            log.warning("Classroom not found for update with id: " + id);
            return ResponseEntity.notFound().build();
        });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete classroom", description = "Delete a classroom by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting classroom with id: " + id);
        classroomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
