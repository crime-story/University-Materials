-- Popescu Paullo Robertto Karloss Grupa 1311
-- PARTEA 1

-- 1. A)
-- 2. C)
-- 3. C)
-- 4. C)
-- 5. 22-MAR-21
-- 6. 3
-- 7. D)

-- PARTEA 2

--1
select * from customer_tbl
where cust_state in ('IN', 'OH', 'MI', 'IL') and (cust_name like 'A%' or cust_name like 'B%')
order by cust_name;

--2a
select prod_id as "Codul", prod_desc as "Descriere", cost as "Cost" from products_tbl
where cost between 1 and 12.50;

--2b
select prod_id as "Codul", prod_desc as "Descriere", cost as "Cost" from products_tbl
where cost < 1 or cost > 12.50;

--3
select first_name || '.' || last_name || '@ittech.com' as "email angajat"
from employee_tbl;

--4
select last_name || ', ' || first_name as Nume, substr(emp_id, 1, 3) ||
'-' || substr(emp_id, 4,2) || '-' || substr(emp_id, 6, 4) as Cod,
concat('(', substr(phone, 1, 3)) || ')'
||substr(phone, 4, 3) || '-' || substr(phone, 7,4) as Telefon
from employee_tbl;

--5
select emp_id, to_char(date_hire,'YYYY') as Anul_angajarii from employee_pay_tbl;

--6
select employee_pay_tbl.emp_id, first_name, last_name, salary, bonus from employee_tbl join employee_pay_tbl 
on employee_tbl.emp_id=employee_pay_tbl.emp_id;

--7
select cust_name, ord_num, ord_date from customer_tbl join orders_tbl on customer_tbl.cust_id=orders_tbl.cust_id 
where cust_state like 'I%';

--8
select ord_num, qty, first_name, last_name, city from employee_tbl join orders_tbl on employee_tbl.emp_id=orders_tbl.sales_rep;

--9
select ord_num, qty, first_name, last_name, city from employee_tbl left join orders_tbl on employee_tbl.emp_id=orders_tbl.sales_rep;

--10
select * from employee_tbl where middle_name is NULL;

--11
select employee_tbl.emp_id, last_name, first_name,  nvl(salary,0)+nvl(bonus,0) as "SALARIUL ANUAL"
from employee_tbl join employee_pay_tbl on employee_tbl.emp_id=employee_pay_tbl.emp_id; 

--12 VARIANTA  1
select last_name, first_name, position,
case 
    when position='MARKETING' then to_char(10/100*salary+salary)
    when position='SALESMAN' then to_char(15/100*salary+salary)
    -- daca vroiam sa nu afiseze null pentru 'Salariu modificat' puneam aici else 'SALARIUL NEMODIFICAT'
end as "Salariu modificat"
from employee_tbl join employee_pay_tbl on employee_tbl.emp_id=employee_pay_tbl.emp_id;
 
--12 VARIANTA  2
select last_name, first_name,DECODE(position,'MARKETING',10/100*salary+salary, 'SALESMAN',15/100*salary+salary)
as "Salariu Modificat" -- daca vroiam sa nu afiseze null pentru 'Salariu modificat' foloseam 
-- decode(primul decode, null, 'SALARIU NEMODIFICAT', decode)
from employee_tbl join employee_pay_tbl on employee_tbl.emp_id=employee_pay_tbl.emp_id;

--12 VARIANTA 3
UPDATE employee_pay_tbl SET salary = 10/100*salary+salary WHERE position='MARKETING';
UPDATE employee_pay_tbl SET salary = 15/100*salary+salary WHERE position='SALESMAN';

select last_name, first_name,  salary, position from employee_tbl join employee_pay_tbl on employee_tbl.emp_id=employee_pay_tbl.emp_id;
