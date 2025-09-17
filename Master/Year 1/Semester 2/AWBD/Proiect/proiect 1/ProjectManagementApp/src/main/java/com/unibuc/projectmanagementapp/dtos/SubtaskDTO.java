package com.unibuc.projectmanagementapp.dtos;

import com.unibuc.projectmanagementapp.domain.Priority;
import com.unibuc.projectmanagementapp.domain.Size;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Getter @Setter @AllArgsConstructor
public class SubtaskDTO {
    private UUID id;
    private String name;
    private String description;
    private String difficulty;
    private String toolsNeeded;
    private Priority priority;
    private Size size;
    private Set<String> skills;
}