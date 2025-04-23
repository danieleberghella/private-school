package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Subject;
import com.daniele.berghella.private_school.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }

    public Subject save(Subject enrollment) {
        return subjectRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }
}
