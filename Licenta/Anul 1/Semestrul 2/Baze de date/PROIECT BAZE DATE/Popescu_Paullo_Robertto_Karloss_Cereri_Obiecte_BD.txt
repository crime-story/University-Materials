-- Popescu Paullo Robertto Karloss --
-- Grupa 131 --

-- CERINTA 11 --
-- 1) Sa se afiseze numele si id-ul tuturor produselor comandate de un client vreodata.
SELECT DISTINCT
    cl.id_client,
    cl.nume,
    cl.prenume,
    prod.id_produs,
    prod.nume
FROM
    comanda            c,
    client             cl,
    continut_comanda   cmd,
    produs             prod
WHERE
    cl.id_client = c.id_client
    AND c.id_comanda = cmd.id_comanda
    AND cmd.id_produs = prod.id_produs
ORDER BY
    id_client,
    id_produs;

-- 2) Pentru fiecare bucatar angajat inanul curent, afisati profitul mediu pe care il poate aduce daca ar vinde
--    din fiecare mancare pe care stie sa o prepare exact o bucata.
SELECT
    a.id_angajat,
    lower(a.nume),
    lower(a.prenume),
    r.id_restaurant,
    upper(r.nume),
    AVG(m.pret) "castig mediu"
FROM
    angajat      a,
    bucatar      b,
    preparare    prep,
    produs       prod,
    meniu        m,
    restaurant   r
WHERE
    a.id_restaurant = r.id_restaurant
    AND a.id_angajat = b.id_angajat
    AND b.id_angajat = prep.id_angajat
    AND prod.id_produs = prep.id_produs
    AND prod.id_produs = m.id_produs
    AND m.id_restaurant = r.id_restaurant
    AND to_char(a.data_angajare, 'yyyy') LIKE to_char(sysdate, 'yyyy')
GROUP BY (
    a.id_angajat,
    a.nume,
    a.prenume,
    r.id_restaurant,
    r.nume
);

-- 3) Afisati pentru fiecare produs, id-ul bucatarului care il prepara si cate stele are bucatarul
--    sau mesaj daca nu e preparat de nimeni
--    daca e preparat de mai multi, ii afisati pe toti.
-- Nesicronizata
with myProd as (
    select distinct prod.ID_PRODUS, prod.NUME, prep.ID_ANGAJAT
    from produs prod,
         PREPARARE prep
    where prod.ID_PRODUS = prep.ID_PRODUS(+))
select myProd.ID_PRODUS,
       myProd.NUME,
       decode(nvl(om.ID_ANGAJAT, -1), -1, 'nu e preparat de nimeni', om.id_angajat) preparator,
       om.NR_STELE,
       om.ID_RESTAURANT
from myProd,
     (select a.ID_ANGAJAT, a.NUME, a.PRENUME, b.NR_STELE, r.ID_RESTAURANT
      from ANGAJAT a,
           BUCATAR b,
           RESTAURANT r
      where a.ID_ANGAJAT = b.ID_ANGAJAT
        and a.ID_RESTAURANT = r.ID_RESTAURANT) om
where myProd.ID_ANGAJAT = om.ID_ANGAJAT(+);



--- 4) Afisati toti clientii cu toate comenzile lor si facturile aferente,
--     a caror suma cheltuita pana acum depaseste average-ul comenzilor cu cel putin 3 produs
-- Sincronizata
select *
from CLIENT cl,
     COMANDA cmd,
     FACTURA f
where cmd.ID_CLIENT = cl.ID_CLIENT
  and f.ID_FACTURA = cmd.ID_FACTURA
  and (select sum(cmd2.PRET)
       from COMANDA cmd2
       where cmd2.ID_CLIENT = cl.ID_CLIENT) >
      (select avg(t1.pr)
       from (select cmd3.ID_COMANDA, cmd3.PRET pr
             from COMANDA cmd3,
                  CONTINUT_COMANDA cnt
             where cmd3.ID_COMANDA = cnt.ID_COMANDA
             group by cmd3.ID_COMANDA, cmd3.PRET
             having count(*) >= 3) t1);


-- 5) Pentru fiecare pereche de angajati, afisati daca acestia se cunosc de cel putin un an.
select case
           when months_between(a1.DATA_ANGAJARE, a2.DATA_ANGAJARE) >= 12 then 'Angajatii ' || a1.ID_ANGAJAT || ' si ' ||
                                                                              a2.ID_ANGAJAT ||
                                                                              ' se cunosc de peste un an'
           else 'Angajatii ' || a1.ID_ANGAJAT || ' si ' || a2.ID_ANGAJAT || ' se cunosc de mai putin de un an' end
from ANGAJAT a1,
     ANGAJAT a2
where a1.ID_ANGAJAT < a2.ID_ANGAJAT;

-- CERINTA 12 --
-- 1) suprimare
delete from producator 
where nume in (select nume 
                from producator
                where upper(nume) like '%BERGENBIER%');

-- 2) update               
update client
set nr_telefon = '666'
where nr_telefon is null
and nume = (select c1.nume from client c1 where lower(c1.nume) like '%becali%');

-- 3) update
update produs
set gramaj = (select avg(gramaj) from produs)
where descriere is null;

-- CERINTA 13 -- 
-- Secventele le-am implementat la inserarea datelor in tabele
-- Le-am pus si aici in comentarii 

---- PENTRU TABELUL LOCATIE --
--create sequence id_locatie
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL RESTAURANT --
--create sequence id_restaurant
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL PRODUS --
---- aici cantitatea este in grame
--create sequence id_produs
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL ANGAJAT --
--create sequence id_angajat
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL PRODUCATOR --
--create sequence id_producator
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL INGREDIENT --
--create sequence id_ingredient
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL CLIENT --
--create sequence id_client
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL FACTURA --
---- fiecare factura are o valoare care include mai mult taxe etc --
--create sequence id_factura
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;

---- PENTRU TABELUL COMANDA --
--create sequence id_comanda
--start with 1
--increment by 1
--minvalue 0
--maxvalue 9999
--nocycle;


-- CERINTA 16
-- Folosim outer join aici
-- Afisati pentru toate produsele din sistem, la ce restaurant se servesc, sau null daca nu e servit nicaieri.
select p.ID_PRODUS, p.NUME, m.ID_RESTAURANT, r.NUME, l.TARA, l.ORAS, l.STRADA
from meniu m, PRODUS p, RESTAURANT r, locatie l
where m.ID_PRODUS(+)=p.ID_PRODUS 
and m.ID_RESTAURANT=r.ID_RESTAURANT(+) 
and r.ID_RESTAURANT=l.ID_LOCATIE(+); --outer join

-- Afisati produsele care se gasesc in toate restaurantele.
-- Folosim division
select distinct p.ID_PRODUS,p.NUME
from PRODUS p, MENIU m
where p.ID_PRODUS in (select distinct m.ID_PRODUS from MENIU m) and m.ID_PRODUS=p.ID_PRODUS
group by p.ID_PRODUS, p.NUME
having count(p.ID_PRODUS) = (select count(r1.ID_RESTAURANT) from RESTAURANT r1);

-- Afisati produsele care se gasesc in toate comenzile dintr-o anumita zi.
-- Folosim Division
insert into FACTURA values(1000,3,100,'nu');
insert into COMANDA values (1000,1,1000,1000,to_date('13-07-2000','dd-mm-yyyy'));
insert into CONTINUT_COMANDA values (4,1000,3);

with t as(select * from CONTINUT_COMANDA cnt0, COMANDA cmd0 where cnt0.ID_COMANDA=cmd0.ID_COMANDA)
select distinct p.ID_PRODUS, p.NUME
from PRODUS p, t t1
where p.ID_PRODUS=t1.ID_PRODUS 
and to_char(t1.DATA,'dd-mm-yyyy')='13-07-2000'
and p.ID_PRODUS in (select distinct t2.ID_PRODUS from t t2 
                    where to_char(t2.DATA, 'dd-mm-yyyy')='13-07-2000')
group by p.ID_PRODUS, p.NUME
having count(p.ID_PRODUS)=(select count(*) from t t3 
                            where  to_char(t3.DATA, 'dd-mm-yyyy')='13-07-2000');

-- CERINTA 17 --
-- neoptimizata
with
R1 as (select * from produs where descriere like '%picante%'),
R2 as (select * from produs where descriere like '%cu%sos%dulce%'),
R3 as (select * from R1 union select * from R2),
R4 as (select id_produs from R3),
R5 as (select * from meniu where pret >= 15),
R6 as (select * from R4, R5 where R4.id_produs = R5.id_produs),
R7 as (select t2.id_restaurant, t2.nume from R6 t1, restaurant t2 where t1.id_restaurant = t2.id_restaurant)
select R7.id_restaurant, R7.nume from R7;

-- optimizata
with
R1 as (select * from produs where descriere like '%picante%'),
R2 as (select * from produs where descriere like '%cu%sos%dulce%'),
R3 as (select id_produs from R1),
R4 as (select id_produs from R2),
R5 as (select * from R3 union select * from R4),
R6 as (select * from meniu where pret >= 15),
R7 as (select * from R5, R6 where R5.id_produs = R6.id_produs),
R8 as (select id_restaurant, nume from restaurant)
select R8.id_restaurant, R8.nume from R7, R8 where R8.id_restaurant = R7.id_restaurant;