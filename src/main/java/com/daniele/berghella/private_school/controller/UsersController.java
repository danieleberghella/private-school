package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.dto.UserDTO;
import com.daniele.berghella.private_school.model.Users;
import com.daniele.berghella.private_school.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations related to user accounts")
public class UsersController {

    @Autowired
    private UserService usersService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public List<UserDTO> getAll() {
        log.info("Fetching all users");
        return usersService.findAll()
                .stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a single user by ID")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        log.info(() -> "Fetching user with ID: " + id);
        return usersService.findById(id)
                .map(user -> ResponseEntity.ok(mapper.map(user, UserDTO.class)))
                .orElseGet(() -> {
                    log.warning(() -> "User not found with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user by ID")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        log.info(() -> "Updating user with ID: " + id);
        return usersService.findById(id).map(user -> {
            user.setUsername(dto.getUsername());
            user.setRole(dto.getRole());

            Users updated = usersService.save(user);
            UserDTO responseDTO = mapper.map(updated, UserDTO.class);
            log.info(() -> "User updated with ID: " + updated.getId());
            return ResponseEntity.ok(responseDTO);
        }).orElseGet(() -> {
            log.warning(() -> "User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(() -> "Deleting user with ID: " + id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
