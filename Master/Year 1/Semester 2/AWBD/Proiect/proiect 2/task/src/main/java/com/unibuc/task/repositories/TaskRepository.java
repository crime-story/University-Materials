package com.unibuc.task.repositories;

import com.unibuc.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
}