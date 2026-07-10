package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.LoginRequest;
import com.samedconnect.pharmacy_backend.dto.request.RegisterRequest;
import com.samedconnect.pharmacy_backend.dto.response.AuthResponse;
import com.samedconnect.pharmacy_backend.service.AuthService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(Response.success("Registration successful", response));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(Response.success("Login successful", response));
    }
}