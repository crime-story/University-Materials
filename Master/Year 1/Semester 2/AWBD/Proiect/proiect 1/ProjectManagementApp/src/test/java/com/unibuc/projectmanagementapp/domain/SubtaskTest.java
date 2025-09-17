package com.unibuc.projectmanagementapp.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*; // folosim JUnit

@DataJpaTest
@ActiveProfiles("h2")
class SubtaskTest {

    @Autowired
    private TestEntityManager entityManager;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testPersistAndLoadSubtask() {
        // 1) Persistă un Skill (nu ai cascade)
        Skill skill = new Skill();
        skill.setName("Persistence");
        entityManager.persist(skill);

        // 2) Subtask cu TOATE câmpurile obligatorii setate
        Subtask subtask = new Subtask();
        subtask.setName("Write Integration Test");
        subtask.setDescription("Test JPA mapping");
        subtask.setToolsNeeded("JUnit");
        subtask.setDifficulty("Medium");
        subtask.setPriority(Priority.MEDIUM);     // ← @NotNull
        subtask.setSize(Size.MEDIUM);             // ← @NotNull

        // 3) Leagă relația pe ambele părți
        subtask.getSkills().add(skill);
        skill.getSubtasks().add(subtask);

        // 4) Persist + flush + find
        Subtask saved = entityManager.persistFlushFind(subtask);

        assertNotNull(saved.getId());
        assertEquals("Write Integration Test", saved.getName());
        assertEquals(1, saved.getSkills().size());
        Skill related = saved.getSkills().iterator().next();
        assertEquals("Persistence", related.getName());
    }

    @Test
    void testSubtaskValidationConstraints() {
        // vrem să testăm DOAR NotBlank-urile și NotEmpty-ul,
        // așa că setăm priority/size ca să nu ne „încurce”
        Subtask invalid = new Subtask();
        invalid.setName("");                 // @NotBlank
        invalid.setDescription("");          // @NotBlank
        invalid.setToolsNeeded("");          // @NotBlank
        invalid.setSkills(new HashSet<>());  // @NotEmpty

        invalid.setPriority(Priority.LOW);   // ← valid
        invalid.setSize(Size.SMALL);         // ← valid

        Set<ConstraintViolation<Subtask>> violations = validator.validate(invalid);
        // 4: name, description, toolsNeeded, skills
        assertEquals(4, violations.size(), violations.toString());

        var byField = violations.stream().collect(Collectors.groupingBy(v -> v.getPropertyPath().toString()));
        assertTrue(byField.containsKey("name"));
        assertTrue(byField.containsKey("description"));
        assertTrue(byField.containsKey("toolsNeeded"));
        assertTrue(byField.containsKey("skills"));
    }

    @Test
    void testNameMaxLengthValidation() {
        // Asigură-te că în entitate ai @jakarta.validation.constraints.Size(max = 100) pe name!
        Subtask subtask = new Subtask();
        subtask.setName("a".repeat(101)); // >100 chars
        subtask.setDescription("Valid description");
        subtask.setToolsNeeded("Valid tools");
        subtask.setPriority(Priority.LOW);
        subtask.setSize(Size.SMALL);

        Skill skill = new Skill();
        skill.setName("Setup");
        entityManager.persist(skill);
        subtask.getSkills().add(skill);

        Set<ConstraintViolation<Subtask>> violations = validator.validate(subtask);

        // trebuie să fie EXACT o violare: name prea lung
        assertEquals(1, violations.size(), violations.toString());
        ConstraintViolation<Subtask> v = violations.iterator().next();
        assertEquals("name", v.getPropertyPath().toString());
        // dacă ai mesaj custom în entitate, poți verifica și mesajul:
        // assertEquals("Name must be at most 100 characters", v.getMessage());
    }
}
