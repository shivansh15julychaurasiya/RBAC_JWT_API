package com.example.rbac.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rbac.dto.UserResponseDTO;
import com.example.rbac.model.User;
import com.example.rbac.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //  Create user (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody Map<String, Object> payload) {
        User user = new User();
        user.setUsername((String) payload.get("username"));
        user.setEmail((String) payload.get("email"));
        user.setPassword((String) payload.get("password"));
        Set<String> roles = Set.copyOf((List<String>) payload.get("roles"));
        return ResponseEntity.ok(userService.createUser(user, roles));
    }

    //  Get all users (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //  Get user by ID (ADMIN or the user themself)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //  Update user roles or enabled status
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") Long id, @RequestBody Map<String, Object> payload) {
        Set<String> roles = payload.containsKey("roles") ?
                Set.copyOf((List<String>) payload.get("roles")) : null;
        Boolean enabled = (Boolean) payload.get("enabled");
        return ResponseEntity.ok(userService.updateUser(id, roles, enabled));
    }

    //  Delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
