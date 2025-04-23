package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {}