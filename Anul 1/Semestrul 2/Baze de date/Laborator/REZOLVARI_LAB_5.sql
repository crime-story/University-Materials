--1
--a

create table ANGAJATI_prof(
cod_ang number(4),
nume varchar2(20), 
prenume varchar2(20),
email char(15), 
data_ang date,
job varchar2(10),
cod_sef number(4),
salariu number(8, 2),
cod_dep number(2));

--b
drop table angajati_prof;
create table ANGAJATI_prof(
cod_ang number(4) primary key,
nume varchar2(20) not null, 
prenume varchar2(20),
email char(15), 
data_ang date,
job varchar2(10),
cod_sef number(4),
salariu number(8, 2) not null,
cod_dep number(2));

describe angajati_prof;


--c
drop table angajati_prof;
create table ANGAJATI_prof(
cod_ang number(4),
nume varchar2(20) not null, 
prenume varchar2(20),
email char(15), 
data_ang date default sysdate,
job varchar2(10),
cod_sef number(4),
salariu number(8, 2) not null,
cod_dep number(2),
constraint pk_angajati_prof primary key(cod_ang));



select * from user_constraints
where lower(table_name) like '%prof';

--2

insert into angajati_prof(cod_ang, nume, prenume, email, data_ang, job, cod_sef, salariu, cod_dep )
values (100, 'Nume1', 'Prenume1', null, null, 'Director', null, 20000, 10);

insert into angajati_prof values(101, 'Nume2', 'Prenume2', 'Nume2',
to_date('02-02-2014', 'dd-mm-yyyy') , 'Inginer', 100, 10000, 10);


insert into angajati_prof values(102, 'Nume3', 'Prenume3', 'Nume3',
to_date('05-06-2010', 'dd-mm-yyyy') , 'Programator', 101, 5000, 20);

insert into angajati_prof(cod_ang, nume, prenume, email, data_ang, job, cod_sef, salariu, cod_dep )
values(103, 'Nume4', 'Prenume4', null, null, 'Inginer', 100, 9000, 20);

insert into angajati_prof values(104, 'Nume5', 'Prenume5', 'Nume5',
null , 'Programator', 101, 3000, 30);

alter table angajati_prof
modify(Job varchar2(15));

select * from angajati_prof;

commit;

--3

create table angajati10_prof as select * from angajati_prof where cod_dep = 10;

select * from angajati10_prof;

describe angajati10_prof;

--4

alter table angajati_prof
add (comision number(4,2));

--5

alter table angajati_prof
modify(salariu number(6,2));

--6

alter table angajati_prof
modify(salariu DEFAULT 1000);

--7
alter table angajati_prof
modify(salariu number(10,2), comision number(2,2));

describe angajati_prof;

--8
update angajati_prof
set comision = 0.1
where upper(job) like 'I%'; 

--9
alter table angajati_prof
modify(email varchar2(20));

commit;

--10

alter table angajati_prof
add (nr_telefon varchar2(15) default '021123456');

--11
select * from angajati_prof;

alter table angajati_prof
drop (nr_telefon);

rollback;

--12

rename angajati_prof to angajati3_prof;

--13
select * from tab;

rename angajati3_prof to angajati_prof;

--14
truncate table  angajati10_prof;

select * from angajati10_prof;

--15

CREATE TABLE departamente_prof (cod_dep number(2), 
nume varchar2(15) not null, 
cod_director number(4)); 
DESC departamente_prof;

--16
insert into departamente_prof values(10, 'Administrativ' , 100);
insert into departamente_prof values (20, 'Proiectare' , 101);
insert into departamente_prof values(30, 'Programare' , null);

select * from departamente_prof;

--17
alter table departamente_prof
add constraint pk_deptro_prof primary key(cod_dep);

--18

--a
alter table angajati_prof
add constraint fk_cod_dept_ang_prof foreign key(cod_dep)
references departamente_prof(cod_dep); 

describe angajati_prof;

--b

drop table angajati_prof;

create table ANGAJATI_prof(
cod_ang number(4) primary key ,
nume varchar2(20) not null, 
prenume varchar2(20),
email char(15) unique, 
data_ang date default sysdate,
job varchar2(15),
cod_sef number(4) constraint fk_cod_sef_prof references angajati_prof(cod_ang),
salariu number(8, 2) not null,
cod_dep number(2) constraint ck_dept_prof check(cod_dep > 0),
comision number(2,2),
constraint u_nume_prenume unique(nume, prenume),
constraint ck_salariu_comisoin_prof check(salariu > comision * 100),
constraint fk_cod_dept_prof foreign key(cod_dep) 
references departamente_prof(cod_dep)
);

--19 
-- tema

--20

describe angajati_prof;

insert into angajati_prof(cod_ang, nume, prenume, email, data_ang, job, cod_sef, salariu, cod_dep )
values (100, 'Nume1', 'Prenume1', null, null, 'Director', null, 20000, 10);

insert into angajati_prof values(101, 'Nume2', 'Prenume2', 'Nume2',
to_date('02-02-2014', 'dd-mm-yyyy') , 'Inginer', 100, 10000, 10, 0.2);


insert into angajati_prof values(102, 'Nume3', 'Prenume3', 'Nume3',
to_date('05-06-2010', 'dd-mm-yyyy') , 'Programator', 101, 5000, 20, null);

insert into angajati_prof(cod_ang, nume, prenume, email, data_ang, job, cod_sef, salariu, cod_dep )
values(103, 'Nume4', 'Prenume4', null, null, 'Inginer', 100, 9000, 20);

insert into angajati_prof values(104, 'Nume5', 'Prenume5', 'Nume5',
null , 'Programator', 101, 3000, 30, 0.3);


select * from angajati_prof;

--21

drop table departamente_prof;

--22
describe user_tables;

describe tab;

describe user_constraints;

--23
SELECT constraint_name, constraint_type, table_name
FROM user_constraints
WHERE lower(table_name) IN ('angajati_prof', 'departamente_prof');

SELECT table_name, constraint_name, column_name
FROM user_cons_columns
WHERE lower(table_name) IN ('angajati_prof', 'departamente_prof');

--24 tema
--25
insert into angajati_prof values(105, 'Nume6', 'Prenume6', 'Nume6',
null , 'Programator', 101, 3000, 50, 0.3);

--26
insert into departamente_prof values(60, 'Testare', null);

commit;

--27
delete from departamente_prof
where cod_dep = 20;

--28
delete from departamente_prof
where cod_dep = 60;

rollback;

--29
insert into angajati_prof values(105, 'Nume6', 'Prenume6', 'Nume6',
null , 'Programator', 114, 3000, 20, 0.3);

--30
insert into angajati_prof values(114, 'Nume7', 'Prenume7', 'Nume7',
null , 'Programator', 114, 3000, 20, 0.3);

insert into angajati_prof values(105, 'Nume6', 'Prenume6', 'Nume6',
null , 'Programator', 114, 3000, 10, 0.3);

select * from angajati_prof;

--31
alter table angajati_prof
drop constraint FK_COD_DEPT_PROF;


alter table angajati_prof
add constraint FK_COD_DEPT_PROF2 foreign key(cod_dep)
references departamente_prof(cod_dep) on delete cascade;  

--32
delete from departamente_prof
where cod_dep = 20;

rollback;

--33
alter table departamente_prof
add constraint FK_COD_DIR_ANG_PROF2 foreign key(cod_director)
references angajati_prof(cod_ang) on delete set null;

--34
update departamente_prof
set cod_director = 102
where cod_dep = 30;

commit;

delete from angajati_prof 
where cod_ang = 102;

select * from departamente_prof;
rollback;

--35
alter table angajati_prof
add constraint ck_salariu_max_prof check(salariu <= 30000);

--36
update angajati_prof
set salariu = 35000
where cod_ang = 100;

--37
alter table angajati_prof
disable constraint ck_salariu_max_prof;

alter table angajati_prof
enable constraint ck_salariu_max_prof;


