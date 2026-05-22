package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users",       userService.findAll());
        model.addAttribute("currentPage", "admin");
        return "admin/users";
    }

    @PostMapping("/users/{id}/promote")
    public String promote(@PathVariable Long id, RedirectAttributes ra) {
        userService.promoteToGestor(id);
        ra.addFlashAttribute("success", "Usuario promovido a GESTOR.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/demote")
    public String demote(@PathVariable Long id, RedirectAttributes ra) {
        userService.demoteToUser(id);
        ra.addFlashAttribute("success", "Usuario degradado a USER.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        userService.delete(id);
        ra.addFlashAttribute("success", "Usuario eliminado.");
        return "redirect:/admin/users";
    }
}
