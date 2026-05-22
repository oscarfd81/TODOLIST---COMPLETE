package com.oscar.todo_rest.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tagd")
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks;

    public Tag() {}
    public Tag(Long id, String name) { this.id = id; this.name = name; }

    public Long getId()          { return id; }
    public String getName()      { return name; }
    public List<Task> getTasks() { return tasks; }
    public void setId(Long id)       { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
