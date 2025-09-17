package com.unibuc.projectmanagementapp.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*; // folosește junit, nu Contracts.assertNotNull

@DataJpaTest
@ActiveProfiles("h2")
class SkillTest {

    @Autowired
    private TestEntityManager em;

    @Test
    void testPersistAndLoadSkill() {
        Skill skill = new Skill();
        skill.setName("JPA Mapping");

        Skill saved = em.persistFlushFind(skill);
        assertNotNull(saved.getId());
        assertEquals("JPA Mapping", saved.getName());
        assertTrue(saved.getSubtasks().isEmpty());
    }

    @Test
    void testSkillSubtaskRelationship() {
        // 1) Persistă mai întâi Skill (nu avem cascade de la Subtask → Skill)
        Skill skill = new Skill();
        skill.setName("Validation");
        em.persist(skill);

        // 2) Creează Subtask cu TOATE câmpurile obligatorii setate
        Subtask subtask = new Subtask();
        subtask.setName("Write Validators");
        subtask.setDescription("Add @Valid and checks");
        subtask.setToolsNeeded("Hibernate Validator");
        subtask.setDifficulty("Low");
        subtask.setPriority(Priority.LOW);   // ← @NotNull
        subtask.setSize(Size.SMALL);         // ← @NotNull

        // 3) Leagă relația pe ambele părți
        subtask.getSkills().add(skill);
        skill.getSubtasks().add(subtask);

        // 4) Persistă owning side-ul (Subtask)
        em.persist(subtask);

        // 5) Flush pentru a forța INSERT-urile + rândul din tabela de legătură
        em.flush();
        em.clear();

        // 6) Verifică relația din DB
        Skill reloaded = em.find(Skill.class, skill.getId());
        assertNotNull(reloaded);
        assertEquals(1, reloaded.getSubtasks().size());
        Subtask linked = reloaded.getSubtasks().iterator().next();
        assertEquals("Write Validators", linked.getName());
    }
}
