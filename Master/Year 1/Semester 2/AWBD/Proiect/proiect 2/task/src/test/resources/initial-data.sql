-- 1. Roles
INSERT INTO role (id, role_name) VALUES
                                     (UNHEX(REPLACE(UUID(),'-','')), 'ROLE_ADMIN'),
                                     (UNHEX(REPLACE(UUID(),'-','')), 'ROLE_USER');

-- 2. Users
INSERT INTO "user" (
    account_non_expired, account_non_locked, credentials_non_expired, enabled,
    id, email, password
) VALUES
      (TRUE, TRUE, TRUE, TRUE,
       UNHEX(REPLACE(UUID(),'-','')),
       'admin@pmapp.com',
       '$2a$10$PDIkEfJysuBypngQBn00MutewMs5qmnbGNnb0spCLYCRcC35YGO/2'),
      (TRUE, TRUE, TRUE, TRUE,
       UNHEX(REPLACE(UUID(),'-','')),
       'user@pmapp.com',
       '$2a$10$eBjBfkT2Gb9JHSObuFGtkO7wQW20d1pUaDYKKKiVy/Y55hq7lnJlG');

-- 3. Link Users ↔ Roles
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM "user" u
         JOIN role r ON r.role_name = 'ROLE_ADMIN'
WHERE u.email = 'admin@pmapp.com'
UNION ALL
SELECT u.id, r.id
FROM "user" u
         JOIN role r ON r.role_name = 'ROLE_USER'
WHERE u.email IN ('admin@pmapp.com','user@pmapp.com');

-- 4. Personal Objectives (1:1 cu admin)
INSERT INTO personal_objective (
    id, target_hours, deadline, description, user_id
) VALUES (
             UNHEX(REPLACE(UUID(),'-','')),
             120.0,
             DATE '2025-09-30',
             'Lansare MVP versiunea 1.0 în septembrie 2025',
             (SELECT id FROM "user" WHERE email = 'admin@pmapp.com')
         );

-- 5. Skills
INSERT INTO skill (id, name) VALUES
                                 (UNHEX(REPLACE(UUID(),'-','')), 'Java'),
                                 (UNHEX(REPLACE(UUID(),'-','')), 'SQL'),
                                 (UNHEX(REPLACE(UUID(),'-','')), 'Spring');

-- 6. Subtasks
INSERT INTO subtask (
    id, name, description, priority, size, tools_needed, difficulty
) VALUES
      (UNHEX(REPLACE(UUID(),'-','')), 'Setup Security',    'Configure Spring Security + JWT',             'HIGH','SMALL','Maven,Spring Security','MEDIUM'),
      (UNHEX(REPLACE(UUID(),'-','')), 'Entities Modeling', 'Define JPA entities & relations',              'MEDIUM','MEDIUM','Hibernate,Lombok','MEDIUM'),
      (UNHEX(REPLACE(UUID(),'-','')), 'Controllers',       'Implement REST & Thymeleaf Controllers',      'MEDIUM','MEDIUM','Spring MVC','MEDIUM');

-- 7. Subtask ↔ Skill (many-to-many)
INSERT INTO subtask_skill_group (subtask_id, skill_id) VALUES
                                                           ((SELECT id FROM subtask WHERE name='Setup Security'),    (SELECT id FROM skill WHERE name='Spring')),
                                                           ((SELECT id FROM subtask WHERE name='Entities Modeling'), (SELECT id FROM skill WHERE name='SQL')),
                                                           ((SELECT id FROM subtask WHERE name='Controllers'),       (SELECT id FROM skill WHERE name='Java'));

-- 8. Tasks
INSERT INTO task (
    id, name, description, priority, size
) VALUES
      (UNHEX(REPLACE(UUID(),'-','')), 'Implement Authentication', 'Configurare Security + JWT', 'HIGH','MEDIUM'),
      (UNHEX(REPLACE(UUID(),'-','')), 'Design Database',          'Modelare entităţi & relaţii', 'MEDIUM','LARGE'),
      (UNHEX(REPLACE(UUID(),'-','')), 'Build REST API',           'Expose CRUD endpoints',       'HIGH','MEDIUM');

-- 9. Subtask Entries (log de lucru)
INSERT INTO subtask_entry (
    id, subtask_id, task_id, hours_worked, effort
) VALUES
      (UNHEX(REPLACE(UUID(),'-','')),
       (SELECT id FROM subtask WHERE name='Setup Security'),
       (SELECT id FROM task    WHERE name='Implement Authentication'),
       2, 3.5),
      (UNHEX(REPLACE(UUID(),'-','')),
       (SELECT id FROM subtask WHERE name='Entities Modeling'),
       (SELECT id FROM task    WHERE name='Design Database'),
       3, 4.0),
      (UNHEX(REPLACE(UUID(),'-','')),
       (SELECT id FROM subtask WHERE name='Controllers'),
       (SELECT id FROM task    WHERE name='Build REST API'),
       1, 2.0);

-- 10. Task Sessions
INSERT INTO task_session (
    id, user_id, task_id, date, notes, status
) VALUES
      (UNHEX(REPLACE(UUID(),'-','')),
       (SELECT id FROM "user" WHERE email='admin@pmapp.com'),
       (SELECT id FROM task    WHERE name='Implement Authentication'),
       DATE '2025-07-01', 'Test endpoint /login', 'COMPLETED'),
      (UNHEX(REPLACE(UUID(),'-','')),
       (SELECT id FROM "user" WHERE email='admin@pmapp.com'),
       (SELECT id FROM task    WHERE name='Design Database'),
       DATE '2025-07-02', 'Finalize relații',        'IN_PROGRESS'),
      (UNHEX(REPLACE(UUID(),'-','')),
       (SELECT id FROM "user" WHERE email='user@pmapp.com'),
       (SELECT id FROM task    WHERE name='Build REST API'),
       DATE '2025-07-03', 'Start GET /projects',      'SCHEDULED');
