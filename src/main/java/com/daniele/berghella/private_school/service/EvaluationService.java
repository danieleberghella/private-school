package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Evaluation;
import com.daniele.berghella.private_school.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    public List<Evaluation> findAll() {
        return evaluationRepository.findAll();
    }

    public Optional<Evaluation> findById(Long id) {
        return evaluationRepository.findById(id);
    }

    public Evaluation save(Evaluation enrollment) {
        return evaluationRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        evaluationRepository.deleteById(id);
    }
}
