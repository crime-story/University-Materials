-- TEMA 8
-- Popescu Paullo Robertto Karloss Grupa 2311

-- Laborator 6 PL/SQL
--5
--g
CREATE OR REPLACE TRIGGER trig5_av INSTEAD OF
    INSERT OR DELETE OR UPDATE ON v_info_av
    FOR EACH ROW
DECLARE
    nr1   PLS_INTEGER;
    nr2   PLS_INTEGER;
BEGIN
    IF inserting THEN
        SELECT COUNT(*) INTO nr2
        FROM info_dept_av
        WHERE :new.id_dept = id;
        IF nr2 = 0 THEN
            INSERT INTO info_dept_av VALUES (:new.id_dept, 'idk', 0, 1);
        END IF;
        SELECT COUNT(*) INTO nr1
        FROM info_emp_av
        WHERE :new.id = id;
        IF nr1 = 0 THEN
            INSERT INTO info_emp_av VALUES (
                :new.id,
                :new.nume,
                :new.prenume,
                :new.salariu,
                :new.id_dept
            );
        END IF;
        IF nr1 = 0 AND nr2 = 0 THEN
            UPDATE info_dept_av
            SET
                plati = plati + :new.salariu
            WHERE
                id = :new.id_dept;

        ELSE
            UPDATE info_dept_av
            SET plati = plati + :new.salariu
            WHERE id = :new.id_dept;
        END IF;

    ELSIF deleting THEN
        DELETE FROM info_emp_av
        WHERE id = :old.id;

        UPDATE info_dept_av
        SET plati = plati - :old.salariu
        WHERE id = :old.id_dept;

    ELSIF updating('salariu') THEN
        UPDATE info_emp_av
        SET salariu = :new.salariu
        WHERE id = :old.id;

        UPDATE info_dept_av
        SET plati = plati - :old.salariu + :new.salariu
        WHERE id = :old.id_dept;

    ELSIF updating('id_dept') THEN
        UPDATE info_emp_av
        SET id_dept = :new.id_dept
        WHERE id = :old.id;

        UPDATE info_dept_av
        SET plati = plati - :old.salariu
        WHERE id = :old.id_dept;

        UPDATE info_dept_av
        SET plati = plati + :new.salariu
        WHERE id = :new.id_dept;
    END IF;
END;
/

--h

INSERT INTO info_emp_av VALUES (
    131,
    'NumeiDK',
    'Prenumeidk',
    151,
    12
);

INSERT INTO info_emp_av VALUES (
    123,
    'NumeCidk',
    'PrenumeidKk',
    123,
    15
);

--i

UPDATE v_info_av
SET
    nume = 'NumeModificat1'
WHERE
    id = 215;
---OBSERV CA NU SE SCHIMBA

--j

CREATE OR REPLACE TRIGGER trig5_av INSTEAD OF
    INSERT OR DELETE OR UPDATE ON v_info_av
    FOR EACH ROW
DECLARE
    nr1   NUMBER;
    nr2   NUMBER;
BEGIN
    IF inserting THEN
        SELECT COUNT(*) INTO nr2
        FROM info_dept_av
        WHERE :new.id_dept = id;

        IF nr2 = 0 THEN
            INSERT INTO info_dept_av VALUES (
                :new.id_dept,
                'Unknown',
                0,
                1
            );

        END IF;

        SELECT COUNT(*) INTO nr1
        FROM info_emp_av
        WHERE :new.id = id;

        IF nr1 = 0 THEN
            INSERT INTO info_emp_av VALUES (
                :new.id,
                :new.nume,
                :new.prenume,
                :new.salariu,
                :new.id_dept
            );

        END IF;

        IF nr1 = 0 AND nr2 = 0 THEN
            UPDATE info_dept_av
            SET plati = plati + :new.salariu
            WHERE id = :new.id_dept;

        ELSE
            UPDATE info_dept_av
            SET plati = plati + :new.salariu
            WHERE id = :new.id_dept;
        END IF;

    ELSIF deleting THEN
        DELETE FROM info_emp_av
        WHERE id = :old.id;

        UPDATE info_dept_av
        SET plati = plati - :old.salariu
        WHERE id = :old.id_dept;

    ELSIF updating('salariu') THEN
        UPDATE info_emp_av
        SET salariu = :new.salariu
        WHERE id = :old.id;

        UPDATE info_dept_av
        SET plati = plati - :old.salariu + :new.salariu
        WHERE id = :old.id_dept;

    ELSIF updating('id_dept') THEN
        UPDATE info_emp_av
        SET id_dept = :new.id_dept
        WHERE id = :old.id;

        UPDATE info_dept_av
        SET plati = plati - :old.salariu
        WHERE id = :old.id_dept;

        UPDATE info_dept_av
        SET plati = plati + :new.salariu
        WHERE id = :new.id_dept;
    ELSIF updating('nume') THEN
        UPDATE info_emp_av
        SET nume = :new.nume
        WHERE id = :old.id;
    ELSIF updating('prenume') THEN
        UPDATE info_emp_av
        SET prenume = :new.prenume
        WHERE id = :old.id;
    ELSIF updating('nume_dept') THEN
        UPDATE info_dept_av
        SET nume_dept = :new.nume_dept
        WHERE id = :old.id_dept;
    END IF;
END;
/

--k

UPDATE v_info_av
SET
    nume = 'NumeModificat1'
WHERE
    id = 215;

UPDATE v_info_av
SET
    prenume = 'PrenumeModificat1'
WHERE
    id = 215;

UPDATE v_info_av
SET
    nume_dept = 'DeptModificat1'
WHERE
    id_dept = 270;

UPDATE v_info_av
SET
    nume_dept = 'DeptModificat2'
WHERE
    id_dept = 50;

ROLLBACK;

SELECT
    *
FROM
    v_info_av
ORDER BY
    1 DESC;

SELECT
    *
FROM
    info_emp_av
ORDER BY
    1 DESC;

SELECT
    *
FROM
    info_dept_av
ORDER BY
    1 DESC;

-- Sectiunea Exercitii
--4
CREATE OR REPLACE TRIGGER trig4_Robertto
    BEFORE UPDATE OR INSERT OR DELETE ON emp_Robertto
    FOR EACH ROW
DECLARE
    nr_ang NUMBER(20);
BEGIN
    FOR i IN (SELECT * FROM departments) LOOP
        SELECT COUNT(employee_id) INTO nr_ang
            FROM emp_Robertto
            WHERE emp_Robertto.department_id = i.department_id
            GROUP BY DEPARTMENT_ID;
        IF(nr_ang > 45) THEN
            RAISE_APPLICATION_ERROR(-20001,'Tabelul nu poate fi actualizat!');
        END IF;
    END LOOP;
END;
/

DROP TRIGGER trig4_Robertto;

--5
CREATE OR REPLACE TRIGGER trig5_Robertto
    BEFORE DELETE OR UPDATE ON dept_Robertto
    FOR EACH ROW
DECLARE
    nr_ang NUMBER(20);
BEGIN
    IF DELETING THEN
        DELETE FROM emp_Robertto e
        WHERE :OLD.department_id = e.department_id;
    END IF;
    
    IF UPDATING THEN
        UPDATE emp_Robertto e
        SET e.department_id = :NEW.department_id
        WHERE e.department_id = :OLD.department_id;
    END IF;
END;
/

DROP TRIGGER trig5_Roberto;

--6
CREATE SEQUENCE seq_err_Robertto
    START WITH 1
    INCREMENT BY 1;
    
CREATE TABLE errors_Robertto (
    error_id NUMBER(20), 
    user_id VARCHAR(50), 
    nume_bd VARCHAR(50),
    erori VARCHAR(2000), 
    data DATE
);

CREATE OR REPLACE TRIGGER trig6_Robertto
    AFTER SERVERERROR ON DATABASE
BEGIN
    INSERT INTO errors_Robertto
    VALUES (seq_err_Robertto.NEXTVAL, SYS.LOGIN_USER, SYS.DATABASE_NAME,
            DBMS_UTILITY.FORMAT_ERROR_STACK, SYSDATE);
END;
/

DROP TRIGGER trig6_Robertto;