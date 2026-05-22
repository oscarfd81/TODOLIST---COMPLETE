package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.dto.RegisterForm;
import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.security.JwtTokenProvider;
import com.oscar.todo_rest.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/login")
    public String loginPage() { return "auth/login"; }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            User user = (User) auth.getPrincipal();
            String role = user.getRoleName();
            String token = jwtTokenProvider.generateToken(user.getUsername(), role);

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); 
            cookie.setPath("/");
            cookie.setMaxAge(86400); 
            response.addCookie(cookie);

            return "redirect:/tasks/board";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login?error=true";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); 
        response.addCookie(cookie);

        return "redirect:/auth/login?logout=true";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "auth/register";
        try {
            userService.register(form);
            redirectAttributes.addFlashAttribute("success", "¡Cuenta creada! Ya puedes iniciar sesión.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            result.rejectValue("username", "error.form", e.getMessage());
            return "auth/register";
        }
    }
}
