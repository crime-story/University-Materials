package com.unibuc.task.services;

import com.unibuc.task.domain.Task;
import com.unibuc.task.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task getTaskById(UUID id){
        return taskRepository.getById(id);
    }
}