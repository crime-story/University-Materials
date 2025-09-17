--38
create sequence  SEQ_DEPT_Prof
start with 400
increment by 10
maxvalue 10000
nocycle
NOCACHE;

--39
select * from USER_SEQUENCES;

--40
select * from angajati_prof;
create sequence  SEQ_angajati_prof
start with 255
increment by 1;

drop sequence SEQ_angajati_prof;

--41
--tema
select * from emp_prof;

--42

insert into emp_prof(employee_id, last_name, job_id, email, hire_date) 
values(SEQ_angajati_prof.nextval, 'NumeSeq', 'IT_PROG', 'NumeSEQ@gmail.com', sysdate);

insert into dept_prof(department_id, department_name) values
(SEQ_DEPT_Prof.nextval, 'Dept SEQ');

select * from dept_prof;

--43
SELECT SEQ_angajati_prof.currval 
FROM dual;

--44
drop sequence SEQ_DEPT_Prof;

-- -------------------------------------------------------------------------

--1
SELECT last_name, hire_date
FROM employees
WHERE hire_date > (SELECT hire_date
                    FROM employees
                    WHERE INITCAP(last_name)='Gates');

--2
SELECT last_name, salary
FROM employees
WHERE department_id = (SELECT department_id
                        FROM employees
                        WHERE LOWER(last_name)='gates')
AND LOWER(last_name) <> 'gates';

--3
select last_name, salary
from employees
where manager_id = (select employee_id
                    from employees
                    where manager_id is null);
                    
--4
SELECT last_name, department_id, salary
FROM employees 
WHERE (department_id, salary) IN ( SELECT department_id, salary 
                                    FROM employees 
                                    WHERE commission_pct IS NOT NULL);
--5
SELECT e.employee_id,e.last_name,e.salary, e.department_id 
FROM employees e 
WHERE e.salary > ( SELECT (j.min_salary+j.max_salary)/2 
                    FROM jobs j 
                    WHERE j.job_id=e.job_id )
AND e.job_id IN ( SELECT job_id 
                    FROM employees m 
                    WHERE e.department_id=m.department_id 
                    AND LOWER(m.last_name) LIKE '%t%' );
                    

SELECT e.employee_id,e.last_name,e.salary, e.department_id
FROM employees e 
WHERE e.salary > ( SELECT (j.min_salary+j.max_salary)/2 
                    FROM jobs j 
                    WHERE j.job_id=e.job_id )
and e.department_id in (select m.department_id
                        from employees m
                        WHERE e.department_id=m.department_id 
                        and LOWER(m.last_name) LIKE '%t%')
and  e.department_id is not null;
 
 
 SELECT e.employee_id,e.last_name,e.salary, e.department_id
FROM employees e 
WHERE e.salary > ( SELECT (j.min_salary+j.max_salary)/2 
                    FROM jobs j 
                    WHERE j.job_id=e.job_id );
                    
select  distinct m.department_id, m.job_id
from employees m
WHERE LOWER(m.last_name) LIKE '%t%';
 
 select job_id
 from employees
 where employee_id in (120,121,122);
    
SELECT UNIQUE e1.employee_id, e1.last_name, e1.salary 
FROM employees e1 
JOIN employees e2 ON (e2.department_id = e1.department_id)
JOIN jobs j ON (j.job_id = e1.job_id) 
WHERE LOWER(e2.last_name) LIKE '%t%' 
AND e1.salary > (j.min_salary + j.max_salary) / 2 ORDER BY e1.last_name;
--6
SELECT * 
FROM employees e 
WHERE salary > ANY ( SELECT salary 
                        FROM employees WHERE
                        job_id LIKE '%CLERK');

--7
select e.last_name, d.department_name, e.salary
from employees e, departments d
where e.department_id = d.department_id
and e.commission_pct is null
and e.manager_id in (select m.employee_id
                    from employees m
                    where m.employee_id = e.manager_id
                    and m.commission_pct is not null);
--8
select last_name, department_id, salary, job_id
from employees 
where (salary, nvl(commission_pct, -1))
in(select salary, nvl(commission_pct, -1)
    from employees t, departments d, locations l
    where t.department_id = d.department_id
    and d.location_id = l.location_id
    and lower(city) = 'oxford');
    
--9
--tema

--11
select max(salary) as "Maxim", min(salary) as "Minim", sum(salary) as "Suma",
round(avg(salary)) as "Media" 
from employees;

--12
select job_id, max(salary) as "Maxim", min(salary) as "Minim", sum(salary) as "Suma",
round(avg(salary)) as "Media" 
from employees
group by job_id;

--13
select job_id, count(employee_id)
from employees
group by job_id;

--14
select count(distinct manager_id) as "Nr manageri"
from employees;

--15
select department_id, nvl(max(salary) - min(salary),0) as "Diferenta"
from employees
right join departments using (department_id) 
group by department_id;

--16
-- tema

--17
select employee_id, last_name
from employees
where salary > (select avg(salary)
                from employees)
order by salary desc;

--18
select m.manager_id, min(e.salary)
from employees e, employees m
where e.manager_id = m.employee_id
and e.manager_id is not null
group by m.manager_id
having min(e.salary) > 1000
order by min(e.salary) desc;

select manager_id, min(salary)
from employees
where manager_id is not null
group by manager_id
having min(salary) > 5000
order by min(salary) desc;

select manager_id, employee_id, salary
from employees
where manager_id = 100
order by 3 asc;

--19

select d.department_name, e.department_id, max(e.salary)
from employees e
join departments d on (e.department_id = d.department_id)
group by d.department_name, e.department_id
having max(e.salary) > 3000;


--20

select min((select avg(salary)
            from employees e
            where e.job_id = j.job_id))
from jobs j;

--21

select d.department_name, d.department_id, nvl(sum(e.salary), 0)
from employees e
right join departments d on (e.department_id = d.department_id)
group by d.department_name, d.department_id;

--22
select max(avg(salary))
from employees
where department_id is not null
GROUP BY department_id;

--23

select job_id, job_title, avg(salary)
from employees
join jobs using(job_Id)
group by job_id, job_title
having avg(salary) = (select min(avg(salary))
                        from employees
                        group by job_id
                        );
--24
select avg(salary)
from employees
having avg(salary) > 2500;








