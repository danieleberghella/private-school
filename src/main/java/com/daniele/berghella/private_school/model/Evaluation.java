package com.daniele.berghella.private_school.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double grade;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    private String comment;
}
