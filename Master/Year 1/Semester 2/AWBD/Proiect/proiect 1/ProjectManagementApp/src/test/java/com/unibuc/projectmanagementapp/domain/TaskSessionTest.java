package com.unibuc.projectmanagementapp.domain;

import com.unibuc.projectmanagementapp.domain.security.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("h2")
public class TaskSessionTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPersistAndLoadTaskSession() {
        // Create and persist a user
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("secret");
        entityManager.persist(user);

        // Create and persist a task
        Task task = new Task();
        task.setName("Implement Feature");
        task.setDescription("Coding and tests");
        entityManager.persist(task);

        // Create a session linking both
        TaskSession session = new TaskSession();
        session.setDate(LocalDate.of(2025, 6, 21));
        session.setNotes("All green");
        session.setStatus(SessionStatus.COMPLETED);
        session.setUser(user);
        session.setTask(task);

        TaskSession saved = entityManager.persistFlushFind(session);

        assertNotNull(saved.getId());
        assertEquals(LocalDate.of(2025, 6, 21), saved.getDate());
        assertEquals("All green", saved.getNotes());
        assertEquals(SessionStatus.COMPLETED, saved.getStatus());
        assertNotNull(saved.getUser());
        assertEquals("user@example.com", saved.getUser().getEmail());
        assertNotNull(saved.getTask());
        assertEquals("Implement Feature", saved.getTask().getName());
    }
}
