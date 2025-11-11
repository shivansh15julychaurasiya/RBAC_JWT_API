package com.example.rbac.service;


import com.example.rbac.dto.UserResponseDTO;
import com.example.rbac.exception.ResourceNotFound;
import com.example.rbac.model.Role;
import com.example.rbac.model.User;
import com.example.rbac.repository.RoleRepository;
import com.example.rbac.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("1234");
        user.setEnabled(true);
        user.setRoles(Set.of(role));
    }

    
    //  1. Test createUser()
    @Test
    void testCreateUser_Success() {
    	
        Set<String> roleNames = Set.of("ADMIN");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("1234")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.createUser(user, roleNames);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals(Set.of("ADMIN"), result.getRoles());
        verify(userRepository, times(1)).save(any(User.class));
    }

    //  2. Test getUserById()
    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserById(1L);

        assertEquals("john", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    //  3. Test getUserById() - User not found
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFound.class, () -> userService.getUserById(1L));
    }

    //  4. Test getAllUsers()
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    //  5. Test updateUser()
    @Test
    void testUpdateUser_Success() {
    	
        Set<String> newRoles = Set.of("ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.updateUser(1L, newRoles, false);

        assertFalse(result.isEnabled());
        verify(userRepository, times(1)).save(any(User.class));
    }

    //  6. Test deleteUser() - Not found
    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFound.class, () -> userService.deleteUser(99L));
    }
}

