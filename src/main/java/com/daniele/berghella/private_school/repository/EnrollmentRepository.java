package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {}
