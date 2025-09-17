// src/test/java/com/unibuc/projectmanagementapp/repositories/TaskSessionRepositoryTest.java
package com.unibuc.projectmanagementapp.repositories;

import com.unibuc.projectmanagementapp.domain.TaskSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
@Sql(scripts = "classpath:initial-data-h2.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Slf4j
public class TaskSessionRepositoryTest {

    @Autowired
    private TaskSessionRepository repository;

    @Test
    public void findAllByUserEmail() {
        List<TaskSession> sessions = repository.findAllByUserEmail("admin@pmapp.com");
        assertFalse(sessions.isEmpty(), "Should have loaded at least one task session");

        log.info("Task sessions for user 'admin@pmapp.com':");
        for (TaskSession s : sessions) {
            log.info("Date: {}, Task: {}, Status: {}",
                    s.getDate(), s.getTask().getName(), s.getStatus());
        }
    }

    @Test
    public void findByUserEmailAndDate() {
        LocalDate on = LocalDate.of(2025, 6, 10);
        TaskSession session = repository.findByUserEmailAndDate("admin@pmapp.com", on);
        assertNotNull(session, "Should find the session on " + on);
        assertEquals(on, session.getDate());
    }
}
