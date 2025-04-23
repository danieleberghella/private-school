package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Course;
import com.daniele.berghella.private_school.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Course save(Course enrollment) {
        return courseRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }
}
