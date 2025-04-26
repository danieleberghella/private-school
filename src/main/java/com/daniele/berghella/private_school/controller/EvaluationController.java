package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.EvaluationDTO;
import com.daniele.berghella.private_school.model.Evaluation;
import com.daniele.berghella.private_school.model.Student;
import com.daniele.berghella.private_school.model.Subject;
import com.daniele.berghella.private_school.service.EvaluationService;
import com.daniele.berghella.private_school.service.StudentService;
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
@RequestMapping("/api/evaluations")
@Tag(name = "Evaluations", description = "Operations related to student evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all evaluations", description = "Retrieve a list of all evaluations")
    public List<EvaluationDTO> getAll() {
        log.info("Fetching all evaluations");
        return evaluationService.findAll()
                .stream()
                .map(evaluation -> mapper.map(evaluation, EvaluationDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID", description = "Retrieve a single evaluation by its ID")
    public ResponseEntity<EvaluationDTO> getById(@PathVariable Long id) {
        log.info("Fetching evaluation with id: " + id);
        return evaluationService.findById(id)
                .map(evaluation -> ResponseEntity.ok(mapper.map(evaluation, EvaluationDTO.class)))
                .orElseGet(() -> {
                    log.warning("Evaluation not found with id: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create evaluation", description = "Create a new evaluation")
    public ResponseEntity<EvaluationDTO> create(@RequestBody EvaluationDTO dto) {
        log.info("Creating evaluation for studentId: " + dto.getStudentId() + ", subjectId: " + dto.getSubjectId());

        Optional<Student> studentOpt = studentService.findById(dto.getStudentId());
        Optional<Subject> subjectOpt = subjectService.findById(dto.getSubjectId());

        if (studentOpt.isEmpty() || subjectOpt.isEmpty()) {
            log.warning("Invalid student id or subject id: studentId=" + dto.getStudentId() + ", subjectId=" + dto.getSubjectId());
            return ResponseEntity.badRequest().build();
        }

        Evaluation evaluation = Evaluation.builder()
                .grade(dto.getGrade())
                .comment(dto.getComment())
                .student(studentOpt.get())
                .subject(subjectOpt.get())
                .build();

        Evaluation saved = evaluationService.save(evaluation);
        EvaluationDTO responseDTO = mapper.map(saved, EvaluationDTO.class);

        log.info("Evaluation created successfully with id: " + saved.getId());
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update evaluation", description = "Update an existing evaluation by ID")
    public ResponseEntity<EvaluationDTO> update(@PathVariable Long id, @RequestBody EvaluationDTO dto) {
        log.info("Updating evaluation with id: " + id);

        Optional<Evaluation> evaluationOpt = evaluationService.findById(id);
        Optional<Student> studentOpt = studentService.findById(dto.getStudentId());
        Optional<Subject> subjectOpt = subjectService.findById(dto.getSubjectId());

        if (evaluationOpt.isEmpty()) {
            log.warning("Evaluation not found for update with id: " + id);
            return ResponseEntity.notFound().build();
        }
        if (studentOpt.isEmpty() || subjectOpt.isEmpty()) {
            log.warning("Invalid student id or subject id for update: studentId=" + dto.getStudentId() + ", subjectId=" + dto.getSubjectId());
            return ResponseEntity.badRequest().build();
        }

        Evaluation evaluation = evaluationOpt.get();
        evaluation.setGrade(dto.getGrade());
        evaluation.setComment(dto.getComment());
        evaluation.setStudent(studentOpt.get());
        evaluation.setSubject(subjectOpt.get());

        Evaluation updated = evaluationService.save(evaluation);
        EvaluationDTO responseDTO = mapper.map(updated, EvaluationDTO.class);

        log.info("Evaluation updated successfully with id: " + updated.getId());
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete evaluation", description = "Delete an evaluation by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting evaluation with id: " + id);
        evaluationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
