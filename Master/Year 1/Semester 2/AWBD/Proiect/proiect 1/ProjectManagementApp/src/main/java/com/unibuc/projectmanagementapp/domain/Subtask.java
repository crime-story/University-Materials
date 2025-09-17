package com.unibuc.projectmanagementapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Subtask {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @jakarta.validation.constraints.Size(max = 100) // ← fără import, nume complet
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String toolsNeeded;

    private String difficulty;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    @ManyToMany
    @JoinTable(name = "subtask_skill",
            joinColumns = @JoinColumn(name = "subtask_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @NotEmpty(message = "At least one skill must be selected")
    private Set<Skill> skills = new HashSet<>();
}
