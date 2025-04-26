package com.daniele.berghella.private_school.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacity;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private Set<Schedule> schedules = new HashSet<>();

    public Classroom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}