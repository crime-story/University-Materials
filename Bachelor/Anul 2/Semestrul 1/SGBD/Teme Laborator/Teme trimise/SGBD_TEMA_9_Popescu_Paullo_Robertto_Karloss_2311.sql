-- TEMA 9
-- Popescu Paullo Robertto Karloss Grupa 2311

-- Laborator 7 PL/SQL
--1
CREATE TABLE error_Robertto (
    cod NUMBER(20), 
    mesaj VARCHAR2(2000)
);

DECLARE
    v_numar     PLS_INTEGER := &my_numar;
    v_cod       NUMBER(20);
    v_mesaj     VARCHAR2(2000);
    exceptie    EXCEPTION;
BEGIN
    IF v_numar < 0 THEN
        RAISE exceptie;
    ELSE
        DBMS_OUTPUT.PUT_LINE(SQRT(v_numar));
    END IF;
EXCEPTION
    WHEN exceptie THEN
        v_cod := -20002;
        v_mesaj := 'Numarul introdus este negativ!';
        INSERT INTO error_Robertto VALUES (v_cod, v_mesaj);
END;
/

SELECT * FROM error_Robertto;

--2
DECLARE
    v_salary        PLS_INTEGER := &my_salary;
    v_first_name    employees.first_name%TYPE;
    v_last_name     employees.last_name%TYPE;
BEGIN
    SELECT first_name, last_name INTO v_first_name, v_last_name
    FROM employees
    WHERE salary = v_salary;
    DBMS_OUTPUT.PUT_LINE(v_first_name || ' ' || v_last_name);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Nu exista niciun angajat cu acest salariu!');
    WHEN TOO_MANY_ROWS THEN
        DBMS_OUTPUT.PUT_LINE('Exista mai multi angajat cu acest salariu!');
END;
/

--3
UPDATE dept_Robertto
SET department_id = 150
WHERE department_id = 90;

DECLARE
    v_old_department_id     employees.department_id%TYPE := &my_old_department_id;
    v_new_department_id     employees.department_id%TYPE := &my_new_department_id;
    v_contor                PLS_INTEGER;
    exceptie EXCEPTION;
BEGIN
    SELECT 1 INTO v_contor
    FROM emp_Robertto
    WHERE department_id = v_old_department_id;
    
    IF v_contor = 1 THEN
        RAISE exceptie;
    ELSE
        UPDATE dept_Robertto
        SET department_id = v_new_department_id
        WHERE department_id = v_old_department_id;
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE ('Nu exista niciun departament cu acest id!');
    WHEN TOO_MANY_ROWS THEN
        DBMS_OUTPUT.PUT_LINE ('Nu puteti modifica codul unui departament in care lucreaza salariati!');
    WHEN exceptie THEN
        DBMS_OUTPUT.PUT_LINE ('Nu puteti modifica codul unui departament in care lucreaza salariati!');
END;
/

--4
DECLARE
    v_min                   PLS_INTEGER := &p_min;
    v_max                   PLS_INTEGER := &p_max;
    v_department_name       departments.department_name%TYPE;
    v_numar_emp             PLS_INTEGER;
    exceptie                EXCEPTION;
BEGIN
    SELECT COUNT(employee_id) INTO v_numar_emp
    FROM employees
    WHERE department_id = 10
    GROUP BY department_id;
    IF v_numar_emp <= v_max AND v_numar_emp >= v_min THEN
        SELECT department_name INTO v_department_name
        FROM departments
        WHERE department_id = 10;
        DBMS_OUTPUT.PUT_LINE(v_department_name);
    ELSE
        RAISE exceptie;
    END IF;
EXCEPTION
    WHEN exceptie THEN
        DBMS_OUTPUT.PUT_LINE('Numarul angajatilor din departamentul 10 nu se afla in intervalul dat!');
END;
/

--5
DECLARE
    v_department_id             departments.department_id%TYPE := &my_department_id;
    v_new_department_name       departments.department_name%TYPE := '&my_new_department_name';
BEGIN
    UPDATE dept_Robertto
    SET department_name = v_new_department_name 
    WHERE department_id = v_department_id;
    IF SQL%notfound THEN
        RAISE_APPLICATION_ERROR(-20004, 'Nu am gasit niciun departament cu acest id!');
    END IF;
END;
/

--6
DECLARE
    v_department_id         departments.department_id%TYPE := &my_department_id;
    v_location_id           departments.location_id%TYPE := &my_location_id;
    v_department_name       departments.department_name%TYPE;
    v_department_name2      departments.department_name%TYPE;
    v_gasit                 PLS_INTEGER;
BEGIN
    v_gasit := 1;
    SELECT department_name INTO v_department_name
    FROM departments
    WHERE location_id = v_location_id;
    v_gasit := 2;
    SELECT department_name INTO v_department_name2
    FROM departments
    WHERE department_id = v_department_id;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        dbms_output.put_line('Eroarea se produce in select-ul cu numarul ' || v_gasit || ' !');
END;
/
-- cea de a doua varianta ar fi cu o procedura