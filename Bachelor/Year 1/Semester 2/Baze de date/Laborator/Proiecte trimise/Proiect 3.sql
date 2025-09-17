--TEMA 3
--Popescu Paullo Robertto Karloss Grupa 1311

--1
select s_id "COD", s_last || ' ' || s_first "STUDENT SAU CURS", 'Student' "TIP"
from student
--aici am crezut ca exista doar un singur profesor pe care il cheama BROWN
--de aceea n-am folosit in si am folosit =
where f_id = (select f_id
              from faculty 
              where upper(f_last) = 'BROWN') 
union
select course_no "COD", course_name "STUDENT SAU CURS", 'Curs'
from course
where course_no in (select course_no
                   from course_section
                   --la fel si aici
                   where f_id = (select f_id
                                from faculty 
                                where upper(f_last) = 'BROWN'));

--2
select s.s_id, s.s_last from student s, course c, enrollment e, course_section cs
where s.s_id = e.s_id 
and cs.c_sec_id = e.c_sec_id 
and cs.course_no = c.course_no 
and c.course_name = 'Database Management' 
and c.course_name not in (select course_name from course 
                        where course_name = 'Programming in C++');

--3
select distinct s.s_id as "COD", s.s_last as "NUME"
from student s
where (select count(1) 
        from enrollment 
        where s_id = s.s_id 
        and (grade = 'C') or grade = null) > 0;

--4 
select loc_id, bldg_code, capacity
from location
where capacity = (select max(capacity) from location);

--5
CREATE TABLE t (id NUMBER PRIMARY KEY);
INSERT INTO t VALUES(1);
INSERT INTO t VALUES(2);
INSERT INTO t VALUES(4);
INSERT INTO t VALUES(6);
INSERT INTO t VALUES(8);
INSERT INTO t VALUES(9);

select min(id)+1 as Minim, null as Maxim
from T
where id+1 not in (select * from t)
union 
select null as Minim, max(id)-1 as Maxim
from T
where id-1 not in (select * from t);

--6
select f.f_id "Cod profesor", f.f_last || ' ' || f.f_first "Nume Profesor",
decode(nvl(s.nr, 0), 0, 'Nu', 'Da (' || s.nr || ')') "Student",
decode(nvl(c.nr, 0), 0, 'Nu', 'Da (' || c.nr || ')') "Curs" 
from faculty f, (select
				 count(1) as nr, f_id
				 from student
				 group by f_id) s,
				(select
				 count(distinct course_no) nr, f_id
				 from course_section
				 group by f_id) c
where s.f_id (+) = f.f_id
and c.f_id (+) = f.f_id;

--7
select t1.term_desc, t2.term_desc
from term t1, term t2
where substr(t1.term_desc, 0, length(t1.term_desc)-1) = substr(t2.term_desc, 0, length(t2.term_desc)-1)
and t1.term_desc < t2.term_desc;

--8
with cursuri as (select s_id,s_last,s_first,course_no
from student s join enrollment e using(s_id) join course_section c1 using(c_sec_id)
order by s_id asc)
select distinct c1.s_id,c1.s_last,c1.s_first,c1.course_no,c2.course_no
from cursuri c1 join cursuri c2 on(c1.s_id = c2.s_id)
where (substr(c1.course_no, 5, 1) != substr(c2.course_no, 5, 1))
      and (c1.course_no != c2.course_no)
      and (c1.course_no < c2.course_no)
order by c1.s_id asc;

--9
select cs1.course_no, cs2.course_no 
from course_section cs1 
join course_section cs2 using (term_id)
where cs1.course_no > cs2.course_no;

--10
select c_sec_id "Cod", course_no "Numele cursului", term_desc "Denumirea semestrului", max_enrl "Nr. locuri"
from course_section join term using (term_id)
where max_enrl < (select min(max_enrl) 
                  from course_section 
                  where loc_id = 1);

--11
select distinct course_name, cs.max_enrl
from course_section cs join course using (course_no)
where cs.max_enrl = (select min(cs2.max_enrl) 
					from course_section cs2);

--12
select f_first, f_last, round(avg(max_enrl),2) "Numar mediu locuri" 
from faculty f, course_section c
where f.f_id = c.f_id 
group by f_first, f_last;

--13
select f_last, f_first, count(s_id) as "Nr studenti coordonati" 
from faculty f, student s
where f.f_id = s.f_id 
group by f_last, f_first 
having count(s_id) >= 3;

--14
select distinct c.course_name, l.capacity, l.loc_id
from course c 
join course_section cs on (c.course_no = cs.course_no) 
join location l on (cs.loc_id = l.loc_id)
where l.capacity = (select max(capacity)
                    from course_section join location using (loc_id)
                    group by course_no
                    having course_no = cs.course_no);

--15
select term_desc, round(avg(max_enrl),2) as "Media de locuri" 
from term t, course_section cs, course c
where t.term_id = cs.term_id 
and c.course_no = cs.course_no 
and term_desc like '%2007%'
group by term_desc;