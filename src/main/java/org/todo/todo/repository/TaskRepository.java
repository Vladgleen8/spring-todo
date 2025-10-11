package org.todo.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatus(StatusEnum status, Pageable pageable);

}
