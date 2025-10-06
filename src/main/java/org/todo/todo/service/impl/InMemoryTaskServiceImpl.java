package org.todo.todo.service.impl;

import org.springframework.stereotype.Service;
import org.todo.todo.model.Task;
import org.todo.todo.service.TaskService;

import java.util.List;

@Service
public class InMemoryTaskServiceImpl implements TaskService {


    @Override
    public List<Task> findAllTasks() {
        return List.of();
    }

    @Override
    public Task saveTask(Task task) {
        return null;
    }

    @Override
    public Task updateTask(Task task) {
        return null;
    }

    @Override
    public void deleteTask(Task task) {

    }
}
