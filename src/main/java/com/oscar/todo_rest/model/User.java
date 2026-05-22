package com.oscar.todo_rest.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_entity")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private boolean isAdmin  = false;
    private boolean isGestor = false;
    private boolean isUser   = false;

    public User() {}

    public User(Long id, String username, String email, String password,
                boolean isAdmin, boolean isGestor, boolean isUser) {
        this.id = id; this.username = username; this.email = email;
        this.password = password; this.isAdmin = isAdmin;
        this.isGestor = isGestor; this.isUser = isUser;
    }

    // Getters
    public Long getId()          { return id; }
    public String getEmail()     { return email; }
    public boolean isAdmin()     { return isAdmin; }
    public boolean isGestor()    { return isGestor; }
    public boolean isUser()      { return isUser; }

    // Setters
    public void setId(Long id)               { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setAdmin(boolean admin)      { this.isAdmin = admin; }
    public void setGestor(boolean gestor)    { this.isGestor = gestor; }
    public void setUser(boolean user)        { this.isUser = user; }

    // UserDetails
    @Override public String getUsername()  { return username; }
    @Override public String getPassword()  { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + (isAdmin ? "ADMIN" : isGestor ? "GESTOR" : "USER");
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }

    public String getRoleName() {
        return isAdmin ? "ADMIN" : isGestor ? "GESTOR" : "USER";
    }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String username, email, password;
        private boolean isAdmin = false, isGestor = false, isUser = false;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder username(String v)        { this.username = v; return this; }
        public Builder email(String v)           { this.email = v; return this; }
        public Builder password(String v)        { this.password = v; return this; }
        public Builder isAdmin(boolean v)        { this.isAdmin = v; return this; }
        public Builder isGestor(boolean v)       { this.isGestor = v; return this; }
        public Builder isUser(boolean v)         { this.isUser = v; return this; }

        public User build() {
            return new User(id, username, email, password, isAdmin, isGestor, isUser);
        }
    }
}
