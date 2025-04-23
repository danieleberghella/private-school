package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.EnrollmentDTO;
import com.daniele.berghella.private_school.model.Course;
import com.daniele.berghella.private_school.model.Enrollment;
import com.daniele.berghella.private_school.model.Student;
import com.daniele.berghella.private_school.service.CourseService;
import com.daniele.berghella.private_school.service.EnrollmentService;
import com.daniele.berghella.private_school.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enrollments")
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
    public List<EnrollmentDTO> getAll() {
        return enrollmentService.findAll().stream().map(e -> {
            EnrollmentDTO dto = new EnrollmentDTO();
            dto.setId(e.getId());
            dto.setStudentId(e.getStudent().getId());
            dto.setCourseId(e.getCourse().getId());
            dto.setEnrollmentDate(e.getEnrollmentDate());
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable Long id) {
        return enrollmentService.findById(id).map(e -> {
            EnrollmentDTO dto = new EnrollmentDTO();
            dto.setId(e.getId());
            dto.setStudentId(e.getStudent().getId());
            dto.setCourseId(e.getCourse().getId());
            dto.setEnrollmentDate(e.getEnrollmentDate());
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EnrollmentDTO> create(@RequestBody EnrollmentDTO dto) {
        Student student = studentService.findById(dto.getStudentId()).orElse(null);
        Course course = courseService.findById(dto.getCourseId()).orElse(null);
        if (student == null || course == null) return ResponseEntity.badRequest().build();

        Enrollment e = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollmentDate(dto.getEnrollmentDate())
                .build();
        e = enrollmentService.save(e);

        dto.setId(e.getId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> update(@PathVariable Long id, @RequestBody EnrollmentDTO dto) {
        Optional<Enrollment> optionalEnrollment = enrollmentService.findById(id);
        if (optionalEnrollment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentService.findById(dto.getStudentId()).orElse(null);
        Course course = courseService.findById(dto.getCourseId()).orElse(null);
        if (student == null || course == null) {
            return ResponseEntity.badRequest().build();
        }

        Enrollment e = optionalEnrollment.get();
        e.setStudent(student);
        e.setCourse(course);
        e.setEnrollmentDate(dto.getEnrollmentDate());

        Enrollment updated = enrollmentService.save(e);

        dto.setId(updated.getId());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
