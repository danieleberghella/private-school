package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.SubjectDTO;
import com.daniele.berghella.private_school.model.Course;
import com.daniele.berghella.private_school.model.Subject;
import com.daniele.berghella.private_school.service.CourseService;
import com.daniele.berghella.private_school.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subjects", description = "Operations related to subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all subjects", description = "Retrieve a list of all subjects")
    public List<SubjectDTO> getAll() {
        log.info("Fetching all subjects");
        return subjectService.findAll()
                .stream()
                .map(subject -> mapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Retrieve a single subject by its ID")
    public ResponseEntity<SubjectDTO> getById(@PathVariable Long id) {
        log.info(() -> "Fetching subject with ID: " + id);
        return subjectService.findById(id)
                .map(subject -> ResponseEntity.ok(mapper.map(subject, SubjectDTO.class)))
                .orElseGet(() -> {
                    log.warning(() -> "Subject not found with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create subject", description = "Create a new subject")
    public ResponseEntity<SubjectDTO> create(@RequestBody SubjectDTO dto) {
        log.info("Creating a new subject");
        Optional<Course> courseOpt = courseService.findById(dto.getCourseId());

        if (courseOpt.isEmpty()) {
            log.warning(() -> "Course not found for ID: " + dto.getCourseId());
            return ResponseEntity.badRequest().build();
        }

        Subject subject = Subject.builder()
                .name(dto.getName())
                .course(courseOpt.get())
                .build();

        Subject saved = subjectService.save(subject);
        SubjectDTO responseDTO = mapper.map(saved, SubjectDTO.class);
        log.info(() -> "Subject created with ID: " + saved.getId());

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject", description = "Update an existing subject by ID")
    public ResponseEntity<SubjectDTO> update(@PathVariable Long id, @RequestBody SubjectDTO dto) {
        log.info(() -> "Updating subject with ID: " + id);
        Optional<Subject> subjectOpt = subjectService.findById(id);
        Optional<Course> courseOpt = courseService.findById(dto.getCourseId());

        if (subjectOpt.isEmpty()) {
            log.warning(() -> "Subject not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }

        if (courseOpt.isEmpty()) {
            log.warning(() -> "Course not found for ID: " + dto.getCourseId());
            return ResponseEntity.badRequest().build();
        }

        Subject subject = subjectOpt.get();
        subject.setName(dto.getName());
        subject.setCourse(courseOpt.get());

        Subject updated = subjectService.save(subject);
        SubjectDTO responseDTO = mapper.map(updated, SubjectDTO.class);
        log.info(() -> "Subject updated with ID: " + updated.getId());

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject", description = "Delete a subject by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(() -> "Deleting subject with ID: " + id);
        subjectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
