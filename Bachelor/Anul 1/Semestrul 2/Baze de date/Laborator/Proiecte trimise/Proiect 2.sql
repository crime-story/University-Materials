-- Popescu Paullo Robertto Karloss Grupa 1311 #TEMA nr.2
-- PARTEA 1

--1
select count(employee_id) from employees 
where upper(first_name) like 'K%';
--Deci sunt 7 angajati al caror nume incepe cu K

--2
select employee_id, first_name, last_name from employees
where salary = (select min(salary) from employees);

select * from employees order by salary asc;
select min(salary) from employees;

--3
--varianta 1 fara subcerere
select distinct m.employee_id, m.last_name
FROM employees e, employees m
WHERE e.manager_id = m.employee_id
and e.department_id = 30
order by m.last_name;


--varianta 2 cu subcerere
select distinct employee_id, last_name from employees
where employee_id in (select manager_id 
                        from employees 
                        where department_id = 30)
order by last_name;
                        

--4
select e.employee_id, e.last_name, e.first_name, (select count(f.employee_id) 
                                            from employees f
                                            where f.manager_id=e.employee_id) as SUBALTERNI 
from employees e;


--5
select e.employee_id, e.last_name, e.first_name 
from employees e
where e.last_name in (select f.last_name
                     from employees f
                     where e.last_name=f.last_name and e.employee_id!=f.employee_id);

--6
select d.department_id, d.department_name
from departments d
where 2<=(select count(distinct e.job_id)
        from employees e
        where d.department_id=e.department_id);
        
-- PARTEA 2


--7
select qty, prod_desc from orders_tbl, products_tbl 
where orders_tbl.prod_id = products_tbl.prod_id 
and upper(prod_desc) like '%PLASTIC%';

--8
select last_name, 'angajat' as "FUNCTIE" from employee_tbl
union select cust_name, 'client' as "FUNCTIE" from customer_tbl;

--9
select distinct prod_desc from employee_tbl, orders_tbl, products_tbl
where employee_tbl.emp_id=orders_tbl.sales_rep and products_tbl.prod_id=orders_tbl.prod_id
and prod_desc in (select prod_desc from employee_tbl, orders_tbl, products_tbl
where employee_tbl.emp_id=orders_tbl.sales_rep and products_tbl.prod_id=orders_tbl.prod_id)
and instr(substr(prod_desc, instr(prod_desc, ' ')+1),' ')!=0 
and substr(prod_desc, instr(prod_desc, ' ')+1,1)='P';

--10
select cust_name from CUSTOMER_TBL, ORDERS_TBL 
where CUSTOMER_TBL.cust_id = ORDERS_TBL.cust_id 
and to_char(ord_date,'dd')=17;

--11
select max(salary), last_name, first_name, max(bonus) 
from employee_tbl, employee_pay_tbl 
where employee_tbl.emp_id=employee_pay_tbl.emp_id
group by last_name, first_name, bonus, employee_tbl.emp_id
HAVING (max(salary)<32000 and max(bonus*17)<32000);

--12
select last_name, first_name, sum(qty) from employee_tbl, orders_tbl where employee_tbl.emp_id=orders_tbl.sales_rep
group by last_name, first_name having sum(qty)>50;

--13

select last_name, first_name,salary, date_last_raise from employee_tbl, employee_pay_tbl, orders_tbl 
where employee_pay_tbl.emp_id=employee_tbl.emp_id and employee_tbl.emp_id=orders_tbl.sales_rep 
group by last_name, first_name,salary, date_last_raise having count(sales_rep)>=2;

---14
select * from products_tbl
where cost > (select avg(cost) from products_tbl);

--15 
--varianta 1
select first_name, last_name, salary, bonus, 
(select sum(salary) from employee_pay_tbl) as "SALARIU TOTAL",
(select sum(bonus) from employee_pay_tbl) as "BONUS TOTAL"
from employee_pay_tbl, employee_tbl 
where employee_pay_tbl.emp_id=employee_tbl.emp_id;

--varianta 2
select first_name, last_name, salary, bonus, 
st "SALARIU TOTAL", bt "BONUS TOTAL"
from employee_pay_tbl, employee_tbl,
(select sum(salary) st, sum(bonus) bt from employee_pay_tbl) sums
where employee_pay_tbl.emp_id=employee_tbl.emp_id;

--16
select city, count(sales_rep) from employee_tbl, orders_tbl 
where employee_tbl.emp_id=orders_tbl.sales_rep 
group by city 
having count(sales_rep)=(select max(count(sales_rep)) from orders_tbl group by sales_rep);

--17
select emp_id, last_name, first_name, count(sales_rep),to_char(ord_date,'mm') 
from employee_tbl, orders_tbl where employee_tbl.emp_id=orders_tbl.sales_rep 
and (to_char(ord_date,'mm')=10 or to_char(ord_date,'mm')=9)
group by  emp_id, last_name, first_name,to_char(ord_date,'mm');

--18
select cust_address, customer_tbl.cust_id from customer_tbl left join orders_tbl 
on customer_tbl.cust_id=orders_tbl.cust_id 
where customer_tbl.cust_id not in (select cust_id from orders_tbl) and
substr(cust_address,1,1) in ('0', '1','2','3', '4','5','6', '7','8','9');

--19

select emp_id, last_name, first_name, city, customer_tbl.cust_id, cust_name, cust_city 
from customer_tbl, orders_tbl, employee_tbl
where customer_tbl.cust_id=orders_tbl.cust_id and orders_tbl.sales_rep=employee_tbl.emp_id
and city!=cust_city;

--20
select avg(NVL(salary, 0)) from employee_pay_tbl;

--21
--CORECT
SELECT CUST_ID, CUST_NAME
FROM CUSTOMER_TBL
WHERE CUST_ID =
(SELECT CUST_ID
FROM ORDERS_TBL
WHERE ORD_NUM = '16C17');

---GRESITA (NU EXISTA TABELA)
SELECT EMP_ID, SALARY
FROM EMPLOYEE_PAY_TBL
WHERE SALARY BETWEEN '20000'
AND (SELECT SALARY
FROM EMPLOYEE_ID
WHERE SALARY = '40000');

--22
select last_name, nvl(pay_rate,0) from employee_tbl, employee_pay_tbl
where employee_tbl.emp_id  = employee_pay_tbl.emp_id
and pay_rate>
all (select pay_rate from employee_pay_tbl where(last_name) like '%LL%')
and pay_rate!=0;