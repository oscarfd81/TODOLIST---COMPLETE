package com.oscar.todo_rest.model;

import com.oscar.todo_rest.enums.enumPrio;
import com.oscar.todo_rest.enums.enumStat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime deadline;
    private boolean important;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private enumStat status;

    @Enumerated(EnumType.STRING)
    private enumPrio priority;

    @ManyToOne
    private User author;

    @ManyToOne
    private Category category;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    public Task() {}

    // Getters
    public Long getId()                  { return id; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public String getTitle()             { return title; }
    public String getDescription()       { return description; }
    public LocalDateTime getDeadline()   { return deadline; }
    public boolean isImportant()         { return important; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }
    public enumStat getStatus()          { return status; }
    public enumPrio getPriority()        { return priority; }
    public User getAuthor()              { return author; }
    public Category getCategory()        { return category; }
    public List<Tag> getTags()           { return tags; }

    // Setters
    public void setId(Long id)                       { this.id = id; }
    public void setCreatedAt(LocalDateTime v)        { this.createdAt = v; }
    public void setTitle(String title)               { this.title = title; }
    public void setDescription(String description)   { this.description = description; }
    public void setDeadline(LocalDateTime deadline)  { this.deadline = deadline; }
    public void setImportant(boolean important)      { this.important = important; }
    public void setUpdatedAt(LocalDateTime v)        { this.updatedAt = v; }
    public void setStatus(enumStat status)           { this.status = status; }
    public void setPriority(enumPrio priority)       { this.priority = priority; }
    public void setAuthor(User author)               { this.author = author; }
    public void setCategory(Category category)       { this.category = category; }
    public void setTags(List<Tag> tags)              { this.tags = tags; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String title, description;
        private LocalDateTime deadline, updatedAt;
        private boolean important;
        private enumStat status;
        private enumPrio priority;
        private User author;
        private Category category;
        private List<Tag> tags = new ArrayList<>();

        public Builder title(String v)           { this.title = v; return this; }
        public Builder description(String v)     { this.description = v; return this; }
        public Builder deadline(LocalDateTime v) { this.deadline = v; return this; }
        public Builder updatedAt(LocalDateTime v){ this.updatedAt = v; return this; }
        public Builder important(boolean v)      { this.important = v; return this; }
        public Builder status(enumStat v)        { this.status = v; return this; }
        public Builder priority(enumPrio v)      { this.priority = v; return this; }
        public Builder author(User v)            { this.author = v; return this; }
        public Builder category(Category v)      { this.category = v; return this; }
        public Builder tags(List<Tag> v)         { this.tags = v; return this; }

        public Task build() {
            Task t = new Task();
            t.title = title; t.description = description;
            t.deadline = deadline; t.updatedAt = updatedAt;
            t.important = important; t.status = status;
            t.priority = priority; t.author = author;
            t.category = category; t.tags = tags;
            return t;
        }
    }
}
