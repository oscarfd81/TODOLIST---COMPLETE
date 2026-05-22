package com.oscar.todo_rest.controller;

import com.oscar.todo_rest.dto.TaskForm;
import com.oscar.todo_rest.enums.enumPrio;
import com.oscar.todo_rest.enums.enumStat;
import com.oscar.todo_rest.model.Task;
import com.oscar.todo_rest.model.User;
import com.oscar.todo_rest.repos.CategoryRepository;
import com.oscar.todo_rest.repos.TagRepository;
import com.oscar.todo_rest.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public TaskController(TaskService taskService,
                          CategoryRepository categoryRepository,
                          TagRepository tagRepository) {
        this.taskService = taskService;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/board")
    public String board(@AuthenticationPrincipal User user, Model model) {
        List<Task> tasks = taskService.findByAuthor(user);
        model.addAttribute("tasksPendiente", tasks.stream().filter(t -> t.getStatus() == enumStat.PENDIENTE).toList());
        model.addAttribute("tasksEnProceso", tasks.stream().filter(t -> t.getStatus() == enumStat.EN_PROCESO).toList());
        model.addAttribute("tasksHecho",     tasks.stream().filter(t -> t.getStatus() == enumStat.HECHO).toList());
        model.addAttribute("tasksNoHecho",   tasks.stream().filter(t -> t.getStatus() == enumStat.NO_HECHO).toList());
        model.addAttribute("stats",          taskService.getDashboardStats(user));
        model.addAttribute("currentPage",    "board");
        return "tasks/board";
    }

    @GetMapping
    public String list(@AuthenticationPrincipal User user,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) enumStat status,
                       @RequestParam(required = false) enumPrio priority,
                       Model model) {
        model.addAttribute("tasks",          taskService.search(user, title, status, priority));
        model.addAttribute("statuses",       enumStat.values());
        model.addAttribute("priorities",     enumPrio.values());
        model.addAttribute("filterTitle",    title);
        model.addAttribute("filterStatus",   status);
        model.addAttribute("filterPriority", priority);
        model.addAttribute("currentPage",    "list");
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        Task task = taskService.findById(id);
        if (!task.getAuthor().getId().equals(user.getId()) && !user.isAdmin())
            return "redirect:/tasks/board?error=acceso";
        model.addAttribute("task", task);
        return "tasks/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form",       new TaskForm());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("tags",       tagRepository.findAll());
        model.addAttribute("statuses",   enumStat.values());
        model.addAttribute("priorities", enumPrio.values());
        model.addAttribute("currentPage","new");
        return "tasks/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("form") TaskForm form,
                         BindingResult result,
                         @AuthenticationPrincipal User user,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("tags",       tagRepository.findAll());
            model.addAttribute("statuses",   enumStat.values());
            model.addAttribute("priorities", enumPrio.values());
            return "tasks/form";
        }
        taskService.save(form, user);
        redirectAttributes.addFlashAttribute("success", "Tarea creada correctamente.");
        return "redirect:/tasks/board";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        Task task = taskService.findById(id);
        if (!taskService.isOwner(id, user.getId()) && !user.isAdmin())
            return "redirect:/tasks/board?error=acceso";

        TaskForm form = new TaskForm();
        form.setTitle(task.getTitle());
        form.setDescription(task.getDescription());
        form.setStatus(task.getStatus());
        form.setPriority(task.getPriority());
        form.setDeadline(task.getDeadline());
        form.setImportant(task.isImportant());
        form.setCategoryId(task.getCategory() != null ? task.getCategory().getId() : null);
        form.setTagIds(task.getTags().stream().map(t -> t.getId()).toList());

        model.addAttribute("form",       form);
        model.addAttribute("taskId",     id);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("tags",       tagRepository.findAll());
        model.addAttribute("statuses",   enumStat.values());
        model.addAttribute("priorities", enumPrio.values());
        model.addAttribute("editMode",   true);
        return "tasks/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") TaskForm form,
                         BindingResult result,
                         @AuthenticationPrincipal User user,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (!taskService.isOwner(id, user.getId()) && !user.isAdmin())
            return "redirect:/tasks/board?error=acceso";
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("tags",       tagRepository.findAll());
            model.addAttribute("statuses",   enumStat.values());
            model.addAttribute("priorities", enumPrio.values());
            model.addAttribute("taskId",     id);
            model.addAttribute("editMode",   true);
            return "tasks/form";
        }
        taskService.edit(form, id);
        redirectAttributes.addFlashAttribute("success", "Tarea actualizada correctamente.");
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal User user,
                         RedirectAttributes redirectAttributes) {
        if (!taskService.isOwner(id, user.getId()) && !user.isAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar esta tarea.");
            return "redirect:/tasks/board";
        }
        taskService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Tarea eliminada.");
        return "redirect:/tasks/board";
    }

    @PostMapping("/{id}/status")
    @ResponseBody
    public String changeStatus(@PathVariable Long id,
                               @RequestParam enumStat status,
                               @AuthenticationPrincipal User user) {
        if (!taskService.isOwner(id, user.getId()) && !user.isAdmin()) return "error:acceso";
        taskService.updateStatus(id, status);
        return "ok";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("stats",       taskService.getDashboardStats(user));
        model.addAttribute("tasks",       taskService.findByAuthor(user));
        model.addAttribute("currentPage", "dashboard");
        return "tasks/dashboard";
    }
}
