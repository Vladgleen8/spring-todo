package org.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.todo.todo.model.enums.StatusEnum;
import org.todo.todo.model.Task;
import org.todo.todo.repository.TaskRepository;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll(); // гарантирует чистую базу перед каждым тестом
    }

    @Test
    void createTaskShouldReturnCreatedAndPersist() throws Exception {
        String json = """
            {
              "title": "Integration Test Task",
              "description": "Testing POST",
              "dueDate": "2025-10-15",
              "status": "TO_DO"
            }
            """;

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.status").value("TO_DO"));
    }

    @Test
    void createTaskWithEmptyTitleReturnBadRequest() throws Exception {
        String json = """
            {
              "title": "",
              "description": "Missing title",
              "dueDate": "2025-10-10",
              "status": "TO_DO"
            }
            """;

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be empty"));
    }

    @Test
    void getTaskByIdShouldReturnTask() throws Exception {
        Task saved = taskRepository.save(
                new Task(null, "desc", StatusEnum.TO_DO, "Sample title", LocalDate.now().plusDays(2), LocalDate.now())
        );

        mockMvc.perform(get("/api/v1/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample title"))
                .andExpect(jsonPath("$.status").value("TO_DO"));
    }

    @Test
    void getTaskByIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    void updateTaskShouldModifyAndReturnUpdatedEntity() throws Exception {
        Task saved = taskRepository.save(
                new Task(null, "old desc", StatusEnum.TO_DO, "Old title",
                        LocalDate.of(2025, 10, 10), LocalDate.now())
        );

        String updateJson = """
            {
              "id": %d,
              "title": "Updated title",
              "description": "Updated desc",
              "dueDate": "2025-10-25",
              "status": "DONE"
            }
            """.formatted(saved.getId());

        mockMvc.perform(put("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void updateNonExistingTaskShouldReturnNotFound() throws Exception {
        String updateJson = """
            {
              "id": 9999,
              "title": "Nonexistent",
              "description": "No such entity",
              "dueDate": "2025-10-25",
              "status": "DONE"
            }
            """;

        mockMvc.perform(put("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    void deleteTaskShouldRemoveEntity() throws Exception {
        Task saved = taskRepository.save(
                new Task(null, "desc", StatusEnum.TO_DO, "Title",
                        LocalDate.now().plusDays(1), LocalDate.now())
        );

        mockMvc.perform(delete("/api/v1/tasks/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(taskRepository.existsById(saved.getId()));
    }

    @Test
    void deleteNonExistingTaskShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTasks_WithPaginationAndStatus_ShouldReturnPage() throws Exception {
        taskRepository.save(new Task(null, "desc1", StatusEnum.TO_DO, "Task A", LocalDate.now(), LocalDate.now()));
        taskRepository.save(new Task(null, "desc2", StatusEnum.IN_PROGRESS, "Task B", LocalDate.now(), LocalDate.now()));
        taskRepository.save(new Task(null, "desc3", StatusEnum.TO_DO, "Task C", LocalDate.now(), LocalDate.now()));

        mockMvc.perform(get("/api/v1/tasks")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sortBy", "title")
                        .param("status", "TO_DO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(2));
    }

    @Test
    void getTasks_WithoutStatus_ShouldReturnAllPaged() throws Exception {
        taskRepository.save(new Task(null, "desc1", StatusEnum.TO_DO, "Task A", LocalDate.now(), LocalDate.now()));
        taskRepository.save(new Task(null, "desc2", StatusEnum.DONE, "Task B", LocalDate.now(), LocalDate.now()));

        mockMvc.perform(get("/api/v1/tasks")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }
}
