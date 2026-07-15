package com.nexora.service;

import com.nexora.dto.LoginRequest;
import com.nexora.dto.RegisterRequest;
import com.nexora.dto.AuthResponse;
import com.nexora.dto.UserDTO;
import com.nexora.entity.User;
import com.nexora.enums.UserRole;
import com.nexora.repository.UserRepository;
import com.nexora.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.builder()
                    .message("Username already exists")
                    .build();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Email already registered")
                    .build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.DEVELOPER)
                .isActive(true)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", request.getUsername());

        return AuthResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("User registered successfully")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(authentication);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        log.info("User logged in successfully: {}", request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login successful")
                .build();
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
