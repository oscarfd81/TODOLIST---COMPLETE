package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.model.Category;
import com.oscar.todo_rest.model.Tag;
import com.oscar.todo_rest.repos.CategoryRepository;
import com.oscar.todo_rest.repos.TagRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/catalogo")
@PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
public class CatalogoController {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public CatalogoController(CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public String catalogo(Model model) {
        model.addAttribute("categories",  categoryRepository.findAll());
        model.addAttribute("tags",        tagRepository.findAll());
        model.addAttribute("currentPage", "catalogo");
        return "admin/catalogo";
    }


    @PostMapping("/categorias/nueva")
    public String nuevaCategoria(@RequestParam String name, RedirectAttributes ra) {
        if (name == null || name.isBlank()) {
            ra.addFlashAttribute("error", "El nombre de la categoría no puede estar vacío.");
            return "redirect:/admin/catalogo";
        }
        Category cat = new Category();
        cat.setName(name.trim());
        categoryRepository.save(cat);
        ra.addFlashAttribute("success", "Categoría '" + name.trim() + "' creada correctamente.");
        return "redirect:/admin/catalogo";
    }

    @PostMapping("/categorias/{id}/editar")
    public String editarCategoria(@PathVariable Long id, @RequestParam String name, RedirectAttributes ra) {
        categoryRepository.findById(id).ifPresentOrElse(cat -> {
            cat.setName(name.trim());
            categoryRepository.save(cat);
            ra.addFlashAttribute("success", "Categoría actualizada.");
        }, () -> ra.addFlashAttribute("error", "Categoría no encontrada."));
        return "redirect:/admin/catalogo";
    }

    @PostMapping("/categorias/{id}/eliminar")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes ra) {
        categoryRepository.deleteById(id);
        ra.addFlashAttribute("success", "Categoría eliminada.");
        return "redirect:/admin/catalogo";
    }


    @PostMapping("/tags/nuevo")
    public String nuevoTag(@RequestParam String name, RedirectAttributes ra) {
        if (name == null || name.isBlank()) {
            ra.addFlashAttribute("error", "El nombre del tag no puede estar vacío.");
            return "redirect:/admin/catalogo";
        }
        Tag tag = new Tag();
        tag.setName(name.trim());
        tagRepository.save(tag);
        ra.addFlashAttribute("success", "Tag '" + name.trim() + "' creado correctamente.");
        return "redirect:/admin/catalogo";
    }

    @PostMapping("/tags/{id}/editar")
    public String editarTag(@PathVariable Long id, @RequestParam String name, RedirectAttributes ra) {
        tagRepository.findById(id).ifPresentOrElse(tag -> {
            tag.setName(name.trim());
            tagRepository.save(tag);
            ra.addFlashAttribute("success", "Tag actualizado.");
        }, () -> ra.addFlashAttribute("error", "Tag no encontrado."));
        return "redirect:/admin/catalogo";
    }

    @PostMapping("/tags/{id}/eliminar")
    public String eliminarTag(@PathVariable Long id, RedirectAttributes ra) {
        tagRepository.deleteById(id);
        ra.addFlashAttribute("success", "Tag eliminado.");
        return "redirect:/admin/catalogo";
    }
}
