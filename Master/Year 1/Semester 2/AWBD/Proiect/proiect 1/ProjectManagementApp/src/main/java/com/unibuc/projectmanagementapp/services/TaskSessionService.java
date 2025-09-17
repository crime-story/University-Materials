package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.TaskSession;
import com.unibuc.projectmanagementapp.repositories.TaskSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskSessionService {
    private final TaskSessionRepository sessionRepository;

    public List<TaskSession> getAll() {
        return sessionRepository.findAll();
    }

    public TaskSession save(TaskSession session) {
        return sessionRepository.save(session);
    }

    public void delete(UUID id) {
        sessionRepository.deleteById(id);
    }

    public List<TaskSession> findByUserEmail(String email) {
        return sessionRepository.findAllByUserEmail(email);
    }

    public TaskSession findByUserAndDate(String email, LocalDate date) {
        return sessionRepository.findByUserEmailAndDate(email, date);
    }
}