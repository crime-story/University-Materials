package com.unibuc.projectmanagementapp.controllers;

import com.unibuc.projectmanagementapp.domain.Task;
import com.unibuc.projectmanagementapp.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void showCreateForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/tasks/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveTask_WithValidData_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/tasks")
                        .with(csrf())
                        .param("name", "My Task")
                        .param("description", "Do things")
                        .param("priority", "HIGH")
                        .param("size", "MEDIUM")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService, times(1)).saveTask(any(Task.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveTask_WithValidationErrors_ShouldReturnFormView() throws Exception {
        mockMvc.perform(post("/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED) // ← form submit
                        // lipsă 'name' → declanșăm validarea
                        .param("description", "No name provided")
                        .param("priority", "LOW")
                        .param("size", "SMALL"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeHasFieldErrors("task", "name")); // ← verifică eroarea corectă

        verify(taskService, never()).saveTask(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void listTasks_ShouldReturnPagedTasks() throws Exception {
        when(taskService.findAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "5")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(model().attributeExists("pageNumber"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteTask_Successful() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/tasks/{id}/delete", id)
                        .with(csrf())
                )
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteTask_DataIntegrityViolation() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new DataIntegrityViolationException("constraint"))
                .when(taskService).deleteTask(id);

        mockMvc.perform(delete("/tasks/{id}/delete", id)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Task folosit în sesiuni")));

        verify(taskService, times(1)).deleteTask(id);
    }
}
