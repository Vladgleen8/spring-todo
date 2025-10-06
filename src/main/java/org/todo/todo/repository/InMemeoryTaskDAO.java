/*
package org.todo.todo.repository;

import org.springframework.stereotype.Repository;
import org.todo.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemeoryTaskDAO {
    private final List<Task> TASKS = new ArrayList();

    public List<Task> findAllTasks() {
        return TASKS;
    }

    public Task saveTask(Task task) {
        TASKS.add(task);
        return task;
    }

    public Task updateTask(Task task) {
        var taskIndex = IntStream.range(0, TASKS.size())
                .filter(idx -> TASKS.get(idx).equals(task.getTitle()))
                .findFirst()
                .orElse(-1);

        if (taskIndex != -1) {
            TASKS.set(taskIndex, task);
            return task;
        }
        return null;
    }

    public void deleteTask(Task task) {

    }
}
*/
