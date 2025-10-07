package org.todo.todo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService implements org.todo.todo.service.TaskService {

    private final TaskRepository taskRepository;
    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> foundTasks = taskRepository.findAll();
        return foundTasks.stream().map(TaskDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public TaskDto saveTask(CreateTaskDto taskDto) {
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .deadline(taskDto.getDueDate())
                .status(taskDto.getStatus() != null ? taskDto.getStatus() : StatusEnum.TO_DO)
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskDto.fromEntity(savedTask);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public List<TaskDto> getSortedTasks(String sortBy, StatusEnum status) {
        Sort sort = Sort.by(sortBy); // по умолчанию ASC, можно добавить Sort.by(Sort.Direction.DESC, sortBy)
        return taskRepository.findByStatus(status, sort)
                .stream()
                .map(TaskDto::fromEntity)
                .collect(Collectors.toList());
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
