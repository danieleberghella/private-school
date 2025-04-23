package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {}
