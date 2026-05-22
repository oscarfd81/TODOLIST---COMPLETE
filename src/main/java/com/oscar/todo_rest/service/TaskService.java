package com.oscar.todo_rest.service;

import com.oscar.todo_rest.dto.DashboardStats;
import com.oscar.todo_rest.dto.TaskForm;
import com.oscar.todo_rest.enums.enumPrio;
import com.oscar.todo_rest.enums.enumStat;
import com.oscar.todo_rest.model.Category;
import com.oscar.todo_rest.model.Tag;
import com.oscar.todo_rest.model.Task;
import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.repos.CategoryRepository;
import com.oscar.todo_rest.repos.TagRepository;
import com.oscar.todo_rest.repos.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public TaskService(TaskRepository taskRepository,
                       CategoryRepository categoryRepository,
                       TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public List<Task> findByAuthor(User author) {
        return taskRepository.findByAuthor(author);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarea no encontrada: " + id));
    }

    public Task save(TaskForm form, User author) {
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada"));

        List<Tag> tags = (form.getTagIds() == null || form.getTagIds().isEmpty())
                ? List.of()
                : tagRepository.findAllById(form.getTagIds());

        return taskRepository.save(
            Task.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .deadline(form.getDeadline())
                .status(form.getStatus())
                .priority(form.getPriority())
                .important(form.isImportant())
                .author(author)
                .category(category)
                .tags(tags)
                .updatedAt(LocalDateTime.now())
                .build()
        );
    }

    public Task edit(TaskForm form, Long id) {
        Task task = findById(id);

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada"));

        List<Tag> tags = (form.getTagIds() == null || form.getTagIds().isEmpty())
                ? List.of()
                : tagRepository.findAllById(form.getTagIds());

        task.setTitle(form.getTitle());
        task.setDescription(form.getDescription());
        task.setDeadline(form.getDeadline());
        task.setStatus(form.getStatus());
        task.setPriority(form.getPriority());
        task.setImportant(form.isImportant());
        task.setCategory(category);
        task.setTags(tags);
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public void updateStatus(Long id, enumStat status) {
        Task task = findById(id);
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> search(User author, String title, enumStat status, enumPrio priority) {
        return findByAuthor(author).stream()
                .filter(t -> title == null || title.isBlank()
                        || t.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> priority == null || t.getPriority() == priority)
                .toList();
    }

    public DashboardStats getDashboardStats(User author) {
        List<Task> tasks = taskRepository.findByAuthor(author);
        long total      = tasks.size();
        long pendiente  = tasks.stream().filter(t -> t.getStatus() == enumStat.PENDIENTE).count();
        long enProceso  = tasks.stream().filter(t -> t.getStatus() == enumStat.EN_PROCESO).count();
        long hecho      = tasks.stream().filter(t -> t.getStatus() == enumStat.HECHO).count();
        long noHecho    = tasks.stream().filter(t -> t.getStatus() == enumStat.NO_HECHO).count();
        long importantes = tasks.stream().filter(t -> t.isImportant()).count();
        return new DashboardStats(total, pendiente, enProceso, hecho, noHecho, importantes);
    }

    public boolean isOwner(Long taskId, Long userId) {
        return taskRepository.findById(taskId)
                .map(t -> t.getAuthor() != null && t.getAuthor().getId().equals(userId))
                .orElse(false);
    }
}
