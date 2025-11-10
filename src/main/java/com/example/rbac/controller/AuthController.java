package com.example.rbac.controller;

import com.example.rbac.dto.AuthResponse;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.service.LoginLogService;
import com.example.rbac.security.JwtUtils;
import com.example.rbac.util.ApiResponse;
import com.example.rbac.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final LoginLogService loginLogService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest req, HttpServletRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
           
            UserDetails ud = (UserDetails) auth.getPrincipal();
            String token = jwtUtils.generateToken(ud);
            System.out.println("Token----------------"+token);
            loginLogService.log(req.getUsername(), request, true, AppConstants.MSG_LOGIN_SUCCESS);

            AuthResponse ar = new AuthResponse(token, "Bearer", jwtUtils.getExpirationMs());
            return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_LOGIN_SUCCESS, ar));
        } catch (Exception e) {
            loginLogService.log(req.getUsername(), request, false, AppConstants.MSG_LOGIN_FAILED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(AppConstants.MSG_LOGIN_FAILED));
        }
    }
}
