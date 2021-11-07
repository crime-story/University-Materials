
select * 
from employees e , departments d
where e.department_id(+) = d.department_id;

--1
select e1.last_name, to_char(e1.hire_Date, 'MONTH') as Luna,
extract(YEAR FROM  e1.hire_Date) as An
from employees e1, employees e2
where e1.department_id = e2.department_id and 
lower(e2.last_name) = 'gates' and
lower(e1.last_name) != 'gates' and
lower(e1.last_name)  like '%a%';

SELECT e1.last_name, 
TO_CHAR(e1.hire_date, 'MONTH') AS "Luna",
EXTRACT(YEAR FROM e1.hire_date) as "An"
FROM employees e1 
JOIN employees e2 
ON (e1.department_id = e2.department_id)
WHERE LOWER(e1.last_name) LIKE '%a%'
AND LOWER(e1.last_name) != 'gates' 
AND LOWER(e2.last_name) = 'gates';

SELECT e1.last_name, 
TO_CHAR(e1.hire_date, 'MONTH') AS "Luna",
EXTRACT(YEAR FROM e1.hire_date) as "An"
FROM employees e1 
JOIN employees e2 
ON (e1.department_id = e2.department_id)
WHERE INSTR(LOWER(e1.last_name), 'a') != 0
AND LOWER(e1.last_name) != 'gates' 
AND LOWER(e2.last_name) = 'gates';

--2
SELECT DISTINCT e1.employee_id, 
e1.last_name,
e1.department_id, 
d.department_name
FROM employees e1, employees e2, departments d 
WHERE e1.department_id = e2.department_id
AND e1.department_id = d.department_id 
AND LOWER(e2.last_name) LIKE '%t%' 
ORDER BY e1.last_name;

SELECT UNIQUE e1.employee_id, 
e1.last_name, 
e1.department_id,
d.department_name
FROM employees e1
JOIN employees e2 
ON (e1.department_id = e2.department_id)
JOIN departments d
ON (e1.department_id = d.department_id)
WHERE LOWER(e2.last_name) LIKE '%t%' 
ORDER BY e1.last_name;

--3
SELECT e.last_name, e.salary,
j.job_title, l.city, c.country_name
FROM employees e 
JOIN employees m on (e.manager_id = m.employee_id)
JOIN jobs j on (e.job_id = j.job_id)
JOIN departments d on (e.department_id = d.department_id)
JOIN locations l ON (l.location_id = d.location_id)
JOIN countries c ON (c.country_id = l.country_id)
WHERE m.last_name = 'Zlotkey';

SELECT e.last_name, e.salary, j.job_title, l.city, c.country_name
FROM employees e 
JOIN employees m on (e.manager_id = m.employee_id) 
JOIN jobs j on (e.job_id = j.job_id)
LEFT JOIN departments d on (e.department_id = d.department_id)
LEFT JOIN locations l ON (l.location_id = d.location_id)
LEFT JOIN countries c ON (c.country_id = l.country_id) 
WHERE m.last_name = 'Zlotkey';

--4
SELECT d.department_id, d.department_name, e.last_name, 
j.job_title, TO_CHAR(e.salary, '$99,999.00') Salariu
FROM departments d
JOIN employees e ON (e.department_id = d.department_id)
JOIN jobs j ON (e.job_id = j.job_id) 
WHERE LOWER(d.department_name) LIKE '%ti%'
ORDER BY d.department_name, e.last_name;

--5
SELECT e.last_name, department_id, department_name,
city , job_title 
FROM employees e 
JOIN departments d USING(department_id) 
JOIN locations l USING(location_id) 
JOIN jobs j USING(job_id) 
WHERE LOWER(l.city) = 'oxford';

--6
SELECT UNIQUE e1.employee_id, e1.last_name, e1.salary 
FROM employees e1
JOIN employees e2 ON (e2.department_id = e1.department_id)
JOIN jobs j ON (j.job_id = e1.job_id) 
WHERE LOWER(e2.last_name) LIKE '%t%'
--INSTR(LOWER(e2.last_name), 't') != 0)
AND e1.salary > (j.min_salary + j.max_salary) / 2
ORDER BY e1.last_name;

--7
SELECT e.last_name, d.department_name 
FROM employees e, departments d 
WHERE e.department_id = d.department_id (+);

SELECT e.last_name, d.department_name 
FROM employees e 
LEFT OUTER JOIN departments d 
ON (e.department_id = d.department_id);

--8
SELECT e.last_name, d.department_name 
FROM employees e 
RIGHT OUTER JOIN departments d 
ON (e.department_id = d.department_id);

--9
SELECT e.last_name, d.department_name 
FROM employees e, departments d 
WHERE e.department_id = d.department_id (+) 
UNION 
SELECT e.last_name, d.department_name 
FROM employees e, departments d 
WHERE e.department_id (+)= d.department_id;

SELECT e.last_name, d.department_name 
FROM employees e 
FULL OUTER JOIN departments d 
ON (e.department_id = d.department_id);


SELECT e.employee_id, e.last_name, d.department_name
FROM employees e, departments d 
WHERE e.department_id = d.department_id (+) 
UNION
SELECT e.employee_id, e.last_name, d.department_name 
FROM employees e, departments d 
WHERE e.department_id (+)= d.department_id;

--10
select * from employees;
SELECT D.DEPARTMENT_ID 
FROM DEPARTMENTS D
WHERE LOWER(D.DEPARTMENT_NAME) LIKE '%re%'
UNION ALL
SELECT DEPARTMENT_ID 
FROM EMPLOYEES E 
WHERE E.JOB_ID='SA_REP';

--11
SELECT d.department_id 
FROM departments d 
MINUS 
SELECT UNIQUE department_id 
FROM employees;

SELECT department_id 
FROM departments
WHERE department_id NOT IN ( SELECT d.department_id 
                            FROM departments d 
                            JOIN employees e 
                            ON(d.department_id=e.department_id));
                            
                            
 --12
SELECT d.department_id 
FROM departments d 
WHERE LOWER(d.department_name) LIKE '%re%' 
INTERSECT 
SELECT e.department_id 
FROM employees e 
WHERE e.job_id = 'HR_REP';     

--13
SELECT e.employee_id, e.job_id, e.last_name
FROM employees e 
WHERE (e.salary > 3000)
UNION 
SELECT e.employee_id, e.job_id, e.last_name
FROM employees e 
JOIN jobs j ON (j.job_id = e.job_id) 
WHERE e.salary = (j.min_salary + j.max_salary) / 2;

SELECT e.employee_id, e.job_id, e.last_name 
FROM employees e 
JOIN jobs j ON (e.job_id = j.job_id)
WHERE e.salary > 3000 OR 
e.salary = (j.min_salary + j.max_salary) / 2;

--14

select distinct 'Departamentul ' || d.department_name 
|| ' este condus de ' ||
nvl(to_char(d.manager_id), 'nimeni') || ' si ' ||
nvl2(e.employee_id, 'are salariati' ,'nu are salariati')
from employees e
right outer join departments d on (e.department_id = d.department_id);

select distinct 'Departamentul ' || d.department_name 
|| ' este condus de ' ||
nvl(to_char(d.manager_id), 'nimeni') || ' si ' ||
case nvl(e.employee_id, -1)
when -1 then 'nu are salariati'
else 'are salariati'
end
from employees e
right outer join departments d 
on (e.department_id = d.department_id);

--15
select first_name, last_name, length(last_name) Lungime
from employees
where nullif(length(last_name), length(first_name)) is not null;