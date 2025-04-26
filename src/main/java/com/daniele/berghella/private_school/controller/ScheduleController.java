package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.ScheduleDTO;
import com.daniele.berghella.private_school.model.*;
import com.daniele.berghella.private_school.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedule", description = "Operations related to schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all schedules", description = "Retrieve a list of all schedules")
    public List<ScheduleDTO> getAll() {
        log.info("Fetching all schedules");
        return scheduleService.findAll().stream()
                .map(schedule -> mapper.map(schedule, ScheduleDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID", description = "Retrieve a specific schedule by its ID")
    public ResponseEntity<ScheduleDTO> getById(@PathVariable Long id) {
        log.info(() -> "Fetching schedule with ID: " + id);
        return scheduleService.findById(id)
                .map(schedule -> mapper.map(schedule, ScheduleDTO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warning(() -> "Schedule not found with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create new schedule", description = "Add a new schedule to the system")
    public ResponseEntity<ScheduleDTO> create(@RequestBody ScheduleDTO dto) {
        log.info("Creating a new schedule");

        Optional<Course> course = courseService.findById(dto.getCourseId());
        Optional<Subject> subject = subjectService.findById(dto.getSubjectId());
        Optional<Classroom> classroom = classroomService.findById(dto.getClassroomId());

        if (course.isEmpty() || subject.isEmpty() || classroom.isEmpty()) {
            log.warning("Course, Subject or Classroom not found. Cannot create schedule.");
            return ResponseEntity.badRequest().build();
        }

        Set<Student> attendees = dto.getAttendeeIds() != null
                ? dto.getAttendeeIds().stream()
                .map(studentService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet())
                : Set.of();

        Schedule schedule = mapper.map(dto, Schedule.class);
        schedule.setCourse(course.get());
        schedule.setSubject(subject.get());
        schedule.setClassroom(classroom.get());
        schedule.setAttendees(attendees);

        Schedule saved = scheduleService.save(schedule);
        log.info(() -> "Schedule created with ID: " + saved.getId());
        return ResponseEntity.ok(mapper.map(saved, ScheduleDTO.class));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update schedule", description = "Update an existing schedule by ID")
    public ResponseEntity<ScheduleDTO> update(@PathVariable Long id, @RequestBody ScheduleDTO dto) {
        log.info(() -> "Updating schedule with ID: " + id);

        Optional<Schedule> optionalSchedule = scheduleService.findById(id);
        if (optionalSchedule.isEmpty()) {
            log.warning(() -> "Schedule not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }

        Optional<Course> course = courseService.findById(dto.getCourseId());
        Optional<Subject> subject = subjectService.findById(dto.getSubjectId());
        Optional<Classroom> classroom = classroomService.findById(dto.getClassroomId());

        if (course.isEmpty() || subject.isEmpty() || classroom.isEmpty()) {
            log.warning("Course, Subject or Classroom not found. Cannot update schedule.");
            return ResponseEntity.badRequest().build();
        }

        Set<Student> attendees = dto.getAttendeeIds() != null
                ? dto.getAttendeeIds().stream()
                .map(studentService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet())
                : Set.of();

        Schedule schedule = optionalSchedule.get();
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setCourse(course.get());
        schedule.setSubject(subject.get());
        schedule.setClassroom(classroom.get());
        schedule.setAttendees(attendees);

        Schedule updated = scheduleService.save(schedule);
        log.info(() -> "Schedule updated with ID: " + updated.getId());
        ScheduleDTO updatedDTO = mapper.map(updated, ScheduleDTO.class);

        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete schedule", description = "Delete a schedule by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(() -> "Deleting schedule with ID: " + id);
        scheduleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
