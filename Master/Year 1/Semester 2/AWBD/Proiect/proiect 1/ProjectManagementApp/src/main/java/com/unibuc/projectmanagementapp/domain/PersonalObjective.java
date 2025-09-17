package com.unibuc.projectmanagementapp.domain;

import com.unibuc.projectmanagementapp.domain.security.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Setter
@Getter
public class PersonalObjective {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double targetHours;
    private LocalDate deadline;
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
