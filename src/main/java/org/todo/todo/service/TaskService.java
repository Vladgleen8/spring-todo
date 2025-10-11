package org.todo.todo.service;

import org.springframework.data.domain.Page;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.dto.UpdateTaskDto;
import org.todo.todo.model.enums.StatusEnum;

public interface TaskService {

    TaskDto createTask(CreateTaskDto task);
    TaskDto updateTask(UpdateTaskDto updateTaskDto);
    void deleteTask(Long id);
    Page<TaskDto> getTasks(int page, int size, String sortBy, StatusEnum status);
    TaskDto getTaskById(Long id);
}
