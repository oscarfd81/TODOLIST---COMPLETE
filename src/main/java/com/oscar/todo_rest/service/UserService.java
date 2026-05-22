package com.oscar.todo_rest.service;

import com.oscar.todo_rest.dto.RegisterForm;
import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterForm form) {
        if (userRepository.existsByUsername(form.getUsername()))
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        if (userRepository.existsByEmail(form.getEmail()))
            throw new IllegalArgumentException("El email ya está registrado");

        User user = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .isAdmin(false).isGestor(false).isUser(true)
                .build();
        return userRepository.save(user);
    }

    public User update(User user)                          { return userRepository.save(user); }

    public User changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User promoteToGestor(Long id) {
        User user = findById(id);
        user.setGestor(true); user.setAdmin(false); user.setUser(false);
        return userRepository.save(user);
    }

    public User demoteToUser(Long id) {
        User user = findById(id);
        user.setUser(true); user.setGestor(false); user.setAdmin(false);
        return userRepository.save(user);
    }

    public List<User> findAll()  { return userRepository.findAll(); }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void delete(Long id)  { userRepository.deleteById(id); }
}
