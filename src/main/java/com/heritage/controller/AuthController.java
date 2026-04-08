package com.heritage.controller;

import com.heritage.model.User;
import com.heritage.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password."));
        }
        User u = user.get();
        return ResponseEntity.ok(Map.of(
            "id", u.getId(), "name", u.getName(), "email", u.getEmail(), "role", u.getRole()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (userRepo.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.status(409).body(Map.of("error", "Email already registered."));
        }
        User saved = userRepo.save(newUser);
        return ResponseEntity.ok(Map.of(
            "id", saved.getId(), "name", saved.getName(), "email", saved.getEmail(), "role", saved.getRole()
        ));
    }
}
