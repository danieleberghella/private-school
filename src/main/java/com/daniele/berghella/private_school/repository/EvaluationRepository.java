package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {}
