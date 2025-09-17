package com.unibuc.projectmanagementapp.repositories;

import com.unibuc.projectmanagementapp.domain.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {
    Optional findByName(String name);
}