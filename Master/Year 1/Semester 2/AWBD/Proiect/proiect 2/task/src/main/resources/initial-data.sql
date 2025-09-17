-- ──────────── 0. Disable foreign keys & truncate ────────────
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE user_role;
TRUNCATE TABLE task_session;
TRUNCATE TABLE task;
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
