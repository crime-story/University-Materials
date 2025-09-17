--1
drop table emp_prof;
drop table dept_prof;
CREATE TABLE EMP_prof AS SELECT * FROM employees;
CREATE TABLE DEPT_prof AS SELECT * FROM departments;

--2
DESCRIBE employees;
describe emp_prof;

--3
select * from emp_prof;

--4
ALTER TABLE emp_prof 
ADD CONSTRAINT pk_emp_p PRIMARY KEY(employee_id); 
ALTER TABLE dept_prof 
ADD CONSTRAINT pk_dept_prof PRIMARY KEY(department_id);
ALTER TABLE emp_prof 
ADD CONSTRAINT fk_emp_dept_prof 
FOREIGN KEY(department_id) REFERENCES dept_prof(department_id);

--5
INSERT INTO DEPT_prof
VALUES (300, 'Programare');

INSERT INTO DEPT_prof (department_id, department_name)
VALUES (300, 'Programare');

select * from dept_prof;

INSERT INTO DEPT_prof (department_name, department_id)
VALUES (300, 'Programare');

INSERT INTO DEPT_prof (department_id, department_name, location_id)
VALUES (300, 'Programare', null);

INSERT INTO DEPT_prof (department_name, location_id)
VALUES ('Programare', null);

commit;

--6
insert into emp_prof
values(250, null, 'Nume1', 'nume1@gmail.com',null,sysdate,'IT_PROG',null, null,null,300);

select * from emp_Prof;
commit;

--7

insert into emp_prof(employee_id, last_name, email, hire_date, job_id, department_id)
values(251, 'Nume2', 'nume2@gmail.com', sysdate, 'IT_PROG', 300);

--8
INSERT INTO emp_prof (employee_id, last_name, email, hire_date,
job_id, salary, commission_pct)
VALUES (252, 'Nume252', 'nume252@emp.com',SYSDATE, 'SA_REP', 5000, NULL);

SELECT employee_id, last_name, email, hire_date, job_id, salary, commission_pct
FROM emp_prof
WHERE employee_id=252;

ROLLBACK;

INSERT INTO
(SELECT employee_id, last_name, email, hire_date, job_id, salary, commission_pct
FROM emp_prof)
VALUES (252, 'Nume252', 'nume252@emp.com',SYSDATE, 'SA_REP', 5000, NULL);

SELECT employee_id, last_name, email, hire_date, job_id, salary, commission_pct
FROM emp_prof
WHERE employee_id=252;
ROLLBACK;

INSERT INTO emp_prof (employee_id, last_name, email, hire_date,
job_id, salary, commission_pct)
VALUES ((select max(employee_id) +1 from emp_prof), 'Nume252', 'nume252@emp.com',SYSDATE, 'SA_REP', 5000, NULL);

commit;

--10
drop table emp1_prof;

CREATE TABLE emp1_prof AS SELECT * FROM employees WHERE 1=0; 
--DELETE FROM emp1_prof;
--necesar daca nu aveam clauza WHERE de mai sus 
INSERT INTO emp1_prof SELECT * FROM employees 
WHERE commission_pct > 0.25;
SELECT employee_id, last_name, salary, commission_pct 
FROM emp1_prof; 
ROLLBACK;

--11

insert into emp_prof
values(0, USER, USER, 'TOTAL', 'TOTAL', sysdate, 'TOTAL',
(select sum(salary) from emp_prof),
(select avg(commission_pct) from emp_prof),null, null);

select * from emp_prof;

commit;

--12
INSERT INTO emp_prof (employee_id, last_name, email, hire_date,
job_id, salary, commission_pct)
VALUES (253, 'Nume253', 'nume253@emp.com',SYSDATE, 'SA_REP', 5000, NULL);

INSERT INTO emp_prof (employee_id, last_name, email, hire_date,
job_id, salary, commission_pct)
VALUES (254, 'Nume254', 'nume254@emp.com',SYSDATE, 'SA_REP', 5000, NULL);

--13

create table emp2_prof as select * from employees where 1=0;
create table emp3_prof as select * from employees where 1=0;

insert all
when salary < 5000 then into emp1_prof
when salary between 5000  and 10000  then into emp2_prof
else
into emp3_prof
select * from employees;

SELECT * FROM emp1_prof;
SELECT * FROM emp2_prof;
SELECT * FROM emp3_prof;

DELETE FROM emp1_prof;
DELETE FROM emp2_prof;
DELETE FROM emp3_prof;

commit;

--14 tema
--15
select * from emp_prof;
update emp_prof
set salary = salary * 1.05;
rollback;

--16 tema
--17

select * from dept_prof;
select * from emp_Prof;
update dept_prof
set manager_id = (select employee_id from emp_prof 
where lower(last_name) = 'grant' and lower(first_name) = 'douglas')
where department_id = 20;

update emp_prof
set salary = salary + 1000
where lower(last_name) = 'grant' and lower(first_name) = 'douglas';

commit;

--18,19 tema

--20
delete from dept_prof;

select * from dept_prof;
select * from emp_prof;
delete from emp_prof where department_id  is not null;

rollback;

--21,22 tema

--23
 
 delete from emp_prof
 where employee_id = 0;

commit;

--24

INSERT INTO emp_prof (employee_id, last_name, email, hire_date,
job_id, salary, commission_pct)
VALUES (256, 'Nume256', 'nume256@emp.com',SYSDATE, 'SA_REP', 5000, NULL);

select * from emp_prof;

--25
savepoint p;

--26

delete from emp_prof;
select * from emp_prof;

--27
 rollback to p;
 --28
select * from emp_prof;
commit;









