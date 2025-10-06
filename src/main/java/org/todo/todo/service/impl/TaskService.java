package org.todo.todo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.todo.todo.model.Task;
import org.todo.todo.repository.TaskRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService implements org.todo.todo.service.TaskService {

    private final TaskRepository taskRepository;
    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Task task) {

    }
}
