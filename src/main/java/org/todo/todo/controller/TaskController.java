package org.todo.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService service;

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return service.getAllTasks();
    }

    @GetMapping("/status/{status}")
    public List<TaskDto> getTasksByStatus(@PathVariable StatusEnum status) {
        return service.getTasksByStatus(status);
    }

    @GetMapping
    public List<TaskDto> getSortedTasks(@RequestParam(required = false, defaultValue = "deadline") String sortBy,
                                        @RequestParam(required = false ) StatusEnum status) {
        return service.getSortedTasks(sortBy, status);
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @PostMapping("create_task")
    public TaskDto createTask(@RequestBody CreateTaskDto taskDto) {
        return service.saveTask(taskDto);
    }




}
