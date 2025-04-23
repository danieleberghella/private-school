package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {}
