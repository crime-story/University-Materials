package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.TaskSession;
import com.unibuc.projectmanagementapp.repositories.TaskSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskSessionServiceTest {

    @Mock
    private TaskSessionRepository repository;

    @InjectMocks
    private TaskSessionService service;

    @Test
    void findByUserEmail_ShouldReturnSessions() {
        String email = "user@example.com";
        List<TaskSession> sessions = List.of(new TaskSession());
        when(repository.findAllByUserEmail(email)).thenReturn(sessions);

        List<TaskSession> result = service.findByUserEmail(email);

        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByUserEmail(email);
    }

    @Test
    void findByUserAndDate_ShouldReturnSession() {
        String email = "user@example.com";
        LocalDate date = LocalDate.of(2025, 6, 15);
        TaskSession ts = new TaskSession();
        when(repository.findByUserEmailAndDate(email, date)).thenReturn(ts);

        TaskSession result = service.findByUserAndDate(email, date);

        assertSame(ts, result);
        verify(repository, times(1)).findByUserEmailAndDate(email, date);
    }

    @Test
    void saveSession_ShouldDelegateToRepository() {
        TaskSession ts = new TaskSession();
        when(repository.save(ts)).thenReturn(ts);

        TaskSession result = service.save(ts);

        assertSame(ts, result);
        verify(repository, times(1)).save(ts);
    }

    @Test
    void deleteSession_ShouldDelegateToRepository() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
