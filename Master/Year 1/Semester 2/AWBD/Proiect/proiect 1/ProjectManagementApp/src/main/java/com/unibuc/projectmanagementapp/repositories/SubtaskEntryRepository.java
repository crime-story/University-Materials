package com.unibuc.projectmanagementapp.repositories;

import com.unibuc.projectmanagementapp.domain.Subtask;
import com.unibuc.projectmanagementapp.domain.SubtaskEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface SubtaskEntryRepository extends JpaRepository<SubtaskEntry, UUID> {
    Set findAllBySubtask(Subtask subtask);
}