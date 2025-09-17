-- TEMA 3
-- Popescu Paullo Robertto Karloss Grupa 2311

-- Laborator 1 PL/SQL
-- Ex 6
VARIABLE v_nume_departament VARCHAR2(35);
VARIABLE v_nr_angajati INTEGER;
BEGIN
    SELECT department_name, COUNT(*)
    INTO :v_nume_departament, :v_nr_angajati
    FROM employees e, departments d
    WHERE e.department_id = d.department_id
    GROUP BY department_name
    HAVING COUNT(*) = (SELECT MAX(COUNT(*))
                        FROM employees
                        GROUP BY department_id);
    DBMS_OUTPUT.PUT_LINE('Departamentul '|| :v_nume_departament || ' cu ' || :v_nr_angajati || ' angajati.');
END;
/
PRINT v_nume_departament;
PRINT v_nr_angajati;

-- Sectiunea Exercitii
-- Ex 2 b) SQL
CREATE TABLE octombrie_Robertto(id number, data date);
COMMIT;

DECLARE
    d DATE := TRUNC(sysdate, 'mm');
    n NUMBER := EXTRACT(DAY FROM last_day(SYSDATE));
begin
    FOR i IN 1..n LOOP
          INSERT INTO octombrie_Robertto
          VALUES (i, d);
          d := d+1;
    END LOOP;
END;

SELECT * FROM octombrie_Robertto;

SELECT data AS "Data", (SELECT COUNT(*) 
FROM rental 
WHERE book_date = d.data) AS "Numar de imprumuturi"
FROM octombrie_Robertto d;

-- Ex 3
-- se introduce numele complet (last_name) al unui membru de la tastatura
DECLARE
    v_nume_membru VARCHAR2(100) := '&nume';
    v_nr_filme_imp INTEGER;
BEGIN
    SELECT COUNT(title) INTO v_nr_filme_imp
    FROM rental r, member m, title t
    WHERE t.title_id = r.title_id
    AND r.member_id = m.member_id
    AND LOWER(last_name) LIKE (LOWER(v_nume_membru))
    AND ROWNUM = 1;
    DBMS_OUTPUT.PUT_LINE('Membrul cu numele "' || v_nume_membru || '" a imprumutat ' || nvl(v_nr_filme_imp, 0) || ' filme.');
END;

-- Ex 4
DECLARE
    v_nume_membru VARCHAR2(100) := '&nume';
    v_nr_filme_imp INTEGER := 0;
    v_procent_imp INTEGER := 0;
    v_nr_titluri INTEGER := 0;
    v_cod_membru member.member_id%TYPE := 0; 
BEGIN
    SELECT COUNT(*) INTO v_nr_titluri
    FROM title;
    SELECT member_id INTO v_cod_membru
    FROM member 
    WHERE LOWER(last_name) LIKE LOWER(v_nume_membru);
    SELECT COUNT(DISTINCT title_id) INTO v_nr_filme_imp
    FROM rental r, member m
    WHERE r.member_id = m.member_id
    GROUP BY m.member_id
    HAVING m.member_id = v_cod_membru;
    DBMS_OUTPUT.PUT_LINE('Membrul cu numele "' || v_nume_membru || '" a imprumutat ' || v_nr_filme_imp || ' filme face parte din: ');
    v_procent_imp := v_nr_filme_imp * 100 / v_nr_titluri;
    IF v_procent_imp >= 75 THEN
        DBMS_OUTPUT.PUT_LINE('Categoria 1');
    ELSIF v_procent_imp >= 50 THEN
        DBMS_OUTPUT.PUT_LINE('Categoria 2');
    ELSIF v_procent_imp >= 25 THEN
        DBMS_OUTPUT.PUT_LINE('Categoria 3');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Categoria 4');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('NO_DATA_FOUND');
END;

SELECT * FROM MEMBER;

-- Ex 5
CREATE TABLE member_Robertto AS (SELECT * FROM MEMBER);

ALTER TABLE member_Robertto ADD CONSTRAINT PK_member_Robertto PRIMARY KEY (member_id);

ALTER TABLE member_Robertto ADD discount NUMBER;

COMMIT;

SELECT * FROM member_Robertto;

SET VERIFY OFF
DECLARE
    v_cod_membru member_Robertto.member_id%TYPE := &cod;
    v_nr_filme_imp INTEGER := 0;
    v_procent_imp INTEGER := 0;
    v_nr_titluri INTEGER := 0;
BEGIN
    SELECT COUNT(*) INTO v_nr_titluri
    FROM title;
    SELECT COUNT(DISTINCT title_id) INTO v_nr_filme_imp
    FROM rental r, member_Robertto m
    WHERE r.member_id = m.member_id
    GROUP BY m.member_id
    HAVING m.member_id = v_cod_membru;
    v_procent_imp := v_nr_filme_imp * 100 / v_nr_titluri;
    IF v_procent_imp >= 75 THEN
        UPDATE member_Robertto
        SET discount = 10
        WHERE member_id = v_cod_membru;
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('S-a modificat 1 rand din tabel!');
    ELSIF v_procent_imp >= 50 THEN
        UPDATE member_Robertto
        SET discount = 5
        WHERE member_id = v_cod_membru;
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('S-a modificat 1 rand din tabel!');
    ELSIF v_procent_imp >= 25 THEN
        UPDATE member_Robertto
        SET discount = 3
        WHERE member_id = v_cod_membru;
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('S-a modificat 1 rand din tabel!');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Nu s-a modificat niciun rand din tabel!');
    END IF;       
EXCEPTION
    WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('NO_DATA_FOUND');
END;
/ 
SET VERIFY ON;

SELECT * FROM member_Robertto;