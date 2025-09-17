package com.unibuc.projectmanagementapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.projectmanagementapp.dtos.SkillDTO;
import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.services.SkillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private SkillService skillService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addSkill_ShouldReturnSavedEntity() throws Exception {
        // given
        SkillDTO dto = new SkillDTO();
        dto.setName("Java");

        Skill saved = new Skill();
        saved.setId(java.util.UUID.randomUUID());
        saved.setName("Java");

        when(skillService.saveSkill(any(Skill.class))).thenReturn(saved);

        // when / then
        mockMvc.perform(post("/api/skills")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(skillService, times(1)).saveSkill(any(Skill.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addSkill_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        SkillDTO dto = new SkillDTO();
        dto.setName(""); // invalid

        mockMvc.perform(post("/api/skills")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(skillService, never()).saveSkill(any());
    }
}
