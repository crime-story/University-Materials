package com.unibuc.projectmanagementapp.repositories;

import com.unibuc.projectmanagementapp.domain.TaskSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskSessionRepository extends JpaRepository<TaskSession, UUID> {
    List findAllByUserEmail(String email);
    TaskSession findByUserEmailAndDate(String userEmail, LocalDate date);
}