package org.todo.todo.service;

import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.model.enums.StatusEnum;

import java.util.List;


public interface TaskService {

    TaskDto createTask(CreateTaskDto task);
    TaskDto updateTask(TaskDto taskDto);
    void deleteTask(Long id);
    List<TaskDto> getTasks(String sortBy, StatusEnum status);
    TaskDto getTaskById(Long id);
}
