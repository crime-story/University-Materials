package com.unibuc.projectmanagementapp.controllers;

import com.unibuc.projectmanagementapp.domain.Priority;
import com.unibuc.projectmanagementapp.domain.Size;
import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.domain.Subtask;
import com.unibuc.projectmanagementapp.dtos.SubtaskCreateDTO;
import com.unibuc.projectmanagementapp.dtos.SubtaskDTO;
import com.unibuc.projectmanagementapp.services.SubtaskService;
import com.unibuc.projectmanagementapp.services.SkillService;
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

import java.util.List;
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
class SubtaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubtaskService subtaskService;

    @MockBean
    private SkillService skillService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void showCreateForm_ShouldReturnFormView() throws Exception {
        when(skillService.getAllSkills()).thenReturn(List.of());

        mockMvc.perform(get("/subtasks/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-subtask"))
                .andExpect(model().attributeExists("subtask"))
                .andExpect(model().attributeExists("skills"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveSubtask_ShouldRedirect() throws Exception {
        UUID skillId = UUID.randomUUID();

        // mock: exista acel skill in "DB"
        Skill skill = new Skill();
        skill.setId(skillId);
        skill.setName("Java");
        when(skillService.findAllById(any())).thenReturn(List.of(skill));

        mockMvc.perform(post("/subtasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Test Subtask")
                        .param("description", "Some description")
                        .param("difficulty", "MEDIUM")
                        .param("toolsNeeded", "IDE")
                        .param("priority", "HIGH")     // <-- obligatoriu (enum)
                        .param("size", "MEDIUM")       // <-- obligatoriu (enum)
                        .param("skillIds", skillId.toString())) // <-- numele corect pt. checkbox-uri
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subtasks"));

        verify(subtaskService, times(1)).saveSubtask(any(Subtask.class));
        verify(skillService, times(1)).findAllById(argThat(ids -> ids.contains(skillId)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void listSubtasks_ShouldReturnSortedAndPagedSubtasks() throws Exception {
        Page<SubtaskDTO> mockPage = new PageImpl<>(List.of());
        when(subtaskService.findAll(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(get("/subtasks")
                        .param("sortDir", "asc")
                        .param("page", "0")
                        .param("size", "5")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("subtasks"))
                .andExpect(model().attributeExists("subtasks"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("sortDir"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteSubtask_Successful() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/subtasks/{id}/delete", id)
                        .with(csrf())
                )
                .andExpect(status().isOk());

        verify(subtaskService, times(1)).deleteSubtask(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteSubtask_DataIntegrityViolation() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new DataIntegrityViolationException("constraint")).when(subtaskService).deleteSubtask(id);

        mockMvc.perform(delete("/subtasks/{id}/delete", id)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Nu se poate șterge subtask-ul")));

        verify(subtaskService, times(1)).deleteSubtask(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteSubtask_UnexpectedError() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("boom")).when(subtaskService).deleteSubtask(id);

        mockMvc.perform(delete("/subtasks/{id}/delete", id)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Eroare neașteptată la ștergere")));

        verify(subtaskService, times(1)).deleteSubtask(id);
    }
}
