package org.todo.todo.dto;

import lombok.Data;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;

import java.time.LocalDate;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private StatusEnum status;
}


