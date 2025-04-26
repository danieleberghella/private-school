package com.daniele.berghella.private_school.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentPdfDTO {
    private String firstName;
    private String lastName;
    private String email;
    private List<String> courseNames;
}
