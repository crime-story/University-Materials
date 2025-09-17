package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.repositories.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public void deleteSkill(UUID id) {
        skillRepository.deleteById(id);
    }

    public List<Skill> findAllById(Collection<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return skillRepository.findAllById(ids);
    }
}