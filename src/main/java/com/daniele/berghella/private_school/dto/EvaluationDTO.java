package com.daniele.berghella.private_school.dto;

import lombok.Data;

@Data
public class EvaluationDTO {
    private Long id;
    private Long studentId;
    private Long subjectId;
    private Double grade;
    private String comment;
}
