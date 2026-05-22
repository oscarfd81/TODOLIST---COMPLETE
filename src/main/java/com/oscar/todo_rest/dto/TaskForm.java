package com.oscar.todo_rest.dto;

import com.oscar.todo_rest.enums.enumPrio;
import com.oscar.todo_rest.enums.enumStat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskForm {

    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    private String description;

    @NotNull(message = "Debes seleccionar un estado")
    private enumStat status;

    @NotNull(message = "Debes seleccionar una prioridad")
    private enumPrio priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    @NotNull(message = "Debes seleccionar una categoría")
    private Long categoryId;

    private List<Long> tagIds = new ArrayList<>();

    private boolean important;

    // ── Getters ──
    public String getTitle()             { return title; }
    public String getDescription()       { return description; }
    public enumStat getStatus()          { return status; }
    public enumPrio getPriority()        { return priority; }
    public LocalDateTime getDeadline()   { return deadline; }
    public Long getCategoryId()          { return categoryId; }
    public List<Long> getTagIds()        { return tagIds; }
    public boolean isImportant()         { return important; }

    // ── Setters ──
    public void setTitle(String v)           { this.title = v; }
    public void setDescription(String v)     { this.description = v; }
    public void setStatus(enumStat v)        { this.status = v; }
    public void setPriority(enumPrio v)      { this.priority = v; }
    public void setDeadline(LocalDateTime v) { this.deadline = v; }
    public void setCategoryId(Long v)        { this.categoryId = v; }
    public void setTagIds(List<Long> v)      { this.tagIds = v != null ? v : new ArrayList<>(); }
    public void setImportant(boolean v)      { this.important = v; }
}
