package org.todo.todo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService implements org.todo.todo.service.TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<TaskDto> getTasks(String sortBy, StatusEnum status) {
        Sort sort = Sort.by(sortBy != null ? sortBy : "dueDate");

        List<Task> tasks;

        if (status != null) {
            tasks = taskRepository.findByStatus(status, sort);
        } else {
            tasks = taskRepository.findAll(sort);
        }

        return tasks.stream()
                .map(TaskDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public TaskDto createTask(CreateTaskDto taskDto) {
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .status(taskDto.getStatus() != null ? taskDto.getStatus() : StatusEnum.TO_DO)
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskDto.fromEntity(savedTask);
    }

    @Transactional
    @Override
    public TaskDto updateTask(TaskDto taskDto) {

        Task task = Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .status(taskDto.getStatus())
                .build();

        Task updatedTask = taskRepository.save(task);
        return  TaskDto.fromEntity(updatedTask);
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDto> getTasksByStatus(StatusEnum status) {
        List<Task> tasksByStatus = taskRepository.findByStatus(status);
        return tasksByStatus.stream().map(TaskDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return TaskDto.fromEntity(taskRepository.getTaskById(id));
    }

}
