package com.nexora.controller;

import com.nexora.dto.AuthResponse;
import com.nexora.dto.LoginRequest;
import com.nexora.dto.RegisterRequest;
import com.nexora.dto.UserDTO;
import com.nexora.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("Registration request for user: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        if (response.getMessage().contains("successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("Login request for user: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO userDTO = authService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }
}
