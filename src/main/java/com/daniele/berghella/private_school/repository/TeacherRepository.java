package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {}