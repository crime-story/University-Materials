package com.unibuc.projectmanagementapp.repositories.security;

import com.unibuc.projectmanagementapp.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
}