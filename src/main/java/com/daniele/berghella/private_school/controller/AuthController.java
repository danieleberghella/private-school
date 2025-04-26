package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.LoginRequestDTO;
import com.daniele.berghella.private_school.dto.LoginResponseDTO;
import com.daniele.berghella.private_school.dto.SignUpResponseDTO;
import com.daniele.berghella.private_school.dto.SignupRequestDTO;
import com.daniele.berghella.private_school.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.authenticateUser(request);
    }

    @PostMapping("/signup")
    public SignUpResponseDTO signup(@RequestBody SignupRequestDTO request) {
        return authService.registerUser(request);
    }
}
