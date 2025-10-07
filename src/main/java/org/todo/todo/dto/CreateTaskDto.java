package org.todo.todo.dto;

import lombok.Data;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;

import java.time.LocalDate;

@Data
public class CreateTaskDto {
    private String title;
    private String description;
    private LocalDate dueDate;
    private StatusEnum status; // опционально, можно по умолчанию TO_DO

    public static CreateTaskDto fromEntity(Task task) {
        CreateTaskDto dto = new CreateTaskDto();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        return dto;
    }
}
