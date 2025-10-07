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
    public List<TaskDto> getTasks(@RequestParam(required = false, defaultValue = "dueDate") String sortBy,
                                        @RequestParam(required = false ) StatusEnum status) {
        return service.getTasks(sortBy, status);
    }

    @GetMapping("/status/{status}")
    public List<TaskDto> getTasksByStatus(@PathVariable StatusEnum status) {
        return service.getTasksByStatus(status);
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @PostMapping("create_task")
    public TaskDto createTask(@RequestBody CreateTaskDto taskDto) {
        return service.createTask(taskDto);
    }

    @PutMapping
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        return service.updateTask(taskDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }


}
