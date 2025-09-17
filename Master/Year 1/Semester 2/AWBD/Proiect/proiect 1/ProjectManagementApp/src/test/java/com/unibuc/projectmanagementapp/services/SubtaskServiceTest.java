package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.domain.Subtask;
import com.unibuc.projectmanagementapp.dtos.SubtaskDTO;
import com.unibuc.projectmanagementapp.repositories.SubtaskRepository;
import com.unibuc.projectmanagementapp.repositories.SubtaskEntryRepository;
import com.unibuc.projectmanagementapp.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubtaskServiceTest {

    @Mock
    private SubtaskRepository subtaskRepository;
    @Mock
    private SubtaskEntryRepository subtaskEntryRepository;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SubtaskService subtaskService;

    @Test
    public void findAll_ShouldReturnMappedDtoPage() {
        // aranjare: stub de Subtask cu skill
        Subtask subtask = new Subtask();
        subtask.setId(UUID.randomUUID());
        subtask.setName("Demo");
        subtask.setDescription("Desc");
        subtask.setToolsNeeded("Tools");
        subtask.setDifficulty("Hard");
        Skill skill = new Skill();
        skill.setName("Java");
        subtask.setSkills(Set.of(skill));

        Page<Subtask> stubPage = new PageImpl<>(List.of(subtask));
        when(subtaskRepository.findAll(any(Pageable.class))).thenReturn(stubPage);

        // executare
        Pageable pageReq = PageRequest.of(0, 10, Sort.by("name"));
        Page<SubtaskDTO> result = subtaskService.findAll(pageReq);

        // verificare
        assertEquals(1, result.getTotalElements());
        SubtaskDTO dto = result.getContent().get(0);
        assertEquals("Demo", dto.getName());
        assertEquals("Desc", dto.getDescription());
        assertEquals("Tools", dto.getToolsNeeded());
        assertEquals("Hard", dto.getDifficulty());
//        assertTrue(dto.getSkills().contains("Java"));

        verify(subtaskRepository, times(1)).findAll(pageReq);
    }

    @Test
    public void saveSubtask_ShouldInvokeRepositorySave() {
        Subtask input = new Subtask();
        input.setName("Test");
        when(subtaskRepository.save(input)).thenReturn(input);

        Subtask saved = subtaskService.saveSubtask(input);
        assertSame(input, saved);
        verify(subtaskRepository, times(1)).save(input);
    }

    @Test
    public void deleteSubtask_NotFound_ShouldThrow() {
        UUID id = UUID.randomUUID();
        when(subtaskRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class,
                () -> subtaskService.deleteSubtask(id),
                "Expected ResourceNotFoundException");
        verify(subtaskRepository).findById(id);
    }
}
