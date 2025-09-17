package com.unibuc.tasksession.services;

import com.unibuc.tasksession.domain.TaskSession;
import com.unibuc.tasksession.repositories.TaskSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskSessionService {
    private final TaskSessionRepository sessionRepository;

    public TaskSession getTaskSessionByDate(LocalDate date) {
        return sessionRepository.findByDate(date);
    }
}