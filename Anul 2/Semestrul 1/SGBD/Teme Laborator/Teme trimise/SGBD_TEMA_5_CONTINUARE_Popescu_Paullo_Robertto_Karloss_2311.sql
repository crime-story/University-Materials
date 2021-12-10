-- TEMA 5 CONTINUARE
-- Popescu Paullo Robertto Karloss Grupa 2311

-- Laborator 3 PL/SQL

-- Ex 10 Keyword
-- Varianta 1.1 - cursor clasic
DECLARE
    CURSOR c IS
        SELECT department_id, department_name
        FROM departments
        WHERE department_id IN (10,20,30,40);
    CURSOR c2 IS
        SELECT last_name, department_id
        FROM employees;
    v_department_id departments.department_id%TYPE;
    v_department_name departments.department_name%TYPE;
    v2_last_name employees.last_name%TYPE;
BEGIN
    OPEN c;
    LOOP
        FETCH c INTO v_department_id, v_department_name;
        EXIT WHEN c%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        DBMS_OUTPUT.PUT_LINE ('DEPARTAMENT '|| v_department_name);
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        OPEN c2;
        LOOP
            FETCH c2 INTO v2_last_name, v2_department_id;
            EXIT WHEN c2%NOTFOUND;
            IF v_department_id = v2_department_id THEN
                DBMS_OUTPUT.PUT_LINE (v2_last_name);
            END IF;
        END LOOP;
        CLOSE c2;
    END LOOP;
    CLOSE c;
END;
/

-- Acelasi lucru dar cu cursor parametrizat
DECLARE
    CURSOR c IS
        SELECT department_id, department_name
        FROM departments
        WHERE department_id IN (10,20,30,40);
    CURSOR c_parametrizat(department NUMBER) IS
                                        SELECT last_name
                                        FROM employees
                                        WHERE department_id = department;
    v_department_id departments.department_id%TYPE;
    v_department_name departments.department_name%TYPE;
    v2_department_id employees.department_id%TYPE;
    v2_last_name employees.last_name%TYPE;
BEGIN
    OPEN c;
    LOOP
        FETCH c INTO v_department_id, v_department_name;
        EXIT WHEN c%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        DBMS_OUTPUT.PUT_LINE ('DEPARTAMENT '|| v_department_name);
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        OPEN c_parametrizat(v_department_id);
        LOOP
            FETCH c_parametrizat INTO v2_last_name;
            EXIT WHEN c_parametrizat%NOTFOUND;
            DBMS_OUTPUT.PUT_LINE (v2_last_name);
        END LOOP;
        CLOSE c_parametrizat;
    END LOOP;
    CLOSE c;
END;
/

BEGIN
    FOR v_dept IN (SELECT department_id, department_name
                    FROM departments
                    WHERE department_id IN (10,20,30,40))
    LOOP
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        DBMS_OUTPUT.PUT_LINE ('DEPARTAMENT '||v_dept.department_name);
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        FOR v_emp IN (SELECT last_name
                        FROM employees
                        WHERE department_id = v_dept.department_id)
        LOOP
            DBMS_OUTPUT.PUT_LINE (v_emp.last_name);
        END LOOP;
    END LOOP;
END;
/

-- Varianta 1.21 - ciclu cursor
DECLARE
    CURSOR c IS
        SELECT department_id, department_name
        FROM departments
        WHERE department_id IN (10,20,30,40);
    CURSOR c2 IS
        SELECT last_name, department_id
        FROM employees;
BEGIN
    FOR i IN c LOOP
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        DBMS_OUTPUT.PUT_LINE ('DEPARTAMENT '||i.department_name);
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        FOR j IN c2 LOOP
            IF j.department_id = i.department_id THEN
                DBMS_OUTPUT.PUT_LINE (j.last_name);
            END IF;
        END LOOP;
    END LOOP;
END;
/

-- Acelasi lucru dar cu cursor parametrizat
DECLARE
    CURSOR c IS
        SELECT department_id, department_name
        FROM departments
        WHERE department_id IN (10,20,30,40);
    CURSOR c_parametrizat(department NUMBER) IS
                                        SELECT last_name
                                        FROM employees
                                        WHERE department_id = department;
BEGIN
    FOR i IN c LOOP
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        DBMS_OUTPUT.PUT_LINE ('DEPARTAMENT '||i.department_name);
        DBMS_OUTPUT.PUT_LINE('-------------------------------------');
        FOR j IN c_parametrizat(i.department_id) LOOP
            DBMS_OUTPUT.PUT_LINE (j.last_name);
        END LOOP;
    END LOOP;
END;
/

-- Sectiunea exercitii
-- Ex 1
-- Pentru fiecare job (titlu – care va fi afișat o singură dată) obțineți lista angajaților (nume și
-- salariu) care lucrează în prezent pe jobul respectiv. Tratați cazul în care nu există angajați care 
-- să lucreze în prezent pe un anumit job. Rezolvați problema folosind:

-- b) ciclu cursoare
DECLARE
    CURSOR j IS 
        SELECT job_id, job_title
        FROM jobs;
    CURSOR e(job_curent VARCHAR2) IS 
                                SELECT last_name, salary 
                                FROM employees 
                                WHERE job_id = job_curent;
    v_nr_angajati PLS_INTEGER;
BEGIN
    FOR i IN j LOOP
        DBMS_OUTPUT.PUT_LINE('Job ' || i.job_title || ':');
        v_nr_angajati := 0;
        FOR j IN e(i.job_id) LOOP
            v_nr_angajati := v_nr_angajati + 1;
            DBMS_OUTPUT.PUT_LINE('# Angajatul ' || j.last_name || ' ' || j.salary);
        END LOOP;
        IF v_nr_angajati = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Fara angajati!');
        END IF;
    END LOOP;
END;
/

-- d) expresii cursor
DECLARE
    TYPE refcursor is REF CURSOR;
    CURSOR j IS
        SELECT j.job_title, CURSOR (SELECT last_name, salary
                                    FROM employees e
                                    WHERE e.job_id = j.job_id)
        FROM jobs j;
    titlu_job jobs.job_title%TYPE;
    v_cursor refcursor;
    v_nume_angajat employees.last_name%TYPE;
    v_salariu_angajat employees.salary%TYPE;
    v_nr_angajati PLS_INTEGER;
BEGIN
    OPEN j;
    LOOP
        FETCH j INTO titlu_job, v_cursor;
        EXIT WHEN j%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('# Job ' || titlu_job || ':');
        v_nr_angajati := 0;
        LOOP
            FETCH v_cursor INTO v_nume_angajat, v_salariu_angajat;
            EXIT WHEN v_cursor%NOTFOUND;
            v_nr_angajati := v_nr_angajati + 1;
            DBMS_OUTPUT.PUT_LINE('Angajatul ' || v_nume_angajat || ' ' || v_salariu_angajat);
        END LOOP;
        IF v_nr_angajati = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Fara angajati!');
        END IF;
    END LOOP;
    CLOSE j;
END;
/

-- Ex 2
-- Modificați exercițiul anterior astfel încât să obțineți și următoarele informații:
-- a) un număr de ordine pentru fiecare angajat care va fi resetat pentru fiecare job
-- b) pentru fiecare job
--  o numărul de angajați 
--  o valoarea lunară a veniturilor angajaților 
--  o valoarea medie a veniturilor angajaților 
-- c) indiferent job
--  o numărul total de angajați
--  o valoarea totală lunară a veniturilor angajaților
--  o valoarea medie a veniturilor angajaților

-- Folosim ciclu cursor
DECLARE
    CURSOR j IS 
        SELECT job_id, job_title
        FROM jobs;
    CURSOR e(id_job_curent VARCHAR2) IS 
                                SELECT last_name, salary
                                FROM employees 
                                WHERE job_id = id_job_curent;
    v_nr_angajati_job   NUMBER;
    v_nr_total_angajati NUMBER := 0;
    v_salariu_job       NUMBER;
    v_salariu_total     NUMBER := 0;
BEGIN
    FOR i IN j LOOP
        DBMS_OUTPUT.PUT_LINE('# Job ' || i.job_title || ':');
        v_nr_angajati_job := 0;
        v_salariu_job := 0;
        FOR k IN e(i.job_id) LOOP
            v_nr_angajati_job := v_nr_angajati_job + 1;
            v_salariu_job := v_salariu_job + k.salary;
        END LOOP;
        
        v_salariu_total := v_salariu_total + v_salariu_job;
        v_nr_total_angajati := v_nr_total_angajati + v_nr_angajati_job;
        
        IF v_nr_angajati_job = 1 THEN
            DBMS_OUTPUT.PUT_LINE('Jobul are ' || v_nr_angajati_job || ' angajat.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Jobul are ' || v_nr_angajati_job || ' angajati.');
        END IF;
        
        IF v_nr_angajati_job > 0 THEN
            DBMS_OUTPUT.PUT_LINE('Salariul total acordat este de ' || TRUNC(v_salariu_job, 2) || ' lei/luna si salariul mediu este de ' || TRUNC(v_salariu_job/v_nr_angajati_job, 2) || ' lei/luna.');
            DBMS_OUTPUT.PUT_LINE('## Lista angajatilor este:');            
        END IF;
        
        FOR k IN e(i.job_id) LOOP
            DBMS_OUTPUT.PUT_LINE('-> '|| k.last_name);
        END LOOP;
        
        IF v_nr_angajati_job = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Fara angajati!');        
        END IF;
        DBMS_OUTPUT.NEW_LINE();
        
    END LOOP;
    
    DBMS_OUTPUT.PUT_LINE('<< Concluzii: >>');
    IF v_nr_total_angajati = 1 THEN
        DBMS_OUTPUT.PUT_LINE('In companie lucreaza doar ' || v_nr_total_angajati || ' angajat.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Avem in total ' || v_nr_total_angajati || ' de angajati in companie.');
    END IF;
    DBMS_OUTPUT.PUT_LINE('Salariul total acordat in companie este de ' || TRUNC(v_salariu_total, 2) || ' lei/luna.');
    DBMS_OUTPUT.PUT_LINE('Salariul mediu acordat in companie este de ' || TRUNC(v_salariu_total / v_nr_total_angajati, 2) || ' lei/luna.');
END;
/

-- Ex 3
-- Modificați exercițiul anterior astfel încât să obțineți suma totală alocată lunar pentru plata 
-- salariilor și a comisioanelor tuturor angajaților, iar pentru fiecare angajat cât la sută din această 
-- sumă câștigă lunar.

DECLARE
    CURSOR j IS
        SELECT last_name, salary, commission_pct
        FROM employees;
    v_salariu_angajat   NUMBER := 0;
    v_salariu_total     NUMBER := 0;
BEGIN
    FOR i IN j LOOP
        v_salariu_total := v_salariu_total + i.salary;
        IF i.commission_pct IS NOT NULL THEN -- daca plateste comision, din salariul lui scadem comision * salariu
            v_salariu_total := v_salariu_total + i.salary * i.commission_pct;
        END IF;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('Suma totala alocata lunar pentru plata salariilor ' || trunc(v_salariu_total, 2) || ' lei.');
    DBMS_OUTPUT.NEW_LINE();
    FOR i IN j LOOP
        v_salariu_angajat := i.salary;
        IF i.commission_pct IS NOT NULL THEN
            v_salariu_angajat := v_salariu_angajat * (1 + i.commission_pct);
        END IF;
        DBMS_OUTPUT.PUT_LINE('Angajatul ' || i.last_name || ' a primit salariul de' || ': ' || v_salariu_angajat || ' lei, care valoreaza ' || TRUNC(100 * v_salariu_angajat / v_salariu_total, 5) || '% din totalul oferit de companie!');
    END LOOP;
END;
/

-- Ex 4
-- Modificați exercițiul anterior astfel încât să obțineți pentru fiecare job primii 5 angajați care 
-- câștigă cel mai mare salariu lunar. Specificați dacă pentru un job sunt mai puțin de 5 angajați.

DECLARE
    CURSOR j IS 
        SELECT job_id, job_title
        FROM jobs;
    CURSOR e(id_job_curent VARCHAR2) IS 
                                SELECT last_name, salary
                                FROM employees 
                                WHERE job_id = id_job_curent;
    v_nr_angajati_job PLS_INTEGER;
BEGIN
    FOR i IN j LOOP
        DBMS_OUTPUT.PUT_LINE('# Job ' || i.job_title || ':');
        v_nr_angajati_job := 0;
        FOR k IN e(i.job_id) LOOP
            IF v_nr_angajati_job = 5 THEN 
                EXIT;
            END IF;
            v_nr_angajati_job := v_nr_angajati_job + 1;
            DBMS_OUTPUT.PUT_LINE(k.last_name || ' are salariul de ' || k.salary || ' lei lunar.');
        END LOOP;
        
        IF v_nr_angajati_job < 5 THEN
            DBMS_OUTPUT.PUT_LINE('SUNT MAI PUTIN DE 5 ANGAJATI PE ACEST JOB!');
        END IF;
        DBMS_OUTPUT.NEW_LINE();
    END LOOP;
END;
/

-- Ex 5
-- Modificați exercițiul anterior astfel încât să obțineți pentru fiecare job top 5 angajați. Dacă 
-- există mai mulți angajați care respectă criteriul de selecție care au același salariu, atunci aceștia 
-- vor ocupa aceeași poziție în top 5.

DECLARE
    CURSOR j IS 
        SELECT job_id, job_title
        FROM jobs;
    CURSOR e(id_job_curent VARCHAR2) IS 
                                SELECT last_name, salary
                                FROM employees 
                                WHERE job_id = id_job_curent
                                ORDER BY salary DESC;
    v_nr_angajati_job PLS_INTEGER;
    v_ultimul_salariu_job PLS_INTEGER;
BEGIN
    FOR i IN j LOOP
        DBMS_OUTPUT.PUT_LINE('# Job ' || i.job_title || ':');
        v_nr_angajati_job := 0;
        FOR k IN e(i.job_id) LOOP
            IF v_nr_angajati_job = 5 THEN 
                EXIT;
            END IF;
            v_nr_angajati_job := v_nr_angajati_job + 1;
            v_ultimul_salariu_job := k.salary;
        END LOOP;
        
        FOR k IN e(i.job_id) LOOP
            IF k.salary < v_ultimul_salariu_job THEN
                EXIT;
            END IF;
            DBMS_OUTPUT.PUT_LINE(k.last_name || ' are salariul de ' || k.salary || ' lei lunar.');
        END LOOP;
        
        IF v_nr_angajati_job < 5 THEN
            DBMS_OUTPUT.PUT_LINE('SUNT MAI PUTIN DE 5 ANGAJATI PE ACEST JOB!');
        END IF;
        DBMS_OUTPUT.NEW_LINE();
    END LOOP;
END;
/