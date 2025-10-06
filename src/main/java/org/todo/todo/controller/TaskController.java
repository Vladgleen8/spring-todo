package org.todo.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
