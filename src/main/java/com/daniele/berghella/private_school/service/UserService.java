package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Users;
import com.daniele.berghella.private_school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }

    public Users save(Users enrollment) {
        return userRepository.save(enrollment);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
