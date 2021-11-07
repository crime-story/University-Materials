describe employees;
describe job_history;

select * from employees;

select * from departments;

select * from jobs;

--5
select employee_id, first_name, last_name, hire_date, job_id from employees;

--6
select employee_id as cod, first_name as prenume, last_name as nume, hire_date as "data angajarii", job_id as "cod job" from employees;

--7
select job_id from employees;

select distinct(job_id) from employees;

--8
select last_name || ', ' || job_id as "Angajat si titlu" from employees;

--9
select employee_id || ', ' || first_name || ', ' || last_name
|| ', ' || hire_date || ', ' || job_id as "Informatii complete"
from employees;

--10
select e.last_name, e.salary
from employees 
where e.salary > 2850;

--11
select last_name, department_id
from employees
where employee_id = 104;

--12
select last_name, salary
from employees
where salary not between 1500 and 2850;

--13
select last_name, job_id, hire_date 
from employees
where hire_date between '20-feb-1987' and '1-may-1989'
order by hire_date asc;

--14
select last_name, department_id
from employees
where department_id in (10,30,50)
order by last_name asc;

--15
select last_name as angajat, department_id, salary as "Salariu lunar"
from employees
where department_id in (10,30,50)
and salary > 1500;

--16

select to_char(sysdate,'dd~~mm~~yyyy hh24-mi-ss')
from dual;

--17
select last_name, hire_date
from employees 
where hire_date like ('%87%');

select last_name, hire_date
from employees 
where to_char(hire_date,'YYYY') = 1987;


select last_name, hire_date
from employees 
where extract(YEAR from hire_date ) = 1987;

--18
select last_name, first_name, hire_date
from employees
where to_char(hire_date, 'DD') = to_char(sysdate, 'DD');

--19
select last_name, job_id
from employees
where manager_id is not null;

--20
select last_name, salary, commission_pct
from employees
where commission_pct is not null 
order by salary desc,commission_pct desc;

--21
select last_name, salary, commission_pct
from employees
order by salary desc,commission_pct desc;

--22
select last_name
from employees
where last_name like '__a%';
