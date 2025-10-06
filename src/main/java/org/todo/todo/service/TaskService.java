package org.todo.todo.service;

import org.springframework.stereotype.Service;
import org.todo.todo.model.Task;

import java.util.List;


public interface TaskService {

    List<Task> findAllTasks();
    Task saveTask(Task task);
    Task updateTask(Task task);
    void deleteTask(Task task);
}
