package com.unibuc.tasksession.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Size size;

    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskSession> taskSessions = new ArrayList<>();
}
