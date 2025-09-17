-- TEMA 6
-- Popescu Paullo Robertto Karloss Grupa 2311

-- Laborator 4 PL/SQL
-- Ex 5
CREATE OR REPLACE FUNCTION f5_Robertto
RETURN NUMBER IS
    CURSOR c IS SELECT department_name nume, department_id id
                FROM departments;
    v_day VARCHAR(20);
    TYPE ln IS TABLE OF VARCHAR(50);
    TYPE ls IS TABLE OF NUMBER;
    TYPE lv IS TABLE OF NUMBER;
    TYPE d IS TABLE OF VARCHAR(20);
    zile d := d();
    listanum ln := ln();
    listasal ls := ls();
    listavec lv := lv();
    cnt PLS_INTEGER;
    maxim PLS_INTEGER;
BEGIN
    FOR i IN c LOOP
        DBMS_OUTPUT.PUT_LINE('Numele departamentului: ' || i.nume);
        SELECT COUNT(last_name) INTO cnt
        FROM employees e, departments d
        WHERE e.department_id = d.department_id and d.department_id = i.id;
        IF cnt = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Nu exista angajati in departamentul ' || i.nume);
        ELSE
            zile.extend(); zile(zile.FIRST) := 'LUNI';
            zile.extend(); zile(zile.LAST) := 'MARTI';
            zile.extend(); zile(zile.LAST) := 'MIERCURI';
            zile.extend(); zile(zile.LAST) := 'JOI';
            zile.extend(); zile(zile.LAST) := 'VINERI';
            zile.extend(); zile(zile.LAST) := 'SAMBATA';
            zile.extend(); zile(zile.LAST) := 'DUMINICA';
            maxim := 0;
            FOR j IN zile.FIRST..zile.LAST LOOP
                SELECT COUNT(*) INTO cnt
                FROM emp_Robertto
                WHERE TO_CHAR(hire_date,'DY') LIKE zile(j) AND department_id = i.id;
                IF cnt = 0 THEN
                    DBMS_OUTPUT.PUT_LINE('Nu s-au facut angajari in ziua ' || zile(j));
                ELSE
                    IF cnt > maxim THEN
                        v_day := zile(j);
                        maxim := cnt;
                    END IF;
                END IF;
            END LOOP;
            DBMS_OUTPUT.PUT_LINE('Ziua in care au fost anagajate cele mai multe persoane: ' || v_day);
            
            SELECT last_name nume, salary salariu, ((SYSDATE - hire_date)/365) vechime
            bulk collect into listanum, listasal, listavec
            FROM employees
            WHERE TO_CHAR(hire_date,'DY') LIKE v_day AND department_id = i.id;
            
            DBMS_OUTPUT.PUT_LINE('Acestea sunt: '); 
            FOR j IN listanum.first..listanum.LAST LOOP
                DBMS_OUTPUT.PUT_LINE(listanum(j) || ' care castiga ' || listasal(j) || ' si este in firma de ' || listavec(j) || ' ani.');            
            END LOOP;
        END IF;
    END LOOP;
END f5_Robertto;
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(f5_Robertto);
END;
/

-- Ex 6
CREATE OR REPLACE PROCEDURE p6_Robertto
AS
    CURSOR c IS SELECT department_name nume, department_id id
                FROM departments;
    v_day VARCHAR(20);
    TYPE ln IS TABLE OF VARCHAR(50);
    TYPE ls IS TABLE OF NUMBER;
    TYPE lv IS TABLE OF NUMBER;
    TYPE d IS TABLE OF VARCHAR(20);
    zile d := d();
    listanum ln := ln();
    listasal ls := ls();
    listavec lv := lv();
    cnt PLS_INTEGER;
    crtvec PLS_INTEGER;
    maxim PLS_INTEGER;
BEGIN
    FOR i IN c LOOP
        DBMS_OUTPUT.PUT_LINE('Numele departamentului: ' || i.nume);
        SELECT COUNT(last_name) INTO cnt
        FROM employees e, departments d
        WHERE e.department_id = d.department_id and d.department_id = i.id;
        IF cnt = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Nu exista angajati in departamentul ' || i.nume);
        ELSE
            zile.extend(); zile(zile.FIRST) := 'LUNI';
            zile.extend(); zile(zile.LAST) := 'MARTI';
            zile.extend(); zile(zile.LAST) := 'MIERCURI';
            zile.extend(); zile(zile.LAST) := 'JOI';
            zile.extend(); zile(zile.LAST) := 'VINERI';
            zile.extend(); zile(zile.LAST) := 'SAMBATA';
            zile.extend(); zile(zile.LAST) := 'DUMINICA';
            maxim := 0;
            FOR j in zile.FIRST..zile.LAST LOOP
                SELECT COUNT(*) INTO cnt
                FROM emp_Robertto
                WHERE TO_CHAR(hire_date,'DY') LIKE zile(j) AND department_id = i.id;
                IF cnt = 0 THEN
                    DBMS_OUTPUT.PUT_LINE('Nu s-au facut angajari in ziua ' || zile(j));
                ELSE
                    IF cnt > maxim THEN
                        v_day := zile(j);
                        maxim := cnt;
                    END IF;
                END IF;
            END LOOP;
            DBMS_OUTPUT.PUT_LINE('Ziua in care au fost anagajate cele mai multe persoane: ' || v_day);
            SELECT last_name nume, salary salariu, ((SYSDATE - hire_date)/365) vechime BULK COLLECT INTO listanum, listasal, listavec
            FROM employees
            WHERE TO_CHAR(hire_date,'DY') LIKE v_day AND department_id = i.id
            ORDER BY (SYSDATE - hire_date) DESC;                                             
            DBMS_OUTPUT.PUT_LINE('Acestea sunt: ');
            cnt := 1;
            crtvec := listavec(listavec.FIRST);
            FOR j IN listanum.FIRST..listanum.LAST LOOP
                IF crtvec != listavec(j) THEN
                    cnt := cnt + 1;
                    crtvec := listavec(j);
                END IF;
                DBMS_OUTPUT.PUT_LINE(cnt || '. ' || listanum(j) || ' care castiga ' || listasal(j) || ' si este in firma de ' || listavec(j) || ' ani.');
            END LOOP;
        END IF;
    END LOOP;
END p6_Robertto;
/
BEGIN
    p6_Robertto;
END;
/

-- Laborator 5 PL/SQL
-- Ex 1
CREATE OR REPLACE PACKAGE pachetAng_Robertto AS
-- 1.a
PROCEDURE add_ang(v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE,
                    v_email emp_Robertto.email%TYPE,
                    v_phone_number emp_Robertto.phone_number%TYPE:=NULL,
                    v_job_name jobs.job_title%TYPE,
                    v_department_name departments.department_name%TYPE,
                    v_manfirstname emp_Robertto.last_name%TYPE,
                    v_manlastname emp_Robertto.last_name%TYPE);              

FUNCTION f_idjob(v_job_name jobs.job_title%TYPE)
RETURN VARCHAR;
FUNCTION f_iddep(v_department_name departments.department_name%TYPE)
RETURN NUMBER;
FUNCTION f_minsal(v_job_id jobs.job_id%TYPE,
                    v_department_id departments.department_id%TYPE)
RETURN NUMBER;
FUNCTION f_idman(v_manfirstname emp_Robertto.last_name%TYPE,
                    v_manlastname emp_Robertto.last_name%TYPE)
RETURN NUMBER;

-- 1.b
PROCEDURE move_ang(v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE,
                    v_department_name departments.department_name%TYPE,
                    v_job_name jobs.job_title%TYPE,
                    v_manfirstname emp_Robertto.last_name%TYPE,
                    v_manlastname emp_Robertto.last_name%TYPE);
-- 1.c
FUNCTION f_subalterni(v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE)
RETURN NUMBER;


-- 1.d
PROCEDURE p_raise(v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE);
                    
-- 1.e
PROCEDURE p_sal(v_salary emp_Robertto.salary%TYPE,
                    v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE);
-- 1.f
CURSOR c_emp(id NUMBER) RETURN employees%ROWTYPE;

-- 1.g
CURSOR c_jobs RETURN jobs%ROWTYPE;

-- 1.h
PROCEDURE p_jobs;
END pachetang_Robertto;
/

CREATE SEQUENCE seq_pachetang_Robertto
    START WITH 107
    INCREMENT BY 1;

CREATE OR REPLACE PACKAGE BODY pachetang_Robertto AS
-- 1.f
    CURSOR c_emp(id NUMBER) RETURN employees%ROWTYPE
            IS SELECT *
                FROM employees
                WHERE job_id = id;
-- 1.g
    CURSOR c_jobs RETURN jobs%ROWTYPE
        IS SELECT * FROM jobs;

-- 1.h
    PROCEDURE p_jobs AS
    BEGIN
        FOR job in pachetang_Robertto.c_jobs LOOP
            DBMS_OUTPUT.PUT_LINE('Pe job-ul' || job.job_title || 'lucreaza angajatii:');
            FOR emp in pachetang_Robertto.c_emp(job.job_id) LOOP
                DBMS_OUTPUT.PUT_LINE(emp.first_name || ' ' || emp.last_name);
            END LOOP;
        END LOOP;
    END p_jobs;
        
-- 1.a functii
    FUNCTION f_idjob(v_job_name jobs.job_title%TYPE)
    RETURN VARCHAR IS
        v_job_id emp_Robertto.job_id%TYPE;
    BEGIN
        SELECT job_id INTO v_job_id
        FROM jobs
        WHERE job_title = v_job_name;
        
        RETURN v_job_id;
    END f_idjob;
    
    FUNCTION f_iddep(v_department_name departments.department_name%TYPE)
    RETURN NUMBER IS
        v_department_id emp_Robertto.department_id%TYPE;
    BEGIN
        SELECT department_id INTO v_department_id
        FROM departments
        WHERE department_name = v_department_name;
        
        RETURN v_department_id;
    END f_iddep;
    
    FUNCTION f_minsal(v_job_id jobs.job_id%TYPE,
                    v_department_id departments.department_id%TYPE)
    RETURN NUMBER IS
    v_minsal employees.salary%TYPE;
    BEGIN
        SELECT MIN(salary) INTO v_minsal
        FROM employees
        WHERE v_department_id = department_id AND v_job_id = job_id
        GROUP BY job_id;
        
        RETURN v_minsal;
    END f_minsal;
    FUNCTION f_idman(v_manfirstname emp_Robertto.last_name%TYPE,
                    v_manlastname emp_Robertto.last_name%TYPE)
    RETURN NUMBER IS
    v_manager_id employees.employee_id%TYPE;
    BEGIN
        SELECT employee_id INTO v_manager_id
        FROM employees
        WHERE last_name = v_manlastname and first_name = v_manfirstname;
        RETURN v_manager_id;
    END f_idman;
    
-- 1.a procedura
    PROCEDURE add_ang (v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE,
                    v_email emp_Robertto.email%TYPE,
                    v_phone_number emp_Robertto.phone_number%TYPE:=NULL,
                    v_job_name jobs.job_title%TYPE,
                    v_department_name departments.department_name%TYPE,
                    v_manfirstname emp_Robertto.last_name%TYPE,
                    v_manlastname emp_Robertto.last_name%TYPE) IS
        v_hire_date emp_Robertto.hire_date%TYPE;
        v_job_id emp_Robertto.job_id%TYPE;
        v_department_id emp_Robertto.department_id%TYPE;
        v_salary emp_Robertto.salary%TYPE;
        v_manager_id emp_Robertto.manager_id%TYPE;
        v_commission_pct emp_Robertto.commission_pct%TYPE := 0;
    BEGIN
        --don't forget employee id
        v_hire_date :=SYSDATE;
        v_job_id := f_idjob(v_job_name);
        v_department_id := f_iddep(v_department_name);
        v_salary := f_minsal(v_job_id, v_department_id);
        v_manager_id := f_idman(v_manfirstname, v_manlastname);
        INSERT INTO emp_Robertto
        VALUES(seq_pachetang_Robertto.nextval, v_first_name, v_last_name, v_email, v_phone_number,
                v_hire_date, v_job_id, v_salary, v_commission_pct, v_manager_id, v_department_id);
    END add_ang;
    
-- 1.b
    PROCEDURE move_ang(v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE,
                    v_department_name departments.department_name%TYPE,
                    v_job_name jobs.job_title%TYPE,
                    v_manfirstname emp_Robertto.last_name%TYPE,
                    v_manlastname emp_Robertto.last_name%TYPE) IS
    v_hire_date emp_Robertto.hire_date%TYPE := SYSDATE;
    v_job_id emp_Robertto.job_id%TYPE;
    v_department_id emp_Robertto.department_id%TYPE;
    v_salary emp_Robertto.salary%TYPE;
    v_manager_id emp_Robertto.manager_id%TYPE;
    v_commission_pct emp_Robertto.commission_pct%TYPE;
    v_old_salary emp_Robertto.salary%TYPE;
    BEGIN
        v_job_id := f_idjob(v_job_name);
        v_department_id := f_iddep(v_department_name);
        v_salary := f_minsal(v_job_id, v_department_id);
        SELECT salary INTO v_old_salary
        FROM employees
        WHERE last_name = v_last_name and first_name = v_first_name;
        IF v_salary < v_old_salary THEN
            v_salary := v_old_salary;
        END IF;
        v_manager_id := f_idman(v_manfirstname, v_manlastname);
        SELECT MIN(commission_pct) INTO v_commission_pct
        FROM employees
        WHERE v_department_id = department_id AND v_job_id = job_id
        GROUP BY job_id;
        UPDATE emp_Robertto
        SET hire_date = v_hire_date, department_id = v_department_id, job_id = v_job_id,
            manager_id = v_manager_id, salary = v_salary, commission_pct = v_commission_pct
        WHERE last_name = v_last_name AND first_name = v_first_name;
    END move_ang;
    
-- 1.c
    FUNCTION f_subalterni(v_first_name emp_Robertto.first_name%TYPE,
                    v_last_name emp_Robertto.last_name%TYPE)
    RETURN NUMBER IS
    v_nr_subalterni NUMBER(20);
    v_employee_id employees.employee_id%TYPE;
    BEGIN
        SELECT employee_id INTO v_employee_id
        FROM employees
        WHERE last_name = v_last_name and first_name = v_first_name;
        
        SELECT COUNT(employee_id) INTO v_nr_subalterni
        FROM employees e
        WHERE manager_id = v_employee_id
        GROUP BY manager_id;
        RETURN v_nr_subalterni;
    END f_subalterni;

-- 1.d
    PROCEDURE p_raise(v_first_name emp_Robertto.first_name%TYPE,
                        v_last_name emp_Robertto.last_name%TYPE)
    IS
        v_manager_id employees.manager_id%TYPE;
        v_job_id emp_Robertto.job_id%TYPE;
        v_salary emp_Robertto.salary%TYPE;
        v_new_manager_id employees.manager_id%TYPE;
        v_old_salary emp_Robertto.salary%TYPE;
        v_commission_pct emp_Robertto.commission_pct%TYPE;
        v_department_id emp_Robertto.department_id%TYPE;
    BEGIN
        SELECT manager_id, department_id INTO v_manager_id, v_department_id
        FROM employees
        WHERE last_name = v_last_name AND first_name = v_first_name;
        
        SELECT job_id, manager_id INTO v_job_id, v_new_manager_id
        FROM employees
        WHERE employee_id = v_manager_id;
        
        SELECT MIN(commission_pct) INTO v_commission_pct
        FROM employees
        WHERE v_department_id = department_id AND v_job_id = job_id
        GROUP BY job_id;
        
        SELECT salary INTO v_old_salary
        FROM employees
        WHERE last_name = v_last_name and first_name = v_first_name;
        
        IF v_salary < v_old_salary THEN
            v_salary := v_old_salary;
        END IF;
        
        UPDATE emp_Robertto 
        SET hire_date = SYSDATE, job_id = v_job_id, manager_id = v_new_manager_id,
            salary = v_salary, commission_pct = v_commission_pct
        WHERE last_name = v_last_name AND first_name = v_first_name;
    END p_raise;

-- 1.e
    PROCEDURE p_sal(v_salary emp_Robertto.salary%TYPE,
                        v_first_name emp_Robertto.first_name%TYPE,
                        v_last_name emp_Robertto.last_name%TYPE)
    IS
        v_min_salary emp_Robertto.salary%TYPE;
        v_max_salary emp_Robertto.salary%TYPE;
    BEGIN
        SELECT min_salary, max_salary INTO v_min_salary, v_max_salary
        FROM employees e, jobs j
        WHERE e.last_name = v_last_name AND e.first_name = v_first_name AND e.job_id = j.job_id;
        IF v_salary <= v_max_salary AND v_salary >= v_min_salary THEN
            DBMS_OUTPUT.PUT_LINE('Salariul este valid!');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Salariul nu este valid!');
        END IF;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Nu exista niciun angajat cu numele dat!');
        WHEN TOO_MANY_ROWS THEN
        DBMS_OUTPUT.PUT_LINE('Exista mai multi angajati cu numele dat!');
        WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Alta eroare!');
    END p_sal;
END pachetAng_Robertto;
/