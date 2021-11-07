--TEMA 4
--Popescu Paullo Robertto Karloss Grupa 1311

--1
select concat(concat(s_first, ' '), s_last) as "Nume si Prenume"
from student s
where 'NULL' not in (select grade from enrollment where s_id=s.s_id);

--2
select bldg_code from location
where bldg_code not in (select bldg_code from location
                        where loc_id not in (select distinct loc_id from course_section));\

--3
select f.f_id, f.f_last 
from faculty f
where (select count(1) from student s 
        where s.f_id = f.f_id 
        and (select count(1) from enrollment 
            where upper(grade) = 'A' 
            and s_id = s.s_id) > 0) > 0
        and (select count(1) from course_section 
            where f_id = f.f_id  
            and upper(course_no) = 'MIS 441') > 0;

--4 
select distinct concat(f_first, concat(' ', f_last)) as "Nume si Prenume"
from faculty f 
join course_section cs on (f.f_id = cs.f_id)
where cs.c_sec_id in (select cs.c_sec_id from course_section cs 
                        join enrollment e on(e.c_sec_id = cs.c_sec_id)
                        group by cs.c_sec_id
                        having count(e.s_id) = (select max(count(e.s_id)) c
                                                from course_section cs join enrollment e on(e.c_sec_id = cs.c_sec_id)
                                                group by cs.c_sec_id))
union
select distinct concat(f_first, concat(' ', f_last)) as "Nume si Prenume"
from faculty f join course_section cs on (f.f_id = cs.f_id)
where cs.c_sec_id in (select cs.c_sec_id from course_section cs
                      join location l
                      on (l.loc_id = cs.loc_id)
                      where l.capacity = (select max(capacity)
                                          from location));
                                          
--5
select concat(concat(f_first, ' '), f_last) as "Nume si Prenume"
from faculty f
where loc_id in (select loc_id from location
                where capacity = (select min(capacity) from location)) 
                and exists (select * from course_section cs
                            where cs.f_id = f.f_id
                            and max_enrl = (select min(max_enrl) from course_section
                                where loc_id in (select loc_id from location
                                                where capacity = (select max(capacity) from location))));
                                                 
--6
select round(avg(loc.capacity),2) as "Capacitatea salilor", round(avg(enrl.max_enrl),2) as "Numarul de locuri" 
from (select distinct l.loc_id, l.capacity from faculty f 
        join course_section cs on (f.f_id=cs.f_id) 
        join location l on (l.loc_id=cs.loc_id) 
        where upper(f.f_last) = 'MARX') loc,
(select cs.max_enrl from student s 
join enrollment e on (s.s_id = e.s_id) 
join course_section cs on (e.c_sec_id=cs.c_sec_id) 
where upper(s.s_last) = 'JONES') enrl;

--7
select bldg_code, avg(capacity) 
from location
where bldg_code in (select bldg_code from course_section cs 
                    join course c on cs.course_no = c.course_no 
                    join location l on cs.loc_id = l.loc_id
                    where upper(c.course_name) like '%SYSTEMS%')
group by bldg_code;

--8
select l.bldg_code as "Codul cladirii", round(avg(l.capacity), 2) as "Media capacitatilor" 
from location l
where l.bldg_code in (select distinct l2.bldg_code
                    from course_section cs, course c, location l2
                    where upper(c.course_name) like '%SYSTEMS%' 
                    and cs.course_no = c.course_no
                    and l2.loc_id = cs.loc_id)
group by l.bldg_code
union
select 'TOTAL' as "Codul cladirii", round(avg(l.capacity), 2) as "Media capacitatilor" from location l
where l.bldg_code in (select distinct l2.bldg_code
                    from course_section cs, course c, location l2
                    where upper(c.course_name) like '%SYSTEMS%' 
                    and cs.course_no = c.course_no
                    and l2.loc_id = cs.loc_id);
                    
--9
--varianta 1
select course_no, course_name as "Nume Curs" from course
where upper(course_name) like '%JAVA%' or (select count(*) from course
                                            where upper(course_name) like '%JAVA%') = 0;

--varianta 2
select course_no, course_name as "Nume Curs" from course
where (select count(*) from course 
        where upper(course_name) like '%JAVA%') = 0 or upper(course_name) like '%JAVA%';
    
--10
select c.course_name as "Nume Curs" from course c 
where
decode((select count(1) from course_section cs 
        join location l using(loc_id) 
        where c.course_no = cs.course_no  
        and l.capacity = 42), 0, 0, 1) +
decode((select count(1) from faculty f 
        join course_section cs on (cs.f_id=f.f_id) 
        join course c1 on (cs.course_no=c1.course_no) 
        where upper(f.f_last) = 'BROWN' 
        and c.course_no = c1.course_no), 0, 0, 1) +
decode((select count(1) from student s 
        join enrollment e on (e.s_id = s.s_id) 
        join course_section cs on (cs.c_sec_id = e.c_sec_id) 
        where upper(s.s_first) = 'TAMMY'
        and upper(s.s_last) = 'JONES' 
        and c.course_no = cs.course_no), 0, 0, 1) +
decode((select count(1) from course c1 
        where upper(c1.course_name) like '%DATABASE%' 
        and c.course_no = c1.course_no), 0, 0, 1) +
decode((select count(1) from course_section cs 
        join term using (term_id) 
        where c.course_no = cs.course_no
        and extract(year from start_date) > 2007), 0,0,1) >= 3;
        
--11
with nr_max as
    (select max(count(c.course_no))
    from course_section cs 
    join term t on (t.term_id = cs.term_id)
    join course c on (cs.course_no = c.course_no) 
    where upper(c.course_name) like '%DATABASE%'
    group by t.term_id)
select t.term_desc, count(c.course_no) "Numar cursuri" from course_section cs 
join term t on (t.term_id = cs.term_id)
join course c on (cs.course_no = c.course_no) 
where upper(c.course_name) like '%DATABASE%'
group by t.term_id, t.term_desc
having count(c.course_no) = (select * from nr_max);

--12
--varianta 1
select count(distinct s_id) as "Numar Studenti", grade as "Nota" from enrollment
where grade is not null
group by grade
having count(distinct s_id) = (select max(count(distinct s_id))
                              from enrollment
                              where grade is not null
                              group by grade);
                              
--varianta 2
with nr_max as
    (select max(count(distinct s_id)) from enrollment
    where grade is not null
    group by grade)
select count(distinct s_id) "Numar studenti", grade as "Nota" from enrollment
group by grade
having count(distinct s_id) = (select * from nr_max);

--13
select term, count_courses 
from (select t.term_id id, t.term_desc as term, count(cs.course_no) as count_courses from course_section cs 
      join course c on (cs.course_no = c.course_no)
      join term t on (cs.term_id = t.term_id) 
      where c.credits = 3 
      group by t.term_id, t.term_desc 
      order by 2 desc)
where rownum = 1;

--14
select loc_id from location
where loc_id in (select loc_id
                 from course_section cs join course c on cs.course_no = c.course_no
                 where c.course_name = 'Programming in C++')
and loc_id in (select loc_id
                 from course_section cs join course c on cs.course_no = c.course_no
                 where c.course_name = 'Database Management');
                 
--15
select bldg_code from location
group by bldg_code
having count(loc_id) = 1;