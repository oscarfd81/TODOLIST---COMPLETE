package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.dto.RegisterForm;
import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.security.JwtTokenProvider;
import com.oscar.todo_rest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthRestController(AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider,
                              UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = (User) auth.getPrincipal();
        String role = user.getRoleName();
        String token = jwtTokenProvider.generateToken(user.getUsername(), role);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "type", "Bearer",
                "username", user.getUsername(),
                "role", role
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterForm form) {
        try {
            User registered = userService.register(form);
            return ResponseEntity.ok(Map.of(
                    "id", registered.getId(),
                    "username", registered.getUsername(),
                    "email", registered.getEmail(),
                    "role", registered.getRoleName(),
                    "message", "Usuario registrado exitosamente"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
