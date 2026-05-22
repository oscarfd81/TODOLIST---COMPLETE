package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profilePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user",        user);
        model.addAttribute("currentPage", "profile");
        return "profile";
    }

    @PostMapping("/password")
    public String changePassword(@AuthenticationPrincipal User user,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes ra) {
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/profile";
        }
        if (newPassword.length() < 4) {
            ra.addFlashAttribute("error", "La contraseña debe tener al menos 4 caracteres.");
            return "redirect:/profile";
        }
        userService.changePassword(user, newPassword);
        ra.addFlashAttribute("success", "Contraseña actualizada correctamente.");
        return "redirect:/profile";
    }
}
