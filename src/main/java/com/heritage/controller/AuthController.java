package com.heritage.controller;

import com.heritage.dto.AuthResponse;
import com.heritage.dto.LoginRequest;
import com.heritage.dto.RegisterRequest;
import com.heritage.model.User;
import com.heritage.repository.UserRepository;
import com.heritage.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository  userRepo;
    private final JwtUtil         jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return userRepo.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> ResponseEntity.ok(buildAuthResponse(u)))
                .orElse(ResponseEntity.status(401)
                        .body(buildAuthResponse(null)));
    }

    @PostMapping({"/signup", "/register"})
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            return ResponseEntity.status(409)
                    .body(Map.of("error", "Email already registered."));

        User user = new User(
                req.getName(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getRole() != null ? req.getRole() : "Cultural Enthusiast"
        );
        userRepo.save(user);
        return ResponseEntity.ok(buildAuthResponse(user));
    }

    private Object buildAuthResponse(User u) {
        if (u == null) return Map.of("error", "Invalid email or password.");
        return AuthResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole())
                .token(jwtUtil.generateToken(u.getEmail(), u.getRole(), u.getId()))
                .build();
    }
}
