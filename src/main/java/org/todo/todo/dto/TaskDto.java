package org.todo.todo.dto;

import lombok.Data;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;

import java.time.LocalDate;

@Data
public class TaskDto {
    private Long id; // теперь возвращаем id
    private String title;
    private String description;
    private LocalDate dueDate;
    private StatusEnum status;

    public static TaskDto fromEntity(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDeadline());
        dto.setStatus(task.getStatus());
        return dto;
    }
}


