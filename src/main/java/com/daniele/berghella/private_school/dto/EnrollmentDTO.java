package com.daniele.berghella.private_school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String enrollmentDate;
}
