-- ======================== H2 SEED ========================

-- 0) Dezactivează FK ca să poți goli tabelele în orice ordine
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE USER_ROLE;
TRUNCATE TABLE TASK_SESSION;
TRUNCATE TABLE SUBTASK_ENTRY;
TRUNCATE TABLE SUBTASK_SKILL;
TRUNCATE TABLE PERSONAL_OBJECTIVE;
TRUNCATE TABLE TASK;
TRUNCATE TABLE SUBTASK;
TRUNCATE TABLE SKILL;
TRUNCATE TABLE "user";
TRUNCATE TABLE ROLE;

-- 1) Roles
INSERT INTO ROLE (ID, ROLE_NAME) VALUES
                                     (RANDOM_UUID(), 'ROLE_ADMIN'),
                                     (RANDOM_UUID(), 'ROLE_USER');

-- 2) Users
INSERT INTO "user" (
    ID, EMAIL, PASSWORD,
    ACCOUNT_NON_EXPIRED, ACCOUNT_NON_LOCKED,
    CREDENTIALS_NON_EXPIRED, ENABLED
) VALUES
      (
          RANDOM_UUID(),
          'admin@pmapp.com',
          '$2a$10$PDIkEfJysuBypngQBn00MutewMs5qmnbGNnb0spCLYCRcC35YGO/2',
          TRUE, TRUE, TRUE, TRUE
      ),
      (
          RANDOM_UUID(),
          'user@pmapp.com',
          '$2a$10$eBjBfkT2Gb9JHSObuFGtkO7wQW20d1pUaDYKKKiVy/Y55hq7lnJlG',
          TRUE, TRUE, TRUE, TRUE
      );

-- 3) User ↔ Role
INSERT INTO USER_ROLE (USER_ID, ROLE_ID)
SELECT u.ID, r.ID
FROM "user" u JOIN ROLE r ON r.ROLE_NAME = 'ROLE_ADMIN'
WHERE u.EMAIL = 'admin@pmapp.com';

INSERT INTO USER_ROLE (USER_ID, ROLE_ID)
SELECT u.ID, r.ID
FROM "user" u JOIN ROLE r ON r.ROLE_NAME = 'ROLE_USER'
WHERE u.EMAIL = 'admin@pmapp.com';

INSERT INTO USER_ROLE (USER_ID, ROLE_ID)
SELECT u.ID, r.ID
FROM "user" u JOIN ROLE r ON r.ROLE_NAME = 'ROLE_USER'
WHERE u.EMAIL = 'user@pmapp.com';

-- 4) Personal Objective
INSERT INTO PERSONAL_OBJECTIVE (
    ID, TARGET_HOURS, DEADLINE, DESCRIPTION, USER_ID
) VALUES (
             RANDOM_UUID(),
             120.0,
             DATE '2025-09-30',
             'Lansare MVP versiunea 1.0 în septembrie 2025',
             (SELECT ID FROM "user" WHERE EMAIL = 'admin@pmapp.com')
         );

-- 5) Skills
INSERT INTO SKILL (ID, NAME) VALUES
                                 (RANDOM_UUID(), 'Java'),
                                 (RANDOM_UUID(), 'SQL'),
                                 (RANDOM_UUID(), 'Spring'),
                                 (RANDOM_UUID(), 'Docker');

-- 6) Subtasks
INSERT INTO SUBTASK (
    ID, NAME, DESCRIPTION, PRIORITY, SIZE, TOOLS_NEEDED, DIFFICULTY
) VALUES
      (
          RANDOM_UUID(),
          'Implement Authentication',
          'User login/logout flow',
          'HIGH','SMALL','IDE','MEDIUM'
      ),
      (
          RANDOM_UUID(),
          'Design ERD',
          'Database schema diagram',
          'MEDIUM','TINY','Draw.io','EASY'
      ),
      (
          RANDOM_UUID(),
          'Setup CI/CD',
          'Configure pipeline',
          'HIGH','MEDIUM','Jenkins','HARD'
      );

-- 7) Subtask ↔ Skill
INSERT INTO SUBTASK_SKILL (SUBTASK_ID, SKILL_ID) VALUES
                                                     (
                                                         (SELECT ID FROM SUBTASK WHERE NAME = 'Implement Authentication'),
                                                         (SELECT ID FROM SKILL   WHERE NAME = 'Java')
                                                     ),
                                                     (
                                                         (SELECT ID FROM SUBTASK WHERE NAME = 'Implement Authentication'),
                                                         (SELECT ID FROM SKILL   WHERE NAME = 'Spring')
                                                     ),
                                                     (
                                                         (SELECT ID FROM SUBTASK WHERE NAME = 'Design ERD'),
                                                         (SELECT ID FROM SKILL   WHERE NAME = 'SQL')
                                                     ),
                                                     (
                                                         (SELECT ID FROM SUBTASK WHERE NAME = 'Setup CI/CD'),
                                                         (SELECT ID FROM SKILL   WHERE NAME = 'Docker')
                                                     );

-- 8) Tasks
INSERT INTO TASK (ID, NAME, PRIORITY, SIZE, DESCRIPTION) VALUES
                                                             (RANDOM_UUID(), 'User Module',     'HIGH','MEDIUM','Core user features'),
                                                             (RANDOM_UUID(), 'Database Setup',  'MEDIUM','SMALL','Initialize schema'),
                                                             (RANDOM_UUID(), 'DevOps Pipeline', 'HIGH','LARGE','CI/CD integration');

-- 9) Task Sessions
INSERT INTO TASK_SESSION (ID, USER_ID, TASK_ID, DATE, NOTES, STATUS) VALUES
                                                                         (
                                                                             RANDOM_UUID(),
                                                                             (SELECT ID FROM "user" WHERE EMAIL = 'admin@pmapp.com'),
                                                                             (SELECT ID FROM TASK    WHERE NAME  = 'User Module'),
                                                                             DATE '2025-06-15','Initial dev started','IN_PROGRESS'
                                                                         ),
                                                                         (
                                                                             RANDOM_UUID(),
                                                                             (SELECT ID FROM "user" WHERE EMAIL = 'admin@pmapp.com'),
                                                                             (SELECT ID FROM TASK    WHERE NAME  = 'Database Setup'),
                                                                             DATE '2025-06-10','Schema finalized','COMPLETED'
                                                                         );

-- 10) Subtask Entries
INSERT INTO SUBTASK_ENTRY (ID, SUBTASK_ID, TASK_ID, HOURS_WORKED, EFFORT) VALUES
                                                                              (
                                                                                  RANDOM_UUID(),
                                                                                  (SELECT ID FROM SUBTASK WHERE NAME = 'Implement Authentication'),
                                                                                  (SELECT ID FROM TASK    WHERE NAME = 'User Module'),
                                                                                  5, 8.0
                                                                              ),
                                                                              (
                                                                                  RANDOM_UUID(),
                                                                                  (SELECT ID FROM SUBTASK WHERE NAME = 'Design ERD'),
                                                                                  (SELECT ID FROM TASK    WHERE NAME = 'Database Setup'),
                                                                                  2, 3.0
                                                                              );

-- 11) Reactivează FK
SET REFERENTIAL_INTEGRITY TRUE;
