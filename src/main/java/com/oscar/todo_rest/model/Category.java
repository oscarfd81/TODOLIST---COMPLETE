package com.oscar.todo_rest.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cat")
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Task> tasks;

    public Category() {}
    public Category(Long id, String name) { this.id = id; this.name = name; }

    public Long getId()          { return id; }
    public String getName()      { return name; }
    public List<Task> getTasks() { return tasks; }
    public void setId(Long id)       { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
