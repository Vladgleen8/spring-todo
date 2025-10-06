package org.todo.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.todo.todo.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
