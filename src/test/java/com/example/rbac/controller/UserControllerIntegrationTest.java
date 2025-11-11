package com.example.rbac.controller;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.rbac.config.TestSecurityConfig;
import com.example.rbac.model.Role;
import com.example.rbac.model.User;
import com.example.rbac.repository.RoleRepository;
import com.example.rbac.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Role adminRole;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);
    }

    @Test
    void testCreateUser_Success() throws Exception {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("1234");

        mockMvc.perform(post("/api/users")
                        .param("roles", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.roles[0]").value("ADMIN"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user = new User();
        user.setUsername("alex");
        user.setEmail("alex@example.com");
        user.setPassword("1234");
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("alex"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().is4xxClientError());
    }
}
