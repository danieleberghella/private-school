package com.daniele.berghella.private_school.repository;

import com.daniele.berghella.private_school.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
}
