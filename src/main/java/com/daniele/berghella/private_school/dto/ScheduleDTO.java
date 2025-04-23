package com.daniele.berghella.private_school.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ScheduleDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long courseId;
    private Long subjectId;
    private Long classroomId;
    private Set<Long> attendeeIds;
}
