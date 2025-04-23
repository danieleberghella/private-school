package com.daniele.berghella.private_school.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Long teacherId;
}
