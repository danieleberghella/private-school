package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {}
