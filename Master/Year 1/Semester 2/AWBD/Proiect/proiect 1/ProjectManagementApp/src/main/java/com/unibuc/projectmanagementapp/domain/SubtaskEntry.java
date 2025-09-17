package com.unibuc.projectmanagementapp.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class SubtaskEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer hoursWorked;
    private Double effort;

    @ManyToOne
    @JoinColumn(name = "subtask_id")
    private Subtask subtask;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
