package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.model.Tag;
import com.oscar.todo_rest.repos.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagRepository tagRepository;

    public TagRestController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> listTags() {
        return ResponseEntity.ok(tagRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<?> createTag(@RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de la etiqueta no puede estar vacío."));
        }
        Tag tag = new Tag();
        tag.setName(name.trim());
        Tag saved = tagRepository.save(tag);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de la etiqueta no puede estar vacío."));
        }
        return tagRepository.findById(id).map(tag -> {
            tag.setName(name.trim());
            Tag saved = tagRepository.save(tag);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        tagRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Etiqueta eliminada exitosamente"));
    }
}
