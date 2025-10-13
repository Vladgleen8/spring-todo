package org.todo.todo.mapper;

import org.mapstruct.*;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.dto.UpdateTaskDto;
import org.todo.todo.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toDto(Task entity);

    Task fromCreateDto(CreateTaskDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(UpdateTaskDto dto, @MappingTarget Task entity);
}
