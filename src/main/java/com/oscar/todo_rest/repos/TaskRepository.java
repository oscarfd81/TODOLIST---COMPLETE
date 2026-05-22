package com.oscar.todo_rest.repos;

import com.oscar.todo_rest.enums.enumPrio;
import com.oscar.todo_rest.enums.enumStat;
import com.oscar.todo_rest.model.Category;
import com.oscar.todo_rest.model.Tag;
import com.oscar.todo_rest.model.Task;
import com.oscar.todo_rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthor(User author);
    List<Task> findByCategory(Category category);
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByTagsContains(Tag tag);
    List<Task> findByStatus(enumStat status);
    List<Task> findByPriority(enumPrio priority);
    List<Task> findByAuthorAndStatus(User author, enumStat status);
    List<Task> findByAuthorAndImportantTrue(User author);
}
