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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;
    private TaskDto sampleDto;
    private CreateTaskDto sampleCreateDto;

    @BeforeEach
    void setUp() {
        sampleTask = new Task(1L, "desc", StatusEnum.TO_DO, "title", LocalDate.of(2025, 10, 10), LocalDate.now());
        sampleDto = TaskDto.fromEntity(sampleTask);
        sampleCreateDto = CreateTaskDto.fromEntity(sampleTask);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

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
        sampleTask = new Task(1L, "new description", StatusEnum.DONE, "new title", LocalDate.of(2025, 11, 10), LocalDate.now());
        sampleDto = TaskDto.fromEntity(sampleTask);
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskDto updated = taskService.updateTask(sampleDto);

        assertNotNull(updated);
        assertEquals(sampleTask.getId(), updated.getId());
        assertEquals(sampleTask.getTitle(), updated.getTitle());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTasksByStatus() {
        StatusEnum status = StatusEnum.TO_DO;

        Task task1 = new Task(1L, "desc1", StatusEnum.TO_DO, "title1", LocalDate.of(2025, 10, 10), LocalDate.now());
        Task task2 = new Task(2L, "desc2", StatusEnum.TO_DO, "title2", LocalDate.of(2025, 10, 15), LocalDate.now());
        List<Task> tasks = List.of(task1, task2);

        when(taskRepository.findByStatus(status)).thenReturn(tasks);

        List<TaskDto> result = taskService.getTasksByStatus(status);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getStatus() == status));
        verify(taskRepository, times(1)).findByStatus(eq(status));
    }


    @Test
    void testGetTasksSortedByDueDate() {
        Task task1 = new Task(1L, "desc1", StatusEnum.TO_DO, "title1",
                LocalDate.of(2025, 10, 10), LocalDate.now());
        Task task2 = new Task(2L, "desc2", StatusEnum.TO_DO, "title2",
                LocalDate.of(2025, 10, 15), LocalDate.now());

        when(taskRepository.findAll(Sort.by("dueDate"))).thenReturn(List.of(task1, task2));

        List<TaskDto> result = taskService.getTasks("dueDate", null);

        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2025, 10, 10), result.get(0).getDueDate());
        assertEquals(LocalDate.of(2025, 10, 15), result.get(1).getDueDate());

        verify(taskRepository, times(1)).findAll(Sort.by("dueDate"));
    }

    @Test
    void testGetTasksSortedByStatus() {
        Task task1 = new Task(1L, "desc1", StatusEnum.DONE, "title1",
                LocalDate.of(2025, 10, 10), LocalDate.now());
        Task task2 = new Task(2L, "desc2", StatusEnum.TO_DO, "title2",
                LocalDate.of(2025, 10, 15), LocalDate.now());

        when(taskRepository.findAll(Sort.by("status"))).thenReturn(List.of(task1, task2));

        List<TaskDto> result = taskService.getTasks("status", null);

        assertEquals(2, result.size());
        assertEquals(StatusEnum.DONE, result.get(0).getStatus());
        assertEquals(StatusEnum.TO_DO, result.get(1).getStatus());

        verify(taskRepository, times(1)).findAll(Sort.by("status"));
    }

    @Test
    void testGetTasksFilteredByStatusAndSorted() {
        StatusEnum status = StatusEnum.TO_DO;

        Task task1 = new Task(1L, "desc1", status, "title1",
                LocalDate.of(2025, 10, 10), LocalDate.now());
        Task task2 = new Task(2L, "desc2", status, "title2",
                LocalDate.of(2025, 10, 20), LocalDate.now());

        when(taskRepository.findByStatus(eq(status), eq(Sort.by("dueDate"))))
                .thenReturn(List.of(task1, task2));

        List<TaskDto> result = taskService.getTasks("dueDate", status);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getStatus() == status));
        verify(taskRepository, times(1)).findByStatus(eq(status), eq(Sort.by("dueDate")));
    }

    @Test
    void testGetTasksWithNullSortUsesDefaultDueDate() {
        Task task1 = new Task(1L, "desc1", StatusEnum.TO_DO, "title1",
                LocalDate.of(2025, 10, 10), LocalDate.now());
        Task task2 = new Task(2L, "desc2", StatusEnum.TO_DO, "title2",
                LocalDate.of(2025, 10, 15), LocalDate.now());

        when(taskRepository.findAll(Sort.by("dueDate"))).thenReturn(List.of(task1, task2));

        List<TaskDto> result = taskService.getTasks(null, null);

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll(Sort.by("dueDate"));
    }

}
