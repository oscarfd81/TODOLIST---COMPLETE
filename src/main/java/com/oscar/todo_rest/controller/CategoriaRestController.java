package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.model.Category;
import com.oscar.todo_rest.repos.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    private final CategoryRepository categoryRepository;

    public CategoriaRestController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Category>> listCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<?> createCategory(@RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de la categoría no puede estar vacío."));
        }
        Category cat = new Category();
        cat.setName(name.trim());
        Category saved = categoryRepository.save(cat);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de la categoría no puede estar vacío."));
        }
        return categoryRepository.findById(id).map(cat -> {
            cat.setName(name.trim());
            Category saved = categoryRepository.save(cat);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Categoría eliminada exitosamente"));
    }
}
