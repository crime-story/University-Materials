-- TEMA 2 continuare
-- Popescu Paullo Robertto Karloss Grupa 2311

--10
select m.first_name as "Nume", m.last_name as "Prenume", c.copy_id as "Cod exemplar", t.title as "Titlu", count(t.title_id) as "Nr. Imprumuturi"
from member m, title_copy c, title t, rental r
where m.member_id = r.member_id
and r.title_id = c.title_id
and c.copy_id = r.copy_id
and t.title_id = c.title_id
group by m.first_name, m.last_name, c.copy_id, t.title
order by 1,2;

--12 a.
select Data, (select count(*) 
                from rental 
                where extract(month from Data) = extract(month from book_date)
                and extract(day from Data) = extract(day from book_date))  as "Imprumuturi"
from(select (last_day(add_months(sysdate, -1)) + rownum) as Data from dual connect by ROWNUM <= 2)
order by 1;

--12 b.
select book_date, count(*) as "Imprumuturi"
from rental 
where extract(month from sysdate) = extract(month from book_date)
group by book_date 
order by 1;

--12 c.
select Data, (select count(*) 
                from rental 
                where extract(day from Data) = extract(day from book_date)
                and extract(month from book_date) = extract(month from Data)) as "Imprumuturi"
from(select (last_day(sysdate) - rownum) as Data from dual connect by rownum < extract(day from last_day(sysdate)))
order by 1;