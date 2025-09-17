package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.domain.Subtask;
import com.unibuc.projectmanagementapp.domain.SubtaskEntry;
import com.unibuc.projectmanagementapp.dtos.SubtaskDTO;
import com.unibuc.projectmanagementapp.exceptions.ResourceNotFoundException;
import com.unibuc.projectmanagementapp.repositories.SubtaskRepository;
import com.unibuc.projectmanagementapp.repositories.SubtaskEntryRepository;
import com.unibuc.projectmanagementapp.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final SubtaskEntryRepository subtaskEntryRepository;
    private final TaskRepository taskRepository;

    public Page<SubtaskDTO> findAll(Pageable pageable) {
        return subtaskRepository.findAll(pageable)
                .map(st -> new SubtaskDTO(
                        st.getId(),
                        st.getName(),
                        st.getDescription(),
                        st.getDifficulty(),
                        st.getToolsNeeded(),
                        st.getPriority(),
                        st.getSize(),
                        st.getSkills().stream().map(Skill::getName).collect(Collectors.toSet())
                ));
    }

    public Subtask saveSubtask(Subtask subtask) {
        return subtaskRepository.save(subtask);
    }

    public void deleteSubtask(UUID id) {
        Subtask sub = subtaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found: " + id));

        Set<SubtaskEntry> entries = subtaskEntryRepository.findAllBySubtask(sub);
        // șterg întâi legăturile
        entries.forEach(e -> taskRepository.deleteById(e.getTask().getId()));
        subtaskEntryRepository.deleteAll(entries);

        // curăț ManyToMany
        sub.getSkills().forEach(skill -> skill.getSubtasks().remove(sub));
        sub.getSkills().clear();

        subtaskRepository.deleteById(id);
    }
}