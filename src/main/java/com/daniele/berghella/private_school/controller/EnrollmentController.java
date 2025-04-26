package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.EnrollmentDTO;
import com.daniele.berghella.private_school.model.Course;
import com.daniele.berghella.private_school.model.Enrollment;
import com.daniele.berghella.private_school.model.Student;
import com.daniele.berghella.private_school.service.CourseService;
import com.daniele.berghella.private_school.service.EnrollmentService;
import com.daniele.berghella.private_school.service.StudentService;
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
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollment", description = "Operations related to course enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Retrieve a list of all course enrollments")
    public List<EnrollmentDTO> getAll() {
        log.info("Fetching all enrollments");
        return enrollmentService.findAll().stream()
                .map(e -> {
                    EnrollmentDTO dto = mapper.map(e, EnrollmentDTO.class);
                    dto.setStudentId(e.getStudent().getId());
                    dto.setCourseId(e.getCourse().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve a specific enrollment by its ID")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable Long id) {
        log.info("Fetching enrollment with id: " + id);
        return enrollmentService.findById(id)
                .map(e -> {
                    EnrollmentDTO dto = mapper.map(e, EnrollmentDTO.class);
                    dto.setStudentId(e.getStudent().getId());
                    dto.setCourseId(e.getCourse().getId());
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> {
                    log.warning("Enrollment not found with id: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create enrollment", description = "Create a new course enrollment")
    public ResponseEntity<EnrollmentDTO> create(@RequestBody EnrollmentDTO dto) {
        log.info("Creating enrollment for student id: " + dto.getStudentId() + ", course id: " + dto.getCourseId());

        Student student = studentService.findById(dto.getStudentId()).orElse(null);
        Course course = courseService.findById(dto.getCourseId()).orElse(null);
        if (student == null || course == null) {
            log.warning("Invalid student id or course id: studentId=" + dto.getStudentId() + ", courseId=" + dto.getCourseId());
            return ResponseEntity.badRequest().build();
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollmentDate(dto.getEnrollmentDate())
                .build();

        Enrollment saved = enrollmentService.save(enrollment);
        EnrollmentDTO result = mapper.map(saved, EnrollmentDTO.class);
        result.setStudentId(saved.getStudent().getId());
        result.setCourseId(saved.getCourse().getId());

        log.info("Enrollment created successfully with id: " + saved.getId());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update enrollment", description = "Update an existing course enrollment by ID")
    public ResponseEntity<EnrollmentDTO> update(@PathVariable Long id, @RequestBody EnrollmentDTO dto) {
        log.info("Updating enrollment with id: " + id);

        Optional<Enrollment> optionalEnrollment = enrollmentService.findById(id);
        if (optionalEnrollment.isEmpty()) {
            log.warning("Enrollment not found for update with id: " + id);
            return ResponseEntity.notFound().build();
        }

        Student student = studentService.findById(dto.getStudentId()).orElse(null);
        Course course = courseService.findById(dto.getCourseId()).orElse(null);
        if (student == null || course == null) {
            log.warning("Invalid student id or course id for update: studentId=" + dto.getStudentId() + ", courseId=" + dto.getCourseId());
            return ResponseEntity.badRequest().build();
        }

        Enrollment e = optionalEnrollment.get();
        e.setStudent(student);
        e.setCourse(course);
        e.setEnrollmentDate(dto.getEnrollmentDate());

        Enrollment updated = enrollmentService.save(e);
        EnrollmentDTO result = mapper.map(updated, EnrollmentDTO.class);
        result.setStudentId(updated.getStudent().getId());
        result.setCourseId(updated.getCourse().getId());

        log.info("Enrollment updated successfully with id: " + updated.getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment", description = "Delete a specific enrollment by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting enrollment with id: " + id);
        enrollmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
