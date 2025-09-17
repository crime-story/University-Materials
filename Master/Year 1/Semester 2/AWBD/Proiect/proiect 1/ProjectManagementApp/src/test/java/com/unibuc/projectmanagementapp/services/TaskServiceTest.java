package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.Task;
import com.unibuc.projectmanagementapp.exceptions.ResourceNotFoundException;
import com.unibuc.projectmanagementapp.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void findAll_ShouldReturnPageOfTasks() {
        // aranjare
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setName("DemoTask");
        Page<Task> stubPage = new PageImpl<>(List.of(task));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(stubPage);

        // acțiune
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Task> result = taskService.findAll(pageable);

        // verificare
        assertEquals(1, result.getTotalElements());
        assertEquals("DemoTask", result.getContent().get(0).getName());
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    public void saveTask_ShouldReturnSavedEntity() {
        Task task = new Task();
        when(taskRepository.save(task)).thenReturn(task);

        Task saved = taskService.saveTask(task);

        assertSame(task, saved);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteTask_ShouldInvokeRepositoryDeleteById() {
        UUID id = UUID.randomUUID();

        Task existing = new Task();
        existing.setId(id);
        existing.setName("Any");

        // dacă service-ul tău folosește findById:
        when(taskRepository.existsById(id)).thenReturn(true);
        // dacă folosește existsById, folosește asta:
        // when(taskRepository.existsById(id)).thenReturn(true);

        doNothing().when(taskRepository).deleteById(id);

        taskService.deleteTask(id);

        verify(taskRepository).deleteById(id);
    }


    @Test
    void deleteTask_NotFound_ShouldThrowResourceNotFound() {
        UUID id = UUID.randomUUID();

        when(taskRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(id));

        verify(taskRepository).existsById(id);
        verify(taskRepository, never()).deleteById(any());
    }
}
