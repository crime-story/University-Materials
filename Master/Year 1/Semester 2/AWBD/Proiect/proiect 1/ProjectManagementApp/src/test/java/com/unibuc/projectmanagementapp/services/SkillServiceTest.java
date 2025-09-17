package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.repositories.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {

    @InjectMocks
    private SkillService skillService;

    @Mock
    private SkillRepository skillRepository;

    @Test
    public void getAllSkills_ShouldReturnListOfSkills() {
        // aranjare
        List<Skill> skillsList = new ArrayList<>();
        Skill s = new Skill();
        skillsList.add(s);
        when(skillRepository.findAll()).thenReturn(skillsList);

        // executare
        List<Skill> result = skillService.getAllSkills();

        // verificare
        assertEquals(1, result.size(), "Ar trebui să returneze exact un skill");
        verify(skillRepository, times(1)).findAll();
    }
}
