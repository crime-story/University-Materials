package com.unibuc.projectmanagementapp.dtos;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SubtaskEntryDTO {
    @NotNull
    private UUID subtaskId;
    @PositiveOrZero
    private Integer hoursWorked;
    @PositiveOrZero
    private Double effort;
}