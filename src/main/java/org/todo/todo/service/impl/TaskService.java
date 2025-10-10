package org.todo.todo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.dto.UpdateTaskDto;
import org.todo.todo.mapper.TaskMapper;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.repository.TaskRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService implements org.todo.todo.service.TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    @Override
    public List<TaskDto> getTasks(String sortBy, StatusEnum status) {
        Sort sort = Sort.by(sortBy);

        List<Task> tasks = (status != null)
                ? taskRepository.findByStatus(status, sort)
                : taskRepository.findAll(sort);


        return taskMapper.toDtoList(tasks);
    }

    @Transactional
    @Override
    public TaskDto createTask(CreateTaskDto taskDto) {
        Task task = taskMapper.fromCreateDto(taskDto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Transactional
    @Override
    public TaskDto updateTask(UpdateTaskDto updateTaskDto) {
        Task existingTaskEntity = getTaskEntityById(updateTaskDto.getId());
        taskMapper.updateTaskFromDto(updateTaskDto, existingTaskEntity);
        Task updated = taskRepository.save(existingTaskEntity);
        return taskMapper.toDto(updated);
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = getTaskEntityById(id);
        return taskMapper.toDto(task);
    }

    private Task getTaskEntityById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));
    }
}
