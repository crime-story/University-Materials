package com.unibuc.projectmanagementapp.dtos;

import com.unibuc.projectmanagementapp.domain.Priority;
import com.unibuc.projectmanagementapp.domain.Size;
import com.unibuc.projectmanagementapp.domain.Task;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TaskCreateDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Priority priority;

    @NotNull
    private Size size;

    public Task toEntity() {
        Task t = new Task();
        t.setName(name);
        t.setDescription(description);
        t.setPriority(priority);
        t.setSize(size);
        return t;
    }
}