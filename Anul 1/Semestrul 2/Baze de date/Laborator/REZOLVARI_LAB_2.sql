--1
select concat(first_name, concat(' ', last_name)) || ' castiga ' ||  salary 
||' lunar dar doreste ' || 3*salary || '.' as "Salariu ideal"
from employees;

--2

select initcap(first_name), upper(last_name), length(last_name) as Lungime
from employees
where last_name like 'J%' OR last_name like 'M%' 
or last_name like '__A%'
order by 3 desc;

select initcap(first_name), upper(last_name), length(last_name) as Lungime
from employees
where substr(last_name,1,1) in ('J', 'M')
or substr(last_name,3,1) = 'A'
order by 3 desc;

--3
select employee_id, last_name
from employees
where trim(upper(first_name)) = 'STEVEN'; 

--4
select initcap(first_name), upper(last_name), length(last_name) as Lungime,
instr(upper(last_name), 'A') as "Prima aparitie"
from employees
where lower(last_name) like '%e';

--5
select *
from employees
where mod(floor(sysdate - hire_date),7) =0;

--6
select employee_id, last_name, salary, 
round(salary + 15*salary/100,2) as "Salariu nou",
round((salary + 15*salary/100)/100,2) as "Numar sute"
from employees
where mod(salary, 1000) != 0;

--7
select last_name as "Nume angajat", hire_date as "Data angajarii",
rpad(hire_date, 20) as "Data angajarii cu padding"
from employees
where commission_pct is not null;

--8
select  to_Char(sysdate + 30, 'dd-mm-yyyy hh-mi-ss') 
from dual;

--9
select ceil(to_date('31-12-' || to_char(sysdate, 'yyyy'),
'dd-mm-yyyy') - sysdate) 
from dual;

--10
select to_char(sysdate +1/2, 'dd-mm-yyyy hh24:mi')
from dual;

select to_char(sysdate +(1/24/60)*5, 'dd-mm-yyyy hh24:mi')
from dual;

--11
select first_name || last_name , hire_date,
next_day(add_months(hire_date,6), 'Monday') as Negociere
from employees;

--12
select last_name, ceil(months_between(sysdate, hire_date)) as "Luni lucrate"
from employees
order by 2;

--13
select last_name, hire_date, to_char(hire_date, 'DAY') as Zi
from employees
order by to_char(hire_date, 'D');

alter session set nls_territory = 'Romania';


--14
select last_name, nvl(to_char(commission_pct), 'Fara comision') as Comision
from employees;

--15

select last_name, commission_pct, salary
from employees
where salary + (nvl(commission_pct, 0)*salary) > 10000;

--16

select last_name, job_id, salary, 
decode(job_id, 
        'IT_PROG', salary *1.2,
        'SA_REP', salary * 1.25,
        'SA_MAN', salary * 1.35,
        salary) as "Salariu renegociat"
from employees;


select last_name, job_id, salary, 
case job_id 
        when 'IT_PROG' then salary *1.2
        when 'SA_REP' then salary * 1.25
        when 'SA_MAN' then salary * 1.35
        else salary
        end as "Salariu renegociat"
from employees;

--17
select * from employees;

--17
select e.last_name, e.department_id, d.department_name
from employees e, departments d
where e.department_id = d.department_id;

--18
select distinct e.job_id, job_title
from jobs j , employees e 
where e.Job_id = j.job_id and
department_id = 30;

--19

select last_name, department_name, city
from employees e, departments d, locations l
where e.department_id = d.department_id and
d.location_id = l.location_id and
commission_pct is not null;

--20

select last_name, department_name
from employees e, departments d
where e.department_id = d.department_id and
lower(last_name) like '%a%';

--21

select last_name, department_name, job_title
from employees e, departments d, locations l, jobs j
where e.department_id = d.department_id and
d.location_id = l.location_id and
e.job_id = j.job_id and
city = 'Oxford';

--22
select e.last_name as Angajat, e.employee_id Ang# ,
m.last_name Manager , m.employee_id Mgr#
from employees e, employees m 
where e.manager_id = m.employee_id;

--23

select e.last_name as Angajat, e.employee_id Ang# ,
m.last_name Manager , m.employee_id Mgr#
from employees e, employees m 
where e.manager_id = m.employee_id(+);

--24
select * from employees;
select e1.last_name as Nume, e1.department_id as Departament,
e2.last_name as Colegi
from employees e1, employees e2
where e1.department_id(+) = e2.department_id and
e1.employee_id != e2.employee_id
order by 2, 1;

--25
select e.last_name, e.job_id, j.job_title, d.department_name, e.salary
from employees e, departments d , jobs j
where e.department_id = d.department_id(+) and
e.job_id = j.job_id;

--26
select e1.last_name, e1.hire_date
from employees e1, employees e2
where lower(e2.last_name) = 'gates' and 
e1.hire_date > e2.hire_date; 


--27
select e.last_name as Angajat, e.employee_id Ang# , e.hire_date as Data_ang,
m.last_name Manager , m.employee_id Mgr#, m.hire_date as Data_mgr
from employees e, employees m 
where e.manager_id = m.employee_id
and e.hire_date < m.hire_date;



