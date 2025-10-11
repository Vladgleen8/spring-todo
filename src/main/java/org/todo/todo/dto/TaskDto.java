package org.todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.todo.todo.model.enums.StatusEnum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private StatusEnum status;
}


