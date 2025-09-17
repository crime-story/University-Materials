DROP TABLE gazduit CASCADE CONSTRAINTS;
DROP TABLE fotografie CASCADE CONSTRAINTS;
DROP TABLE artist CASCADE CONSTRAINTS;
DROP TABLE sala CASCADE CONSTRAINTS;
DROP TABLE expozitie CASCADE CONSTRAINTS;
DROP TABLE locatie CASCADE CONSTRAINTS;

SELECT 
SYS_CONTEXT('USERENV','NLS_TERRITORY') nls_territory,
SYS_CONTEXT('USERENV','NLS_DATE_FORMAT') nls_date_format,
SYS_CONTEXT('USERENV','NLS_DATE_LANGUAGE') nls_date_language,
SYS_CONTEXT('USERENV','NLS_SORT') nls_sort, 
SYS_CONTEXT('USERENV','LANGUAGE') language
FROM dual;

SELECT SYSDATE FROM dual;

ALTER SESSION SET NLS_LANGUAGE='AMERICAN';
ALTER SESSION SET NLS_TERRITORY='AMERICA';
ALTER SESSION SET NLS_DATE_LANGUAGE='AMERICAN';

CREATE TABLE locatie (
    cod_locatie NUMBER CONSTRAINT pk_locatie PRIMARY KEY,
    nume_locatie VARCHAR2(50) NOT NULL,
    tara        VARCHAR2(50) NOT NULL,
    cod_postal  VARCHAR2(10),
    CONSTRAINT uc_locatie UNIQUE (nume_locatie, tara)
);

CREATE TABLE expozitie (
    cod_expozitie   NUMBER CONSTRAINT pk_expozitie PRIMARY KEY,
    nume_expozitie  VARCHAR2(50) NOT NULL,
    nume_cladire    VARCHAR2(50),
    adresa          VARCHAR2(50) NOT NULL,
    cod_locatie     NUMBER CONSTRAINT fk_expozitie_locatie REFERENCES locatie(cod_locatie),
    data_inceput    DATE,
    data_sfarsit    DATE,
    tip_expozitie   VARCHAR2(50),
    CONSTRAINT ck_perioada CHECK (data_inceput <= data_sfarsit)
);

CREATE TABLE sala (
    cod_sala        NUMBER CONSTRAINT pk_sala PRIMARY KEY,
    cod_expozitie   NUMBER CONSTRAINT fk_sala_expozitie REFERENCES expozitie(cod_expozitie) ON DELETE CASCADE,
    nume_sala       VARCHAR2(50) NOT NULL,
    suprafata       NUMBER,
    capacitate_maxima NUMBER,
    CONSTRAINT ck_capacitate CHECK (capacitate_maxima > 0)
);

CREATE TABLE artist (
    cod_artist      NUMBER CONSTRAINT pk_artist PRIMARY KEY,
    nume            VARCHAR2(50) NOT NULL,
    prenume         VARCHAR2(50),
    data_nasterii   DATE,
    data_decesului  DATE,
    nationalitate   VARCHAR2(50),
    cod_locatie     NUMBER CONSTRAINT fk_artist_locatie REFERENCES locatie(cod_locatie),
    CONSTRAINT ck_dates CHECK (data_decesului IS NULL OR data_nasterii <= data_decesului)
);

CREATE TABLE fotografie (
    cod_fotografie  NUMBER CONSTRAINT pk_fotografie PRIMARY KEY,
    gen             VARCHAR2(50),
    titlu           VARCHAR2(50) NOT NULL,
    valoare         NUMBER(8, 2) NOT NULL,
    data_crearii    DATE NOT NULL,
    dimensiuni      VARCHAR2(20),
    cod_artist      NUMBER CONSTRAINT fk_fotografie_artist REFERENCES artist(cod_artist),
    cod_sala        NUMBER CONSTRAINT fk_fotografie_sala REFERENCES sala(cod_sala),
    CONSTRAINT ck_valoare CHECK (0 < valoare AND valoare < 1000000),
    CONSTRAINT ck_data_crearii CHECK (data_crearii <= TO_DATE('1/10/2024', 'DD/MM/YYYY'))
);

CREATE TABLE gazduit (
    cod_artist    NUMBER,
    cod_expozitie NUMBER,
    rol_artist VARCHAR2(50),
    data_participare DATE,
    premiu VARCHAR2(50),
    CONSTRAINT pk_gazduit PRIMARY KEY (cod_artist, cod_expozitie),
    CONSTRAINT fk_gazduit_artist FOREIGN KEY (cod_artist) REFERENCES artist(cod_artist) ON DELETE CASCADE,
    CONSTRAINT fk_gazduit_expozitie FOREIGN KEY (cod_expozitie) REFERENCES expozitie(cod_expozitie) ON DELETE CASCADE
);


INSERT INTO locatie (cod_locatie, nume_locatie, tara, cod_postal)
VALUES (1, 'Bucuresti', 'Romania', '010001');

INSERT INTO locatie (cod_locatie, nume_locatie, tara, cod_postal)
VALUES (2, 'Cluj-Napoca', 'Romania', '400001');

INSERT INTO locatie (cod_locatie, nume_locatie, tara, cod_postal)
VALUES (3, 'Paris', 'Franta', '75001');

INSERT INTO locatie (cod_locatie, nume_locatie, tara, cod_postal)
VALUES (4, 'Londra', 'Marea Britanie', 'EC1A1BB');

INSERT INTO locatie (cod_locatie, nume_locatie, tara, cod_postal)
VALUES (5, 'Berlin', 'Germania', '10115');

INSERT INTO expozitie (cod_expozitie, nume_expozitie, nume_cladire, adresa, cod_locatie, data_inceput, data_sfarsit, tip_expozitie)
VALUES (1, 'Expozitia de Arta Moderna', 'Muzeul National', 'Strada Muzeului, nr. 1', 1, TO_DATE('01/09/2024', 'DD/MM/YYYY'), TO_DATE('30/09/2024', 'DD/MM/YYYY'), 'Temporara');

INSERT INTO expozitie (cod_expozitie, nume_expozitie, nume_cladire, adresa, cod_locatie, data_inceput, data_sfarsit, tip_expozitie)
VALUES (2, 'Fotografia Contemporana', 'Palatul Artelor', 'Strada Libertatii, nr. 3', 2, TO_DATE('15/10/2024', 'DD/MM/YYYY'), TO_DATE('20/11/2024', 'DD/MM/YYYY'), 'Permanenta');

INSERT INTO expozitie (cod_expozitie, nume_expozitie, nume_cladire, adresa, cod_locatie, data_inceput, data_sfarsit, tip_expozitie)
VALUES (3, 'Sculptura Clasica', 'Galeria Louvre', 'Rue de Rivoli', 3, TO_DATE('01/07/2024', 'DD/MM/YYYY'), TO_DATE('01/12/2024', 'DD/MM/YYYY'), 'Temporara');

INSERT INTO expozitie (cod_expozitie, nume_expozitie, nume_cladire, adresa, cod_locatie, data_inceput, data_sfarsit, tip_expozitie)
VALUES (4, 'Expozitia de Fotografie', 'Muzeul Britanic', 'Great Russell St', 4, TO_DATE('10/08/2024', 'DD/MM/YYYY'), TO_DATE('10/10/2024', 'DD/MM/YYYY'), 'Temporara');

INSERT INTO expozitie (cod_expozitie, nume_expozitie, nume_cladire, adresa, cod_locatie, data_inceput, data_sfarsit, tip_expozitie)
VALUES (5, 'Arhitectura Urbana', 'Galeria Urbana', 'Potsdamer Platz', 5, TO_DATE('05/09/2024', 'DD/MM/YYYY'), TO_DATE('05/10/2024', 'DD/MM/YYYY'), 'Itineranta');

INSERT INTO sala (cod_sala, cod_expozitie, nume_sala, suprafata, capacitate_maxima)
VALUES (1, 1, 'Sala Mare', 500, 200);

INSERT INTO sala (cod_sala, cod_expozitie, nume_sala, suprafata, capacitate_maxima)
VALUES (2, 1, 'Sala Mica', 200, 50);

INSERT INTO sala (cod_sala, cod_expozitie, nume_sala, suprafata, capacitate_maxima)
VALUES (3, 2, 'Sala Principala', 300, 150);

INSERT INTO sala (cod_sala, cod_expozitie, nume_sala, suprafata, capacitate_maxima)
VALUES (4, 3, 'Galeria Est', 400, 120);

INSERT INTO sala (cod_sala, cod_expozitie, nume_sala, suprafata, capacitate_maxima)
VALUES (5, 4, 'Sala Centrala', 600, 250);

INSERT INTO artist (cod_artist, nume, prenume, data_nasterii, nationalitate, cod_locatie)
VALUES (1, 'Popescu', 'Ion', TO_DATE('15/03/1980', 'DD/MM/YYYY'), 'Romana', 1);

INSERT INTO artist (cod_artist, nume, prenume, data_nasterii, nationalitate, cod_locatie)
VALUES (2, 'Ionescu', 'Maria', TO_DATE('25/06/1985', 'DD/MM/YYYY'), 'Romana', 2);

INSERT INTO artist (cod_artist, nume, prenume, data_nasterii, nationalitate, cod_locatie)
VALUES (3, 'Dupont', 'Jean', TO_DATE('10/11/1975', 'DD/MM/YYYY'), 'Franceza', 3);

INSERT INTO artist (cod_artist, nume, prenume, data_nasterii, nationalitate, cod_locatie)
VALUES (4, 'Smith', 'John', TO_DATE('12/01/1990', 'DD/MM/YYYY'), 'Britanica', 4);

INSERT INTO artist (cod_artist, nume, prenume, data_nasterii, nationalitate, cod_locatie)
VALUES (5, 'Müller', 'Hans', TO_DATE('22/07/1970', 'DD/MM/YYYY'), 'Germana', 5);

INSERT INTO fotografie (cod_fotografie, gen, titlu, valoare, data_crearii, dimensiuni, cod_artist, cod_sala)
VALUES (1, 'Peisaj', 'Apus de soare', 5000.00, TO_DATE('01/08/2018', 'DD/MM/YYYY'), '40x60 cm', 1, 1);

INSERT INTO fotografie (cod_fotografie, gen, titlu, valoare, data_crearii, dimensiuni, cod_artist, cod_sala)
VALUES (2, 'Portret', 'Portret de femeie', 12000.00, TO_DATE('15/05/2016', 'DD/MM/YYYY'), '50x70 cm', 2, 2);

INSERT INTO fotografie (cod_fotografie, gen, titlu, valoare, data_crearii, dimensiuni, cod_artist, cod_sala)
VALUES (3, 'Abstract', 'Forme geometrice', 7500.00, TO_DATE('22/03/2020', 'DD/MM/YYYY'), '30x40 cm', 3, 3);

INSERT INTO fotografie (cod_fotografie, gen, titlu, valoare, data_crearii, dimensiuni, cod_artist, cod_sala)
VALUES (4, 'Peisaj urban', 'Oras la apus', 8500.00, TO_DATE('30/09/2019', 'DD/MM/YYYY'), '60x90 cm', 4, 4);

INSERT INTO fotografie (cod_fotografie, gen, titlu, valoare, data_crearii, dimensiuni, cod_artist, cod_sala)
VALUES (5, 'Natura moarta', 'Fructe si vase', 3000.00, TO_DATE('12/11/2017', 'DD/MM/YYYY'), '35x45 cm', 5, 5);

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (1, 1, 'Participant principal', TO_DATE('01/09/2024', 'DD/MM/YYYY'),'Premiul de excelenta');

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (2, 1, 'Invitat',TO_DATE('05/09/2024', 'DD/MM/YYYY'), NULL);

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (3, 2, 'Curator',TO_DATE('15/10/2024', 'DD/MM/YYYY'), 'Premiul Curatorului');

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (4, 3, 'Participant principal',TO_DATE('01/07/2024', 'DD/MM/YYYY'), NULL);

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (5, 4, 'Invitat',TO_DATE('10/08/2024', 'DD/MM/YYYY'), 'Premiul Invitatului');

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (1, 2, 'Participant principal', TO_DATE('12/09/2024', 'DD/MM/YYYY'), NULL);

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (2, 3, 'Curator', TO_DATE('20/09/2024', 'DD/MM/YYYY'), 'Premiul Curatorului');

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (3, 4, 'Invitat', TO_DATE('05/10/2024', 'DD/MM/YYYY'), NULL);

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (4, 5, 'Participant principal', TO_DATE('08/10/2024', 'DD/MM/YYYY'), 'Premiul Excelentei');

INSERT INTO gazduit (cod_artist, cod_expozitie, rol_artist, data_participare, premiu)
VALUES (5, 1, 'Invitat', TO_DATE('25/09/2024', 'DD/MM/YYYY'), NULL);

COMMIT;

SELECT * FROM locatie;
SELECT * FROM artist;
SELECT * FROM fotografie;
SELECT * FROM sala;
SELECT * FROM expozitie;
SELECT * FROM gazduit
ORDER BY 1;

