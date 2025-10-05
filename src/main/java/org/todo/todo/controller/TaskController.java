package org.todo.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.todo.todo.model.Task;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @GetMapping
    public List<Task> findAllTasks() {
        return List.of(
                Task.builder().description("Описание").status("TO DO").title("Купить хлеб").build()
        );
    }
}
