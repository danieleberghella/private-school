package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.CourseDTO;
import com.daniele.berghella.private_school.model.Course;
import com.daniele.berghella.private_school.model.Teacher;
import com.daniele.berghella.private_school.service.CourseService;
import com.daniele.berghella.private_school.service.TeacherService;
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
@RequestMapping("/api/courses")
@Tag(name = "Course", description = "Operations related to courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve a list of all courses")
    public List<CourseDTO> getAll() {
        log.info("Fetching all courses");
        return courseService.findAll().stream()
                .map(course -> {
                    CourseDTO dto = mapper.map(course, CourseDTO.class);
                    if (course.getTeacher() != null) {
                        dto.setTeacherId(course.getTeacher().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by its ID")
    public ResponseEntity<CourseDTO> getById(@PathVariable Long id) {
        log.info("Fetching course with id: " + id);
        return courseService.findById(id)
                .map(course -> {
                    CourseDTO dto = mapper.map(course, CourseDTO.class);
                    if (course.getTeacher() != null) {
                        dto.setTeacherId(course.getTeacher().getId());
                    }
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> {
                    log.warning("Course not found with id: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create course", description = "Create a new course")
    public ResponseEntity<CourseDTO> create(@RequestBody CourseDTO dto) {
        log.info("Creating course with title: " + dto.getTitle());

        Optional<Teacher> teacher = teacherService.findById(dto.getTeacherId());
        if (teacher.isEmpty()) {
            log.warning("Teacher not found with id: " + dto.getTeacherId());
            return ResponseEntity.badRequest().build();
        }

        Course course = mapper.map(dto, Course.class);
        course.setTeacher(teacher.get());

        Course saved = courseService.save(course);
        CourseDTO result = mapper.map(saved, CourseDTO.class);
        result.setTeacherId(saved.getTeacher().getId());

        log.info("Course created successfully with id: " + saved.getId());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Update an existing course by ID")
    public ResponseEntity<CourseDTO> update(@PathVariable Long id, @RequestBody CourseDTO dto) {
        log.info("Updating course with id: " + id);

        Optional<Course> optionalCourse = courseService.findById(id);
        if (optionalCourse.isEmpty()) {
            log.warning("Course not found for update with id: " + id);
            return ResponseEntity.notFound().build();
        }

        Optional<Teacher> teacher = teacherService.findById(dto.getTeacherId());
        if (teacher.isEmpty()) {
            log.warning("Teacher not found with id: " + dto.getTeacherId());
            return ResponseEntity.badRequest().build();
        }

        Course course = optionalCourse.get();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setTeacher(teacher.get());

        Course updated = courseService.save(course);
        CourseDTO result = mapper.map(updated, CourseDTO.class);
        result.setTeacherId(updated.getTeacher().getId());

        log.info("Course updated successfully with id: " + updated.getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Delete a specific course by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting course with id: " + id);
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
