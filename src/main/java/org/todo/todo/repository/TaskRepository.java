package org.todo.todo.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(StatusEnum status, Sort sort);

    List<Task> findByStatus(StatusEnum status);

    Task getTaskById(long id);
}
