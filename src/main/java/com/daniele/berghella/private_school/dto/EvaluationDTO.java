package com.daniele.berghella.private_school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDTO {
    private Long id;
    private Long studentId;
    private Long subjectId;
    private Double grade;
    private String comment;
}
