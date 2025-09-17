-- ──────────── 0. Disable foreign keys & truncate ────────────
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE user_role;
TRUNCATE TABLE task_session;
TRUNCATE TABLE subtask_entry;
TRUNCATE TABLE subtask_skill;        -- join table
TRUNCATE TABLE personal_objective;
TRUNCATE TABLE task;
TRUNCATE TABLE subtask;
TRUNCATE TABLE skill;
TRUNCATE TABLE `user`;
TRUNCATE TABLE role;

SET FOREIGN_KEY_CHECKS = 1;

-- ──────────── 1. Roles ────────────
INSERT INTO role (id, role_name) VALUES
                                     (UNHEX(REPLACE(UUID(),'-','')), 'ROLE_ADMIN'),
                                     (UNHEX(REPLACE(UUID(),'-','')), 'ROLE_USER');

-- ──────────── 2. Users ────────────
INSERT INTO `user` (
    id, email, password,
    account_non_expired, account_non_locked,
    credentials_non_expired, enabled
) VALUES
      (
          UNHEX(REPLACE(UUID(),'-','')),
          'admin@pmapp.com',
          '$2a$10$PDIkEfJysuBypngQBn00MutewMs5qmnbGNnb0spCLYCRcC35YGO/2',
          TRUE, TRUE, TRUE, TRUE
      ),
      (
          UNHEX(REPLACE(UUID(),'-','')),
          'user@pmapp.com',
          '$2a$10$eBjBfkT2Gb9JHSObuFGtkO7wQW20d1pUaDYKKKiVy/Y55hq7lnJlG',
          TRUE, TRUE, TRUE, TRUE
      );

-- ──────────── 3. User ↔ Role ────────────
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM `user` u
         JOIN role r ON r.role_name = 'ROLE_ADMIN'
WHERE u.email = 'admin@pmapp.com'
UNION ALL
SELECT u.id, r.id
FROM `user` u
         JOIN role r ON r.role_name = 'ROLE_USER'
WHERE u.email = 'admin@pmapp.com'
UNION ALL
SELECT u.id, r.id
FROM `user` u
         JOIN role r ON r.role_name = 'ROLE_USER'
WHERE u.email = 'user@pmapp.com';

-- ──────────── 4. Personal Objective ────────────
INSERT INTO personal_objective (
    id, target_hours, deadline, description, user_id
) VALUES (
             UNHEX(REPLACE(UUID(),'-','')),
             120.0,
             '2025-09-30',
             'Lansare MVP versiunea 1.0 în septembrie 2025',
             (SELECT id FROM `user` WHERE email = 'admin@pmapp.com')
         );

-- ──────────── 5. Skills ────────────
INSERT INTO skill (id, name) VALUES
                                 (UNHEX(REPLACE(UUID(),'-','')), 'Java'),
                                 (UNHEX(REPLACE(UUID(),'-','')), 'SQL'),
                                 (UNHEX(REPLACE(UUID(),'-','')), 'Spring'),
                                 (UNHEX(REPLACE(UUID(),'-','')), 'Docker');

-- ──────────── 6. Subtasks ────────────
INSERT INTO subtask (
    id, name, description, priority, size, tools_needed, difficulty
) VALUES
      (
          UNHEX(REPLACE(UUID(),'-','')),
          'Implement Authentication',
          'User login/logout flow',
          'HIGH', 'SMALL', 'IDE', 'MEDIUM'
      ),
      (
          UNHEX(REPLACE(UUID(),'-','')),
          'Design ERD',
          'Database schema diagram',
          'MEDIUM','TINY','Draw.io','EASY'
      ),
      (
          UNHEX(REPLACE(UUID(),'-','')),
          'Setup CI/CD',
          'Configure pipeline',
          'HIGH','MEDIUM','Jenkins','HARD'
      );

-- ──────────── 7. Subtask ↔ Skill ────────────
INSERT INTO subtask_skill (subtask_id, skill_id) VALUES
                                                     (
                                                         (SELECT id FROM subtask WHERE name = 'Implement Authentication'),
                                                         (SELECT id FROM skill   WHERE name = 'Java')
                                                     ),
                                                     (
                                                         (SELECT id FROM subtask WHERE name = 'Implement Authentication'),
                                                         (SELECT id FROM skill   WHERE name = 'Spring')
                                                     ),
                                                     (
                                                         (SELECT id FROM subtask WHERE name = 'Design ERD'),
                                                         (SELECT id FROM skill   WHERE name = 'SQL')
                                                     ),
                                                     (
                                                         (SELECT id FROM subtask WHERE name = 'Setup CI/CD'),
                                                         (SELECT id FROM skill   WHERE name = 'Docker')
                                                     );

-- ──────────── 8. Tasks ────────────
INSERT INTO task (id, name, priority, size, description) VALUES
                                                             (
                                                                 UNHEX(REPLACE(UUID(),'-','')),
                                                                 'User Module','HIGH','MEDIUM','Core user features'
                                                             ),
                                                             (
                                                                 UNHEX(REPLACE(UUID(),'-','')),
                                                                 'Database Setup','MEDIUM','SMALL','Initialize schema'
                                                             ),
                                                             (
                                                                 UNHEX(REPLACE(UUID(),'-','')),
                                                                 'DevOps Pipeline','HIGH','LARGE','CI/CD integration'
                                                             );

-- ──────────── 9. Task Sessions ────────────
INSERT INTO task_session (
    id, user_id, task_id, date, notes, status
) VALUES
      (
          UNHEX(REPLACE(UUID(),'-','')),
          (SELECT id FROM `user` WHERE email = 'admin@pmapp.com'),
          (SELECT id FROM task   WHERE name  = 'User Module'),
          '2025-06-15','Initial dev started','IN_PROGRESS'
      ),
      (
          UNHEX(REPLACE(UUID(),'-','')),
          (SELECT id FROM `user` WHERE email = 'admin@pmapp.com'),
          (SELECT id FROM task   WHERE name  = 'Database Setup'),
          '2025-06-10','Schema finalized','COMPLETED'
      );

-- ──────────── 10. Subtask Entries ────────────
INSERT INTO subtask_entry (
    id, subtask_id, task_id, hours_worked, effort
) VALUES
      (
          UNHEX(REPLACE(UUID(),'-','')),
          (SELECT id FROM subtask WHERE name = 'Implement Authentication'),
          (SELECT id FROM task    WHERE name = 'User Module'),
          5, 8.0
      ),
      (
          UNHEX(REPLACE(UUID(),'-','')),
          (SELECT id FROM subtask WHERE name = 'Design ERD'),
          (SELECT id FROM task    WHERE name = 'Database Setup'),
          2, 3.0
      );
