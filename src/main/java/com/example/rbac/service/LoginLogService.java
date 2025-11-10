package com.example.rbac.service;

import com.example.rbac.model.LoginLog;
import com.example.rbac.repository.LoginLogRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class LoginLogService {
    private final LoginLogRepository repo;

    public LoginLogService(LoginLogRepository repo) {
        this.repo = repo;
    }

    public void log(String username, HttpServletRequest request, boolean success, String message) {
        LoginLog log = new LoginLog();
        log.setUsername(username);
        log.setIpAddress(getClientIP(request));
        log.setTimestamp(LocalDateTime.now());
        log.setSuccess(success);
        log.setMessage(message);
        log.setUserAgent(request.getHeader("User-Agent"));
        repo.save(log);
    }

    private String getClientIP(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        return xf == null ? request.getRemoteAddr() : xf.split(",")[0];
    }
}
