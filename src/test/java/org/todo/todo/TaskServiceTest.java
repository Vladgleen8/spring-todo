package org.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.todo.todo.dto.CreateTaskDto;
import org.todo.todo.dto.TaskDto;
import org.todo.todo.model.Task;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.repository.TaskRepository;
import org.todo.todo.service.impl.TaskService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTaskWithAllFields;
    private TaskDto sampleDto;
    private CreateTaskDto sampleCreateDto;

    @BeforeEach
    void setUp() {
        sampleTaskWithAllFields = new Task(1L, "desc", StatusEnum.TO_DO, "title", LocalDate.of(2025, 10, 10), LocalDate.now());
        sampleDto = TaskDto.fromEntity(sampleTaskWithAllFields);
        sampleCreateDto = CreateTaskDto.fromEntity(sampleTaskWithAllFields);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTaskWithAllFields);

        TaskDto result = taskService.createTask(sampleCreateDto);

        assertNotNull(result);
        assertEquals("title", result.getTitle());
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(sampleDto.getId());

        taskService.deleteTask(sampleDto.getId());

        verify(taskRepository, times(1)).deleteById(sampleDto.getId());
    }

    @Test
    void testUpdateTask() {
        sampleTaskWithAllFields = new Task(1L, "new description", StatusEnum.DONE, "new title", LocalDate.of(2025, 11, 10), LocalDate.now());
        sampleDto = TaskDto.fromEntity(sampleTaskWithAllFields);
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTaskWithAllFields);

        TaskDto updated = taskService.updateTask(sampleDto);

        assertNotNull(updated);
        assertEquals(sampleTaskWithAllFields.getId(), updated.getId());
        assertEquals(sampleTaskWithAllFields.getTitle(), updated.getTitle());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTaskWithAllFields);

        TaskDto result = taskService.getTaskById(sampleDto.getId());

        assertNotNull(result);
        assertEquals(sampleTaskWithAllFields.getId(), result.getId());
        assertEquals(sampleTaskWithAllFields.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }




 /*   @Test
    void testFindTasksWithStatus() {
        List<Task> tasks = List.of(task);
        Sort sort = Sort.by("dueDate");

        when(taskRepository.findByStatus(StatusEnum.TO_DO, sort)).thenReturn(tasks);

        List<TaskDto> result = taskService.findTasks("dueDate", StatusEnum.TO_DO);

        assertEquals(1, result.size());
        assertEquals(task.getTitle(), result.get(0).getTitle());

        verify(taskRepository, times(1)).findByStatus(StatusEnum.TO_DO, sort);
    }*/

//    @Test
//    void testFindTasksWithoutStatus() {
//        List<Task> tasks = List.of(task);
//        Sort sort = Sort.by("dueDate");
//
//        when(taskRepository.findAll(sort)).thenReturn(tasks);
//
//        List<TaskDto> result = taskService.findTasks("dueDate", null);
//
//        assertEquals(1, result.size());
//        assertEquals(task.getTitle(), result.get(0).getTitle());
//
//        verify(taskRepository, times(1)).findAll(sort);
//    }


}
