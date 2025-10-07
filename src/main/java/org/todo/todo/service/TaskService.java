package org.todo.todo.service;

import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;

import java.util.List;


public interface TaskService {

    List<TaskDto> getAllTasks();
    TaskDto saveTask(CreateTaskDto task);
    TaskDto updateTask(Task task);
    void deleteTask(Task task);
    List<TaskDto> getSortedTasks(String sortBy, StatusEnum status);
    List<TaskDto> getTasksByStatus(StatusEnum status);
    TaskDto getTaskById(Long id);
}
