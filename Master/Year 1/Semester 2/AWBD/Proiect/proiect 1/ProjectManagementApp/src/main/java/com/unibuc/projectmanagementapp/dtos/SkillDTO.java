package com.unibuc.projectmanagementapp.dtos;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import com.unibuc.projectmanagementapp.domain.Skill;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SkillDTO {
    @NotBlank
    private String name;

    public Skill toEntity() {
        Skill sk = new Skill();
        sk.setName(name);
        return sk;
    }
}