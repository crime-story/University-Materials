-- TEMA 1
-- Popescu Paullo Robertto Karloss Grupa 2311
--23
SET PAGESIZE 0
SET FEEDBACK OFF;

spool c:\users\rober\desktop\c++\inserare_departments.sql

select 'insert into departments (department_id, department_name, manager_id, location_id)
values(' || department_id || ', ' || department_name || ', ' || manager_id || ', '|| location_id || ');'
from departments;
SPOOL OFF;
SET FEEDBACK ON;