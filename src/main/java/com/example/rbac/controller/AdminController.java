package com.example.rbac.controller;

import com.example.rbac.model.LoginLog;
import com.example.rbac.repository.LoginLogRepository;
import com.example.rbac.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final LoginLogRepository loginLogRepository;

    @GetMapping("/login-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<LoginLog>> getLogs(@RequestParam(required = false) String username) {
        List<LoginLog> logs;
        if (username == null) logs = loginLogRepository.findAll();
        else logs = loginLogRepository.findByUsernameOrderByTimestampDesc(username);
        return ApiResponse.success("Fetched logs", logs);
    }
}
