package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Student;
import com.daniele.berghella.private_school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public Student save(Student enrollment) {
        return studentRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
