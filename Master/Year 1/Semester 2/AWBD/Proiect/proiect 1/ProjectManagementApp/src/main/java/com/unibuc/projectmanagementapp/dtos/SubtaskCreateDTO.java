package com.unibuc.projectmanagementapp.dtos;

import com.unibuc.projectmanagementapp.domain.Priority;
import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.domain.Subtask;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import jakarta.validation.constraints.*;
import com.unibuc.projectmanagementapp.domain.Size;
import java.util.Set;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SubtaskCreateDTO {
    @NotBlank
    @jakarta.validation.constraints.Size(max = 100)
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String toolsNeeded;

    @NotBlank
    private String difficulty;

    @NotNull
    private Priority priority;

    @NotNull
    private Size size;

    @NotEmpty
    private Set<@NotNull UUID> skillIds;

    public Subtask toEntity(Set<Skill> skills) {
        Subtask s = new Subtask();
        s.setName(name);
        s.setDescription(description);
        s.setToolsNeeded(toolsNeeded);
        s.setDifficulty(difficulty);
        s.setPriority(priority);
        s.setSize(size);
        s.setSkills(skills);
        return s;
    }
}