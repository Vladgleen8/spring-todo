package org.todo.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.todo.todo.model.Task;
import org.todo.todo.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService service;

    @GetMapping
    public List<Task> findAllTasks() {
        return service.findAllTasks();
    }

    @PostMapping("save_task")
    public Task saveTask(@RequestBody Task task) {
        return service.saveTask(task);
    }
}
