package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {

    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/{id}/promote")
    public ResponseEntity<?> promote(@PathVariable Long id) {
        User promoted = userService.promoteToGestor(id);
        return ResponseEntity.ok(Map.of(
                "message", "Usuario promovido a GESTOR exitosamente",
                "user", promoted
        ));
    }

    @PostMapping("/{id}/demote")
    public ResponseEntity<?> demote(@PathVariable Long id) {
        User demoted = userService.demoteToUser(id);
        return ResponseEntity.ok(Map.of(
                "message", "Usuario degradado a USER exitosamente",
                "user", demoted
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado exitosamente"));
    }
}
