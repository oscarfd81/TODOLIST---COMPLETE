package com.oscar.todo_rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 30, message = "El usuario debe tener entre 3 y 30 caracteres")
    private String username;

    @Email(message = "Email no válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 4, message = "La contraseña debe tener mínimo 4 caracteres")
    private String password;

    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public void setUsername(String v) { this.username = v; }
    public void setEmail(String v)    { this.email = v; }
    public void setPassword(String v) { this.password = v; }
}
