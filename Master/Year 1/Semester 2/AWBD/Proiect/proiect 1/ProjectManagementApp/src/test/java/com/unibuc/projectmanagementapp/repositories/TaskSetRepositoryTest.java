package com.unibuc.projectmanagementapp.repositories;

import com.unibuc.projectmanagementapp.domain.Subtask;
import com.unibuc.projectmanagementapp.domain.SubtaskEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("h2")
@Sql(scripts = "classpath:initial-data-h2.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TaskSetRepositoryTest {

    @Autowired
    private SubtaskEntryRepository subtaskEntryRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    @Test
    public void findAllBySubtaskTest() {
        // În initial-data.sql am inserat un subtask numit 'Design ERD'
        Optional<Subtask> setupSec = subtaskRepository.findByName("Design ERD");
        assertTrue(setupSec.isPresent(), "Subtask-ul 'Design ERD' ar trebui să existe în DB");

        Set<SubtaskEntry> entries = subtaskEntryRepository.findAllBySubtask(setupSec.get());
        assertFalse(entries.isEmpty(), "Ar trebui să existe SubtaskEntry-uri legate de 'Design ERD'");
    }
}
