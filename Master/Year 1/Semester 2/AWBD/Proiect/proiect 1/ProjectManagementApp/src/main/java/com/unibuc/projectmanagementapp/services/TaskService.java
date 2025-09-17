package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.Task;
import com.unibuc.projectmanagementapp.exceptions.ResourceNotFoundException;
import com.unibuc.projectmanagementapp.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task saveTask(Task t) {
        return taskRepository.save(t);
    }

    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found: " + id);
        }
        taskRepository.deleteById(id);
    }
}