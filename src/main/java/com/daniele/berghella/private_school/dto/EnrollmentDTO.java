package com.daniele.berghella.private_school.dto;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String enrollmentDate;
}
