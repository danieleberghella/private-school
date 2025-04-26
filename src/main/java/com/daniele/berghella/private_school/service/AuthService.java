package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.dto.LoginRequestDTO;
import com.daniele.berghella.private_school.dto.LoginResponseDTO;
import com.daniele.berghella.private_school.dto.SignUpResponseDTO;
import com.daniele.berghella.private_school.dto.SignupRequestDTO;
import com.daniele.berghella.private_school.model.Users;
import com.daniele.berghella.private_school.util.JwtTokenUtil;
import com.daniele.berghella.private_school.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Log
@Service
public class AuthService {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public LoginResponseDTO authenticateUser(LoginRequestDTO request) {
        log.info("Attempting to authenticate user: " + request.getUsername());

        Users user = usersRepository.findByUsername(request.getUsername());

        if (user != null && BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            String token = jwtTokenUtil.generateToken(user);
            log.info("Authentication successful for user: " + request.getUsername());
            return new LoginResponseDTO(token);
        }

        log.warning("Authentication failed for user: " + request.getUsername());
        throw new RuntimeException("Invalid username or password");
    }

    public SignUpResponseDTO registerUser(SignupRequestDTO request) {
        log.info("Attempting to register new user: " + request.getUsername());

        if (usersRepository.findByUsername(request.getUsername()) != null) {
            log.warning("Registration failed: user already exists: " + request.getUsername());
            throw new RuntimeException("User already exists");
        }

        Users newUser = new Users();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        newUser.setRole("USER");

        usersRepository.save(newUser);

        log.info("User registered successfully: " + newUser.getUsername());
        return new SignUpResponseDTO(newUser.getUsername(), null, newUser.getRole());
    }
}
