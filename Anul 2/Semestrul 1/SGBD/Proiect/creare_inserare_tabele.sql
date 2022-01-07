-- Popescu Paullo Robertto Karloss --
-- Grupa 131 --

--- CREAREA TABELELOR ---

-- LOCATIE --
CREATE TABLE locatie (
    id_locatie   NUMBER(10) PRIMARY KEY,
    tara         VARCHAR2(20),
    oras         VARCHAR2(20),
    cod_postal   VARCHAR2(15) UNIQUE,
    strada       VARCHAR2(50)
);

-- RESTAURANT --
CREATE TABLE restaurant (
    id_restaurant   NUMBER(10) PRIMARY KEY,
    id_locatie      NUMBER(10),
    nume            VARCHAR2(32) UNIQUE,
    CONSTRAINT fk_restaurant_locatie FOREIGN KEY ( id_locatie )
        REFERENCES locatie ( id_locatie )
);

-- PRODUS --
CREATE TABLE produs (
    id_produs   NUMBER(10) PRIMARY KEY,
    nume        VARCHAR2(25),
    gramaj      NUMBER(10),
    descriere   VARCHAR2(100),
    CONSTRAINT chk_gramaj CHECK ( gramaj > 0 )
);

-- MENIU --
CREATE TABLE meniu (
    id_produs       NUMBER(10),
    id_restaurant   NUMBER(10),
    pret            NUMBER(10),
    CONSTRAINT meniu_pk PRIMARY KEY ( id_produs,
                                      id_restaurant ),
    CONSTRAINT meniu_produs_fk FOREIGN KEY ( id_produs )
        REFERENCES produs ( id_produs ),
    CONSTRAINT meniu_restaurant_fk FOREIGN KEY ( id_restaurant )
        REFERENCES restaurant ( id_restaurant ),
    CONSTRAINT chk_pret CHECK ( pret > 0 )
);

-- ANGAJAT --
CREATE TABLE angajat (
    id_angajat      NUMBER(10) PRIMARY KEY,
    id_restaurant   NUMBER(10),
    nume            VARCHAR2(32),
    prenume         VARCHAR2(32),
    data_angajare   DATE DEFAULT to_date(sysdate, 'dd-mm-yy'),
    CONSTRAINT angajat_restaurant_fk FOREIGN KEY ( id_restaurant )
        REFERENCES restaurant ( id_restaurant )
);

-- BUCATAR --
CREATE TABLE bucatar (
    id_angajat   NUMBER(10),
    nr_stele     NUMBER(7),
    CONSTRAINT bucatar_pk PRIMARY KEY ( id_angajat ),
    CONSTRAINT angajat_bucatar_fk FOREIGN KEY ( id_angajat )
        REFERENCES angajat ( id_angajat )
);

-- CASIER --
CREATE TABLE casier (
    id_angajat   NUMBER(10),
    ani_studiu   NUMBER(5),
    CONSTRAINT casier_pk PRIMARY KEY ( id_angajat ),
    CONSTRAINT angajat_casier_fk FOREIGN KEY ( id_angajat )
        REFERENCES angajat ( id_angajat )
);

-- CHELNER --
CREATE TABLE chelner (
    id_angajat       NUMBER(10),
    ani_experienta   NUMBER(5),
    CONSTRAINT chelner_pk PRIMARY KEY ( id_angajat ),
    CONSTRAINT angajat_fk FOREIGN KEY ( id_angajat )
        REFERENCES angajat ( id_angajat )
);

-- MANAGER --
CREATE TABLE manager (
    id_angajat NUMBER(10),
    CONSTRAINT manager_pk PRIMARY KEY ( id_angajat ),
    CONSTRAINT angajat_manager_fk FOREIGN KEY ( id_angajat )
        REFERENCES angajat ( id_angajat )
);

-- PRODUCATOR --
CREATE TABLE producator (
    id_producator   NUMBER(10) PRIMARY KEY,
    nume            VARCHAR2(32),
    nr_telefon      VARCHAR2(15) UNIQUE
);

-- INGREDIENT --
CREATE TABLE ingredient (
    id_ingredient   NUMBER(12) PRIMARY KEY,
    id_producator   NUMBER(10),
    nume            VARCHAR2(32),
    CONSTRAINT fk_ingredient_producator FOREIGN KEY ( id_producator )
        REFERENCES producator ( id_producator )
);

-- CANTITATE_PRODUS --
CREATE TABLE cantitate_produs (
    id_produs       NUMBER(10),
    id_ingredient   NUMBER(12),
    cantitate       NUMBER(10),
    CONSTRAINT cantitate_produs_pk PRIMARY KEY ( id_produs,
                                                 id_ingredient ),
    CONSTRAINT cantitate_produs_fk FOREIGN KEY ( id_produs )
        REFERENCES produs ( id_produs ),
    CONSTRAINT cantitate_ingredient_fk FOREIGN KEY ( id_ingredient )
        REFERENCES ingredient ( id_ingredient ),
    CONSTRAINT chk_cantitate_produs CHECK ( cantitate > 0 )
);


-- CLIENT --
CREATE TABLE client (
    id_client    NUMBER(10) PRIMARY KEY,
    nume         VARCHAR2(32) NOT NULL,
    prenume      VARCHAR2(32) NOT NULL,
    nr_telefon   VARCHAR2(15)
);

-- FACTURA --
CREATE TABLE factura (
    id_factura   NUMBER(15) PRIMARY KEY,
    id_angajat   NUMBER(10),
    valoare      NUMBER(10),
    detalii      VARCHAR2(50),
    CONSTRAINT fk_factura_casier FOREIGN KEY ( id_angajat )
        REFERENCES casier ( id_angajat ),
    CONSTRAINT chk_factura_valoare CHECK ( valoare > 0 )
);

-- COMANDA --
CREATE TABLE comanda (
    id_comanda   NUMBER(20) PRIMARY KEY,
    id_client    NUMBER(10),
    id_factura   NUMBER(15),
    pret         NUMBER(10) NOT NULL,
    data         DATE DEFAULT to_date(sysdate, 'dd-mm-yy'),
    CONSTRAINT fk_comanda_client FOREIGN KEY ( id_client )
        REFERENCES client ( id_client ),
    CONSTRAINT fk_comanda_factura FOREIGN KEY ( id_factura )
        REFERENCES factura ( id_factura ),
    CONSTRAINT chk_comanda CHECK ( pret > 0 )
);

-- CONTINUT_COMANDA --
CREATE TABLE continut_comanda (
    id_produs       NUMBER(10),
    id_comanda      NUMBER(20),
    numar_produse   NUMBER(10),
    CONSTRAINT continut_comanda_pk PRIMARY KEY ( id_produs,
                                                 id_comanda ),
    CONSTRAINT continut_produs_fk FOREIGN KEY ( id_produs )
        REFERENCES produs ( id_produs ),
    CONSTRAINT continut_comanda_fk FOREIGN KEY ( id_comanda )
        REFERENCES comanda ( id_comanda ),
    CONSTRAINT chk_continut_comanda CHECK ( numar_produse > 0 )
);

-- PREPARARE --
CREATE TABLE preparare (
    id_produs    NUMBER(10),
    id_comanda   NUMBER(20),
    id_angajat   NUMBER(10),
    durata       NUMBER(10),
    CONSTRAINT preparare_pk PRIMARY KEY ( id_produs,
                                          id_comanda,
                                          id_angajat ),
    CONSTRAINT preparare_casier_fk FOREIGN KEY ( id_angajat )
        REFERENCES bucatar ( id_angajat ),
    CONSTRAINT preparare_produs_fk FOREIGN KEY ( id_produs )
        REFERENCES produs ( id_produs ),
    CONSTRAINT preparare_comanda_fk FOREIGN KEY ( id_comanda )
        REFERENCES comanda ( id_comanda )
);


--- INSERAREA DATELOR IN TABELE ---

-- PENTRU TABELUL LOCATIE --
create sequence id_locatie
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into locatie
values (id_locatie.nextval, 'Romania' , 'Bucuresti', '010051', 'Batista'); --1

insert into locatie
values (id_locatie.nextval, 'Romania', 'Cluj', '400033', 'Mihai Eminescu'); --2

insert into locatie
values (id_locatie.nextval, 'Romania', 'Iasi', '700547', 'Rediu'); --3

insert into locatie
values (id_locatie.nextval, 'Italia', 'Milano', '20127', 'Monza'); --4

insert into locatie
values (id_locatie.nextval, 'Germania', 'Munchen', '80687', 'Perhamerstrabe'); --5


-- PENTRU TABELUL RESTAURANT --
create sequence id_restaurant
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into restaurant
values (id_restaurant.nextval, 2, 'Gurmandul'); --1

insert into restaurant
values (id_restaurant.nextval, 3, 'Yamas'); --2

insert into restaurant
values (id_restaurant.nextval, 1, 'Ivans'); --3

insert into restaurant
values (id_restaurant.nextval, 5, 'Savanna'); --4

insert into restaurant
values (id_restaurant.nextval, 4, 'Grande appetito!'); --5


-- PENTRU TABELUL PRODUS --
-- aici cantitatea este in grame
create sequence id_produs
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into produs
values (id_produs.nextval, 'Spaghete', 200, 'picante'); --1

insert into produs
values (id_produs.nextval, 'Pizza', 150, 'dulce aromata'); --2

insert into produs
values (id_produs.nextval, 'Spaghete', 200, 'picante'); --3

-- aici am updatat linia pentru paste bolognese
update produs
set nume = 'Paste bolognese', descriere = 'cu sos dulce'
where id_produs = 3;

insert into produs
values (id_produs.nextval, 'Sarmale', 250, 'ca la mama acasa'); --4

insert into produs
values (id_produs.nextval, 'Ciorba de post', 300, 'fara carne'); --5

insert into produs(id_produs,nume,gramaj)
values (id_produs.nextval, 'Sushi', 300); --6


-- PENTRU TABELUL MENIU --
-- pretul este in lei --
insert into meniu (id_restaurant, id_produs, pret)
values (4, 1, 35); --1

insert into meniu (id_restaurant, id_produs, pret)
values (4, 2, 50); --2

insert into meniu (id_restaurant, id_produs, pret)
values (1, 5, 20); --3

insert into meniu (id_restaurant, id_produs, pret)
values (1, 4, 33); --4

insert into meniu (id_restaurant, id_produs, pret)
values (5, 2, 60); --5

insert into meniu (id_restaurant, id_produs, pret)
values (2, 4, 15); --6

insert into meniu (id_restaurant, id_produs, pret)
values (5, 4, 13); --7
commit;

insert into meniu (id_restaurant, id_produs, pret)
values (3, 5, 12); --8

insert into meniu (id_restaurant, id_produs, pret)
values (4, 4, 30); --9

insert into meniu (id_restaurant, id_produs, pret)
values (3, 4, 15); --10

insert into meniu (id_restaurant, id_produs, pret)
values (2, 3, 27); --11


-- PENTRU TABELUL ANGAJAT --
create sequence id_angajat
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into angajat (id_angajat, id_restaurant, nume, prenume)
values (id_angajat.nextval, 1, 'Popescu', 'Robertto'); --1

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 2, 'Escobar', 'Ricardo', to_date('15-02-21', 'dd-mm-yy')); --2

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 4, 'Marinescu', 'Teodora', to_date('10-01-20','dd-mm-yy')); --3

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 3, 'Marinescu', 'Petre', to_date('15-01-21','dd-mm-yy')); --4

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 2, 'Manole', 'Alexandru', to_date('20-06-19','dd-mm-yy')); --5

insert into angajat (id_angajat, id_restaurant, nume, prenume)
values (id_angajat.nextval, 1, 'Voicu', 'Andrei'); --6

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 3, 'Radoi', 'Raisa', to_date('15-05-20','dd-mm-yy')); --7

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 4, 'Stan', 'Mihnea', to_date('01-01-20','dd-mm-yy')); --8

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 5, 'Filip', 'Mihnea', to_date('21-09-20','dd-mm-yy')); --9

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 1, 'Peste', 'Florin', to_date('25-07-20','dd-mm-yy')); --10

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 2, 'Raducanu', 'Sorin', to_date('01-04-21', 'dd-mm-yy')); --11

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 3, 'Bida', 'Marian', to_date('25-02-21', 'dd-mm-yy')); --12

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 4, 'Fredo', 'Magiore', to_date('01-05-21', 'dd-mm-yy')); --13

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 5, 'Cercel', 'Florin', to_date('02-09-20','dd-mm-yy')); --14

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 4, 'Stanescu', 'Gigel', to_date('02-07-20', 'dd-mm-yy')); --15

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 2, 'Dociu', 'Mihai', to_date('17-08-20', 'dd-mm-yy')); --16

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 1, 'Dumitrescu', 'Florin', to_date('04-03-21', 'dd-mm-yy')); --17

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 3, 'Sociu', 'Razvan', to_date('29-12-20', 'dd-mm-yy')); --18

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 5, 'Scarlatescu', 'Catalin', to_date('01-03-21', 'dd-mm-yy')); --19

insert into angajat (id_angajat, id_restaurant, nume, prenume, data_angajare)
values (id_angajat.nextval, 2, 'Bontea', 'Sorin', to_date('02-03-21', 'dd-mm-yy')); --20

insert into angajat (id_angajat, id_restaurant, nume, prenume)
values (21, 1, 'Daniel', 'Andrei'); --21


-- PENTRU TABELUL BUCATAR --
insert into bucatar (id_angajat, nr_stele)
values (9, 1); --1

insert into bucatar (id_angajat, nr_stele)
values (17, 3); --2

insert into bucatar (id_angajat, nr_stele)
values (18, 4); --3

insert into bucatar (id_angajat, nr_stele)
values (19, 5); --4

insert into bucatar (id_angajat, nr_stele)
values (20, 5); --5

insert into bucatar (id_angajat, nr_stele)
values (21, 2); --6


-- PENTRU TABELUL CASIER --
insert into casier(id_angajat, ani_studiu)
values (3, 10); --1

insert into casier(id_angajat, ani_studiu)
values (16, 2); --2

insert into casier(id_angajat, ani_studiu)
values (5, 0); --3

insert into casier(id_angajat, ani_studiu)
values (12, 1); --4

insert into casier(id_angajat, ani_studiu)
values (8, 4); --5


-- PENTRU TABELUL CHELNER --
insert into chelner (id_angajat, ani_experienta)
values (4, 2); --1

insert into chelner (id_angajat, ani_experienta)
values (7, 5); --2

insert into chelner (id_angajat, ani_experienta)
values (11, 1); --3

insert into chelner (id_angajat, ani_experienta)
values (14, 10); --4

insert into chelner (id_angajat, ani_experienta)
values (15, 7); --5


-- PENTRU TABELUL MANAGER --
insert into manager (id_angajat)
values (1); --1

insert into manager (id_angajat)
values (2); --2

insert into manager (id_angajat)
values (6); --3

insert into manager (id_angajat)
values (10); --4

insert into manager (id_angajat)
values (13); --5


-- PENTRU TABELUL PRODUCATOR --
create sequence id_producator
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into producator
values (id_producator.nextval, 'Livada cu de toate', '0770573182'); --1

insert into producator
values (id_producator.nextval, 'Olivers', '0754754318'); --2

insert into producator
values (id_producator.nextval, 'FreshOnly', '021999123'); --3

insert into producator
values (id_producator.nextval, 'ION MOS', '021129123'); --4

insert into producator
values (id_producator.nextval, 'FUCHS', '021592555'); --5

insert into producator
values (id_producator.nextval, 'Bergenbier', '026662555'); --6

insert into producator
values (id_producator.nextval, 'Tuborg', '072592555'); --7


-- PENTRU TABELUL INGREDIENT --
create sequence id_ingredient
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into ingredient
values (id_ingredient.nextval, 1, 'rosii'); --1

insert into ingredient
values (id_ingredient.nextval, 3, 'varza murata'); --2

insert into ingredient
values (id_ingredient.nextval, 4, 'ulei floarea soarelui'); --3

insert into ingredient
values (id_ingredient.nextval, 5, 'condimente'); --4

insert into ingredient
values (id_ingredient.nextval, 2, 'bors'); --5

insert into ingredient
values (id_ingredient.nextval, 2, 'parmezan'); --6

insert into ingredient
values (id_ingredient.nextval, 2, 'cascaval'); --7


-- PENTRU TABELUL CANTITATE_PRODUS --
-- cantitatea este masurata in grame --
insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (1, 1, 85); --1

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (1, 4, 50); --2

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (2, 4, 60); --3

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (2, 3, 150); --4

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (2, 1, 100); --5

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (3, 4, 200); --6

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (3, 5, 55); --7

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (4, 4, 100); --8

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (4, 3, 500); --9

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (5, 5, 350); --10

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (2, 6, 50); --11

insert into cantitate_produs (id_produs, id_ingredient, cantitate)
values (2, 7, 35); --12


-- PENTRU TABELUL CLIENT --
create sequence id_client
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into client
values (id_client.nextval, 'Ionescu', 'Marian', '0721666212'); --1

insert into client (id_client, nume, prenume, nr_telefon)
values (id_client.nextval, 'Dumitrescu', 'Mircel', '0721611211'); --2

insert into client (id_client, nume, prenume, nr_telefon)
values (id_client.nextval, 'Gheorghe', 'Sebastian', '0744573419'); --3

insert into client (id_client, nume, prenume, nr_telefon)
values (id_client.nextval, 'Salam','Florin', '0210116666'); --4

insert into client (id_client, nume, prenume, nr_telefon)
values (id_client.nextval, 'Biju', 'Costel', '0211999913'); --5

insert into client (id_client, nume, prenume)
values (id_client.nextval, 'Hagi', 'Gica'); --6

insert into client (id_client, nume, prenume)
values (id_client.nextval, 'Becali', 'Gigi'); --7


-- PENTRU TABELUL FACTURA --
-- fiecare factura are o valoare care include mai mult taxe etc --
create sequence id_factura
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into factura (id_factura, id_angajat, valoare, detalii)
values (id_factura.nextval, 16, 100, 'CASH'); --1

insert into factura (id_factura, id_angajat, valoare, detalii)
values (id_factura.nextval, 12, 250, 'CARD'); --2

insert into factura (id_factura, id_angajat, valoare, detalii)
values (id_factura.nextval, 8, 50, 'CASH'); --3

insert into factura (id_factura, id_angajat, valoare, detalii)
values (id_factura.nextval, 3, 70, 'CASH'); --4

insert into factura (id_factura, id_angajat, valoare, detalii)
values (id_factura.nextval, 5, 150, 'CARD'); --5


-- PENTRU TABELUL COMANDA --
create sequence id_comanda
start with 1
increment by 1
minvalue 0
maxvalue 9999
nocycle;

insert into comanda (id_comanda, id_client, id_factura, pret)
values (id_comanda.nextval, 1, 4, 50); --1

insert into comanda (id_comanda, id_client, id_factura, pret)
values (id_comanda.nextval, 3, 5, 130); --2

insert into comanda (id_comanda, id_client, id_factura, pret)
values (id_comanda.nextval, 2, 3, 20); --3

insert into comanda (id_comanda, id_client, id_factura, pret)
values (id_comanda.nextval, 4, 2, 230); --4

insert into comanda (id_comanda, id_client, id_factura, pret)
values (id_comanda.nextval, 5, 1, 80); --5

insert into comanda (id_comanda, id_client, id_factura, pret)
values (id_comanda.nextval, 1, 4, 100); --6


-- PENTRU TABELUL CONTINUT_COMANDA --
insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (1, 4, 2); --1

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (1, 2, 1); --2

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (2, 2, 1); --3

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (2, 1, 1); --4

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (3, 5, 1); --5

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (3, 4, 1); --6

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (4, 5, 1); --7

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (5, 4, 1); --8

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (5, 2, 1); --9

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (3, 3, 1); --10

insert into continut_comanda (id_produs, id_comanda, numar_produse)
values (2, 6, 2); --11


-- PENTRU TABELUL PREPARARE --
-- durata este masurata in minute --
insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (1, 1, 17, 60); --1

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (1, 2, 9, 45); --2

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (2, 4, 19, 35); --3

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (3, 4, 19, 25); --4

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (4, 4, 19, 120); --5

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (5, 4, 17, 75); --6

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (3, 3, 20, 70); --7

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (3, 5, 9, 16); --8

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (1, 2, 17, 130); --9

insert into preparare (id_produs, id_comanda, id_angajat, durata)
values (4, 2, 18, 240); --10

commit;