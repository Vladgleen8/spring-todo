package org.todo.todo.mapper;

import org.mapstruct.*;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.dto.UpdateTaskDto;
import org.todo.todo.model.Task;

import java.util.List;

@Mapper(componentModel = "spring") // чтобы можно было инжектить через @Autowired
public interface TaskMapper {

    TaskDto toDto(Task entity);

    List<TaskDto> toDtoList(List<Task> entities);

    Task fromCreateDto(CreateTaskDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(UpdateTaskDto dto, @MappingTarget Task entity);
}
