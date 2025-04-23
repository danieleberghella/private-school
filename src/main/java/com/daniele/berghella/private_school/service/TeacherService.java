package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Teacher;
import com.daniele.berghella.private_school.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    public Teacher save(Teacher enrollment) {
        return teacherRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }

}
