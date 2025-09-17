package com.unibuc.projectmanagementapp.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("h2")
public class TaskTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPersistTaskWithEntriesAndSessions() {
        // Create Task
        Task task = new Task();
        task.setName("Implement Feature X");
        task.setDescription("Core application functionality");

        // Create SubtaskEntry (like a work log)
        SubtaskEntry entry = new SubtaskEntry();
        entry.setTask(task);
        entry.setHoursWorked(8);
        entry.setEffort(3.5);
        task.getSubtaskEntries().add(entry);

        // Create TaskSession
        TaskSession session = new TaskSession();
        session.setTask(task);
        session.setDate(LocalDate.of(2025, 6, 21));
        session.setNotes("Initial implementation");
        session.setStatus(SessionStatus.IN_PROGRESS);
        task.getTaskSessions().add(session);

        // Persist and reload
        Task saved = entityManager.persistFlushFind(task);

        assertNotNull(saved.getId());
        assertEquals(1, saved.getSubtaskEntries().size());
        assertEquals(1, saved.getTaskSessions().size());

        SubtaskEntry savedEntry = saved.getSubtaskEntries().get(0);
        TaskSession savedSession = saved.getTaskSessions().get(0);

        assertEquals(8, savedEntry.getHoursWorked());
        assertEquals(3.5, savedEntry.getEffort());
        assertEquals(LocalDate.of(2025, 6, 21), savedSession.getDate());
        assertEquals("Initial implementation", savedSession.getNotes());
    }

    @Test
    public void testCascadeDeleteTaskRemovesChildren() {
        // Create Task with entries and sessions
        Task task = new Task();
        task.setName("Bug Fixing");
        task.setDescription("Resolve defects");

        SubtaskEntry entry = new SubtaskEntry();
        entry.setTask(task);
        entry.setHoursWorked(5);
        entry.setEffort(2.0);
        task.getSubtaskEntries().add(entry);

        TaskSession session = new TaskSession();
        session.setTask(task);
        session.setDate(LocalDate.of(2025, 7, 1));
        session.setNotes("Testing fixes");
        session.setStatus(SessionStatus.COMPLETED);
        task.getTaskSessions().add(session);

        Task persisted = entityManager.persistFlushFind(task);
        UUID taskId = persisted.getId();

        // Remove parent
        entityManager.remove(persisted);
        entityManager.flush();

        Task found = entityManager.find(Task.class, taskId);
        assertNull(found);
    }
}
