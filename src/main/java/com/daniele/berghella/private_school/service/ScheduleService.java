package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Schedule;
import com.daniele.berghella.private_school.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule save(Schedule enrollment) {
        return scheduleRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }
}
