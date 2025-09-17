package com.unibuc.tasksession.repositories;

import com.unibuc.tasksession.domain.TaskSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskSessionRepository extends JpaRepository<TaskSession, UUID> {
    TaskSession findByDate(LocalDate date);
}