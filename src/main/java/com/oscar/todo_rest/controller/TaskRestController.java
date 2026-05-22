package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.dto.DashboardStats;
import com.oscar.todo_rest.dto.TaskForm;
import com.oscar.todo_rest.enums.enumPrio;
import com.oscar.todo_rest.enums.enumStat;
import com.oscar.todo_rest.model.Task;
import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> list(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) enumStat status,
            @RequestParam(required = false) enumPrio priority) {
        return ResponseEntity.ok(taskService.search(user, title, status, priority));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Task task = taskService.findById(id);
        if (!task.getAuthor().getId().equals(user.getId()) && !user.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TaskForm form, @AuthenticationPrincipal User user) {
        Task created = taskService.save(form, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TaskForm form, @AuthenticationPrincipal User user) {
        if (!taskService.isOwner(id, user.getId()) && !user.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }
        Task updated = taskService.edit(form, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (!taskService.isOwner(id, user.getId()) && !user.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }
        taskService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Tarea eliminada exitosamente"));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestParam enumStat status, @AuthenticationPrincipal User user) {
        if (!taskService.isOwner(id, user.getId()) && !user.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }
        taskService.updateStatus(id, status);
        return ResponseEntity.ok(Map.of("message", "Estado de tarea actualizado exitosamente"));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> dashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getDashboardStats(user));
    }
}
