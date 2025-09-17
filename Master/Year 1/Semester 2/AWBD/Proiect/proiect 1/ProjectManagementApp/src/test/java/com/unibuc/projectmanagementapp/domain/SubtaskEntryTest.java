package com.unibuc.projectmanagementapp.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("h2")
public class SubtaskEntryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPersistAndLoadSubtaskEntry() {
        // 1) Create a Skill (e.g. a technology)
        Skill skill = new Skill();
        skill.setName("Java");
        entityManager.persist(skill);

        // 2) Create a Subtask (e.g. a piece of work)
        Subtask subtask = new Subtask();
        subtask.setName("Implement Authentication");
        subtask.setDescription("Configure Spring Security + JWT");
        subtask.setToolsNeeded("Spring Security");
        subtask.setDifficulty("Medium");
        subtask.setPriority(Priority.MEDIUM);
        subtask.setSize(Size.SMALL);
        subtask.setSkills(Set.of(skill));
        entityManager.persist(subtask);

        // 3) Create a Task (e.g. a user-facing service)
        Task task = new Task();
        task.setName("User Management Service");
        task.setDescription("Handles registration, login, roles");
        task.setPriority(Priority.HIGH);
        task.setSize(Size.MEDIUM);
        entityManager.persist(task);

        // 4) Create the SubtaskEntry itself
        SubtaskEntry entry = new SubtaskEntry();
        entry.setHoursWorked(3);
        entry.setEffort(5.0);
        entry.setSubtask(subtask);
        entry.setTask(task);

        // 5) Persist & reload
        SubtaskEntry saved = entityManager.persistFlushFind(entry);

        // 6) Assertions
        assertNotNull(saved.getId());
        assertEquals(3, saved.getHoursWorked());
        assertEquals(5.0, saved.getEffort());
        assertNotNull(saved.getSubtask());
        assertEquals("Implement Authentication", saved.getSubtask().getName());
        assertNotNull(saved.getTask());
        assertEquals("User Management Service", saved.getTask().getName());
    }
}
