package com.unibuc.projectmanagementapp.repositories;

import com.unibuc.projectmanagementapp.domain.PersonalObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonalObjectiveRepository extends JpaRepository<PersonalObjective, UUID> {
}