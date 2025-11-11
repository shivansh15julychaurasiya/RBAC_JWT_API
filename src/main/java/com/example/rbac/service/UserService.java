package com.example.rbac.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rbac.dto.UserResponseDTO;
import com.example.rbac.exception.ResourceNotFound;
import com.example.rbac.model.Role;
import com.example.rbac.model.User;
import com.example.rbac.repository.RoleRepository;
import com.example.rbac.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Create new user
    public UserResponseDTO createUser(User user, Set<String> roleNames) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFound("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);
        return toDTO(user);
    }

    // Fetch all users
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Fetch by ID
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User", "id", id));
        return toDTO(user);
    }


    // Update user roles or status
    public UserResponseDTO updateUser(Long id, Set<String> newRoles, Boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User", "id", id));

        if (newRoles != null && !newRoles.isEmpty()) {
            Set<Role> roles = newRoles.stream()
                    .map(r -> roleRepository.findByName(r)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + r)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        if (enabled != null) {
            user.setEnabled(enabled);
        }

        userRepository.save(user);
        return toDTO(user);
    }

    // Delete user
    public void deleteUser(Long id) {
    	if (!userRepository.existsById(id)) {
    	    throw new ResourceNotFound("User", "id", id);
    	}
    }

    // Helper: convert entity to DTO
    private UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        dto.setRoles(user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return dto;
    }
}
