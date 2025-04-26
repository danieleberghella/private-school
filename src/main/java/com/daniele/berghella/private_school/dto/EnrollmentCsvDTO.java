package com.daniele.berghella.private_school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCsvDTO {
    private Long id;
    private Long studentId;
    private String studentLastName;
    private Long courseId;
    private String courseTitle;
    private String enrollmentDate;
}
