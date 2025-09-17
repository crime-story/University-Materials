-- TEMA 5
-- Popescu Paullo Robertto Karloss Grupa 2311

-- Laborator 3 PL/SQL

-- Ex 1
-- Rezolvați problema în SQL.
SELECT 
    CASE
        WHEN COUNT(e.employee_id) = 0 THEN 'In departamentul ' || d.department_name || ' nu lucreaza angajati'
        WHEN COUNT(e.employee_id) = 1 THEN 'In departamentul ' || d.department_name || ' lucreaza un angajat'
        ELSE 'In departamentul ' || d.department_name || ' lucreaza ' || count(e.employee_id) || ' angajati'
    END AS Departamente
FROM departments d, employees e
WHERE d.department_id = e.department_id (+)
GROUP BY d.department_name;
/

-- Ex 2 
-- a) Rezolvați problema folosind cursorul și o singură colecție.
DECLARE 
    TYPE make_pair IS RECORD(nume departments.department_name%TYPE, numar NUMBER(5));
    TYPE table_make_pair IS TABLE OF make_pair;
    t table_make_pair;
    
    CURSOR c IS
        SELECT department_name nume, COUNT(employee_id) nr 
        FROM departments d, employees e
        WHERE d.department_id = e.department_id(+)
        GROUP BY department_name;
BEGIN
    OPEN c;
    FETCH c BULK COLLECT INTO t limit 5;
    WHILE t.COUNT <> 0
        LOOP
            FOR i IN 1..t.LAST LOOP
                CASE
                    WHEN t(i).numar = 0 THEN DBMS_OUTPUT.PUT_LINE('In departamentul ' || t(i).nume ||  ' nu lucreaza angajati');
                    WHEN t(i).numar = 1 THEN DBMS_OUTPUT.PUT_LINE('In departamentul ' || t(i).nume || ' lucreaza un angajat');
                    ELSE DBMS_OUTPUT.PUT_LINE('In departamentul ' || t(i).nume || ' lucreaza ' || t(i).numar || ' angajati');
                END CASE;
            END LOOP;
            FETCH c BULK COLLECT INTO t LIMIT 5;
    END LOOP;
    CLOSE c;
END;
/

-- b) Rezolvați problema folosind doar colecții. 
DECLARE 
    TYPE make_pair IS RECORD(nume departments.department_name%TYPE, numar NUMBER(5));
    TYPE table_make_pair IS TABLE OF make_pair;
    t table_make_pair;
    
    indexStart number(5) := 1;
    indexEnd number(5);
BEGIN
    SELECT department_name nume, COUNT(employee_id) nr BULK COLLECT INTO t
    FROM departments d, employees e
    WHERE d.department_id = e.department_id(+)
    GROUP BY department_name;

    WHILE indexStart <= t.count
        LOOP 
            IF indexStart + 4 <= t.COUNT THEN
                indexEnd := 4;
            ELSE 
                indexEnd := MOD(t.COUNT, 5) - 1;
            END IF;
            FOR i IN indexStart..(indexStart + indexEnd) LOOP
                CASE
                    WHEN t(i).numar = 0 THEN DBMS_OUTPUT.PUT_LINE('In departamentul ' || t(i).nume ||  ' nu lucreaza angajati');
                    WHEN t(i).numar = 1 THEN DBMS_OUTPUT.PUT_LINE('In departamentul ' || t(i).nume || ' lucreaza un angajat');
                    ELSE DBMS_OUTPUT.PUT_LINE('In departamentul ' || t(i).nume || ' lucreaza ' || t(i).numar || ' angajati');
                END CASE;
            END LOOP;
            indexstart := indexstart + indexEnd + 1;
        END LOOP;
END;
/