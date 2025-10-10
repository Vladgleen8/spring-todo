package org.todo.todo.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.dto.UpdateTaskDto;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.service.TaskService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService service;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid CreateTaskDto taskDto) {
        TaskDto created = service.createTask(taskDto);
        URI location = URI.create("/api/v1/tasks/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
            TaskDto task = service.getTaskById(id);
            return ResponseEntity.ok(task);
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody @Valid UpdateTaskDto updateTaskDto) {
            TaskDto updated = service.updateTask(updateTaskDto);
            return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
            service.deleteTask(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(@RequestParam(required = false, defaultValue = "createdOn") String sortBy,
                                        @RequestParam(required = false ) StatusEnum status) {
        List<TaskDto> tasks = service.getTasks(sortBy, status);
        return ResponseEntity.ok(tasks);
    }





}
