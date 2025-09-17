package com.unibuc.projectmanagementapp.controller;

import com.unibuc.projectmanagementapp.domain.TaskSession;
import com.unibuc.projectmanagementapp.domain.security.User;
import com.unibuc.projectmanagementapp.services.TaskSessionService;
import com.unibuc.projectmanagementapp.services.TaskService;
import com.unibuc.projectmanagementapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.unibuc.projectmanagementapp.domain.Task;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class TaskSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskSessionService sessionService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showSessionsForm_ShouldReturnViewWithAttributes() throws Exception {
        // prepare
        YearMonth now = YearMonth.now();
        when(sessionService.findByUserEmail(anyString())).thenReturn(List.of());
        Page<Task> mockPage = new PageImpl<>(List.of());
        when(taskService.findAll(Pageable.ofSize(100))).thenReturn(mockPage);

        // act & assert
        mockMvc.perform(get("/taskSessions"))
                .andExpect(status().isOk())
                .andExpect(view().name("taskSessions"))
                .andExpect(model().attributeExists("currentMonth"))
                .andExpect(model().attributeExists("prevMonth"))
                .andExpect(model().attributeExists("nextMonth"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("taskSession"));

        verify(sessionService).findByUserEmail("admin");
        verify(taskService).findAll(Pageable.ofSize(100));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveSession_New_ShouldSave() throws Exception {
        // given no existing session for that date
        LocalDate date = LocalDate.of(2025, 6, 15);
        when(userService.getByEmail(anyString())).thenReturn(new User());
        when(sessionService.findByUserAndDate(anyString(), eq(date))).thenReturn(null);

        // when
        mockMvc.perform(post("/taskSessions")
                        .with(csrf())
                        .param("date", date.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/taskSessions?year=2025&month=6"));

        // then
        verify(sessionService, times(1)).save(any(TaskSession.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveSession_Existing_ShouldUpdate() throws Exception {
        // given an existing session -> controller will set its ID then call save()
        LocalDate date = LocalDate.of(2025, 6, 15);
        TaskSession existing = new TaskSession();
        existing.setId(UUID.randomUUID());
        existing.setDate(date);

        when(userService.getByEmail(anyString())).thenReturn(new User());
        when(sessionService.findByUserAndDate(anyString(), eq(date))).thenReturn(existing);

        // act
        mockMvc.perform(post("/taskSessions")
                        .with(csrf())
                        .param("date", date.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/taskSessions?year=2025&month=6"));

        // assert that save(...) was called with a TaskSession whose ID equals the existing one
        verify(sessionService).save(argThat(s -> existing.getId().equals(s.getId())));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteSession_Found_ShouldDelete() throws Exception {
        // given a session for that date
        LocalDate date = LocalDate.of(2025, 6, 15);
        TaskSession session = new TaskSession();
        session.setId(UUID.randomUUID());
        session.setDate(date);

        when(sessionService.findByUserAndDate(anyString(), eq(date))).thenReturn(session);

        // act & assert
        mockMvc.perform(delete("/taskSessions/{date}", date.toString())
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).delete(session.getId());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteSession_NotFound_ShouldReturn404() throws Exception {
        // no session present -> ResourceNotFoundException -> 404
        LocalDate date = LocalDate.of(2025, 6, 15);
        when(sessionService.findByUserAndDate(anyString(), eq(date))).thenReturn(null);

        mockMvc.perform(delete("/taskSessions/{date}", date.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
