package com.unibuc.projectmanagementapp.dtos;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor
public class TaskSessionDTO {
    private String taskName;
    private LocalDate date;
    private String notes;
    private String status;
}