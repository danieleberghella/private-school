package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Classroom;
import com.daniele.berghella.private_school.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    public Optional<Classroom> findById(Long id) {
        return classroomRepository.findById(id);
    }

    public Classroom save(Classroom enrollment) {
        return classroomRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        classroomRepository.deleteById(id);
    }
}
