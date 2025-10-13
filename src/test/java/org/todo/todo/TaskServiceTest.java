package org.todo.todo;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.dto.UpdateTaskDto;
import org.todo.todo.mapper.TaskMapper;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.repository.TaskRepository;
import org.todo.todo.service.impl.TaskServiceImpl;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDto taskDto;
    private CreateTaskDto createTaskDto;
    private UpdateTaskDto updateTaskDto;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "desc", StatusEnum.TO_DO, "title",
                LocalDate.of(2025, 10, 10), LocalDate.now());

        taskDto = new TaskDto(1L, "title", "desc",
                LocalDate.of(2025, 10, 10), StatusEnum.TO_DO);

        createTaskDto = new CreateTaskDto("title", "desc",
                LocalDate.of(2025, 10, 10), StatusEnum.TO_DO);

        updateTaskDto = new UpdateTaskDto("updated title", "updated desc",
                LocalDate.of(2025, 12, 31), StatusEnum.IN_PROGRESS);
    }


    @Test
    void testCreateTask() {
        when(taskMapper.fromCreateDto(createTaskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(createTaskDto);

        assertNotNull(result);
        assertEquals(taskDto.getId(), result.getId());
        verify(taskMapper).fromCreateDto(createTaskDto);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(task);
    }

    @Test
    void testGetTaskByIdFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.getTaskById(1L);

        assertEquals(taskDto, result);
        verify(taskRepository).findById(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(77L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.getTaskById(77L)
        );

        assertEquals("Task with id 77 not found", exception.getMessage());
        verify(taskRepository).findById(77L);
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        doAnswer(invocation -> {
            UpdateTaskDto dto = invocation.getArgument(0);
            Task target = invocation.getArgument(1);
            target.setTitle(dto.getTitle());
            target.setDescription(dto.getDescription());
            target.setStatus(dto.getStatus());
            return null;
        }).when(taskMapper).updateTaskFromDto(eq(updateTaskDto), any(Task.class));

        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.updateTask(taskId, updateTaskDto);

        assertNotNull(result);
        verify(taskRepository).findById(taskId);
        verify(taskMapper).updateTaskFromDto(eq(updateTaskDto), any(Task.class));
        verify(taskRepository).save(task);
    }

    @Test
    void updateTaskWhenTaskNotFoundThrowsException() {
        Long missingId = 999L;
        when(taskRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTask(missingId, updateTaskDto));

        verify(taskRepository).findById(missingId);
        verifyNoMoreInteractions(taskRepository, taskMapper);
    }

    @Test
    void updateTaskWhenMapperFailsThrowsException() {
        Long id = 1L;
        Task existingTask = new Task();
        when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));

        doThrow(new RuntimeException("Mapping error"))
                .when(taskMapper).updateTaskFromDto(updateTaskDto, existingTask);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> taskService.updateTask(id, updateTaskDto));

        assertEquals("Mapping error", ex.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateTaskShouldBeTransactional() throws NoSuchMethodException {
        Method method = TaskServiceImpl.class.getMethod("updateTask", Long.class, UpdateTaskDto.class);
        assertTrue(method.isAnnotationPresent(Transactional.class));
    }



    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTaskWhenTaskNotFoundThrowsException() {
        Long id = 999L;
        doThrow(new EmptyResultDataAccessException(1)).when(taskRepository).deleteById(id);
        assertThrows(EmptyResultDataAccessException.class, () -> taskService.deleteTask(id));
        verify(taskRepository).deleteById(id);
    }

    @Test
    void deleteTask_shouldBeTransactional() throws NoSuchMethodException {
        Method method = TaskServiceImpl.class.getMethod("deleteTask", Long.class);
        assertTrue(method.isAnnotationPresent(Transactional.class));
    }


    @Test
    void getTasksWithStatusReturnsPagedResult() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdOn"));
        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findByStatus(StatusEnum.TO_DO, pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.getTasks(0, 10, "createdOn", StatusEnum.TO_DO);

        assertEquals(1, result.getTotalElements());
        assertEquals("title", result.getContent().getFirst().getTitle());
        verify(taskRepository).findByStatus(StatusEnum.TO_DO, pageable);
    }

    @Test
    void getTasksWithoutStatusReturnsPagedResult() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("title"));
        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.getTasks(0, 5, "title", null);

        assertEquals(1, result.getTotalElements());
        assertEquals("title", result.getContent().getFirst().getTitle());
        verify(taskRepository).findAll(pageable);
    }

    @Test
    void getTasksWithInvalidSortFieldThrowsException() {
        String invalidSortBy = "unknownField";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(invalidSortBy));

        when(taskRepository.findAll(pageable))
                .thenThrow(new IllegalArgumentException("Invalid sort property: " + invalidSortBy));

        assertThrows(IllegalArgumentException.class, () ->
                taskService.getTasks(0, 10, invalidSortBy, null)
        );

        verify(taskRepository).findAll(pageable);
    }
}
