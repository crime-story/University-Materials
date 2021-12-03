-- TEMA 4
-- Popescu Paullo Robertto Karloss Grupa 2311

-- Laborator 2 PL/SQL

-- Ex 2
-- creez un vector tip_orase_Robertto
CREATE OR REPLACE TYPE tip_orase_Robertto AS VARRAY(100) OF VARCHAR2(100);
/

-- creez tabelul excursie_Robertto
CREATE TABLE excursie_Robertto (cod_excursie    NUMBER(4) PRIMARY KEY,
                                denumire        VARCHAR2(20),
                                orase           tip_orase_Robertto,
                                status          VARCHAR2(20));

SELECT * FROM excursie_Robertto;

-- a) Inserați 5 înregistrări în tabel.
INSERT INTO excursie_Robertto VALUES (
    1,
    'La arabi',
    tip_orase_Robertto('Dubai', 'Abu Dhabi', 'Schardscha'),
    'OPEN'
);

INSERT INTO excursie_Robertto VALUES (
    2,
    'Pe o insula Pustie',
    tip_orase_robertto('Hawai', 'Ceva frumos', 'Takimi'),
    'CLOSED'
);

INSERT INTO excursie_Robertto VALUES (
    3,
    'La munte',
    tip_orase_Robertto('Brasov', 'Tusnad', 'Sinaia'),
    'OPEN'
);

INSERT INTO excursie_Robertto VALUES (
    4,
    'In Suedia',
    tip_orase_Robertto('Umea', 'Stockholm', 'Ystad'),
    'CLOSED'
);

INSERT INTO excursie_Robertto VALUES (
    5,
    'In Italia',
    tip_orase_Robertto('Milano', 'Roma', 'Bergamo'),
    'OPEN'
);

COMMIT;

SELECT * FROM excursie_Robertto;

-- b) Actualizați coloana orase pentru o excursie specificată:
-- b1) adăugați un oraș nou în listă, ce va fi ultimul vizitat în excursia respectivă;
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := '&cod_excursie';
    nume_oras               VARCHAR2(20) := '&nume_oras';
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    lista_orase.extend();
    lista_orase(lista_orase.last) := nume_oras;
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;
/

SELECT * FROM excursie_Robertto;

-- b2) adăugați un oraș nou în listă, ce va fi al doilea oraș vizitat în excursia respectivă;
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := '&cod_excursie';
    nume_oras               VARCHAR2(20) := '&nume_oras';
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    lista_orase.extend();
    lista_orase(lista_orase.last) := nume_oras;
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;
/

SELECT * FROM excursie_Robertto;

-- b3) inversați ordinea de vizitare a două dintre orașe al căror nume este specificat;
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := '&cod_excursie';
    nume_oras1              VARCHAR2(20) := '&nume_oras1';
    nume_oras2              VARCHAR2(20) := '&nume_oras2';
    i                       BINARY_INTEGER;
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    i := lista_orase.first();
    
    WHILE i <= lista_orase.LAST() LOOP
        IF LOWER(lista_orase(i)) = LOWER(nume_oras1) THEN
            lista_orase(i) := nume_oras2;
        ELSIF LOWER(lista_orase(i)) = LOWER(nume_oras2) THEN
            lista_orase(i) := nume_oras1;
        END IF;
        i := lista_orase.NEXT(i);
    END LOOP;
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;
/

SELECT * FROM excursie_Robertto;

-- b4) eliminați din listă un oraș al cărui nume este specificat. 
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := '&cod_excursie';
    nume_oras               VARCHAR2(20) := '&nume_oras';
    i                       BINARY_INTEGER;
    gasit                   BINARY_INTEGER := 0;
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;    
    i := lista_orase.FIRST();
    
    WHILE i <= lista_orase.LAST() LOOP
        IF LOWER(lista_orase(i)) = LOWER(nume_oras) THEN 
            gasit := 1;
        ELSIF gasit = 1 THEN 
            lista_orase(lista_orase.PRIOR(i)) := lista_orase(i);
        END IF; 
        i := lista_orase.NEXT(i);
    END LOOP;
    
    lista_orase.TRIM();
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;

SELECT * FROM excursie_Robertto;

-- c) Pentru o excursie al cărui cod este dat, afișați numărul de orașe vizitate, respectiv numele orașelor. 
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := '&cod_excursie';
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    DBMS_OUTPUT.PUT_LINE('Numarul de orase vizitate este: ' || lista_orase.count());
    
    FOR i IN lista_orase.FIRST()..lista_orase.LAST() LOOP
        DBMS_OUTPUT.PUT_LINE('Orasul cu numarul ' || i || ' este: ' ||  lista_orase(i));
    END LOOP;
END;
/

SELECT * FROM excursie_Robertto;

-- d) Pentru fiecare excursie afișați lista orașelor vizitate.
CREATE OR REPLACE TYPE lista_excursii_Robertto AS VARRAY(100) OF INTEGER;
/
DECLARE
    lista_orase         tip_orase_Robertto;
    lista_excursii      lista_excursii_Robertto;
BEGIN
    SELECT cod_excursie BULK COLLECT INTO lista_excursii
    FROM excursie_Robertto;
    
    FOR i IN lista_excursii.FIRST()..lista_excursii.LAST() LOOP
        SELECT orase INTO lista_orase
        FROM excursie_Robertto
        WHERE cod_excursie = i;
        
        DBMS_OUTPUT.PUT_LINE('Numarul de orase vizitate este: ' || lista_orase.count());
        
        FOR j IN lista_orase.FIRST()..lista_orase.LAST() LOOP
            DBMS_OUTPUT.PUT_LINE('Orasul cu numarul ' || j || ' este: ' || lista_orase(j));
        END LOOP;
    END LOOP;
END;
/

SELECT * FROM excursie_Robertto;

-- e) Anulați excursiile cu cele mai puține orașe vizitate.
CREATE OR REPLACE TYPE lista_excursii_Robertto AS VARRAY(100) OF INTEGER;
/
DECLARE
    lista_orase         tip_orase_Robertto;
    lista_excursii      lista_excursii_Robertto;
    v_min_viz           INTEGER := 1000000;
BEGIN
    SELECT cod_excursie BULK COLLECT INTO lista_excursii
    FROM excursie_Robertto;
    
    FOR i IN lista_excursii.FIRST() .. lista_excursii.LAST() LOOP
        SELECT orase INTO lista_orase
        FROM excursie_Robertto
        WHERE cod_excursie = i;
        
        IF lista_orase.COUNT() < v_min_viz THEN
            v_min_viz := lista_orase.COUNT();
        END IF;
    END LOOP;
    
    FOR i IN lista_excursii.FIRST() .. lista_excursii.LAST() LOOP
        SELECT orase INTO lista_orase
        FROM excursie_Robertto
        WHERE cod_excursie = i;
        
        IF lista_orase.COUNT() = v_min_viz THEN
            UPDATE excursie_Robertto
            SET status = 'CLOSED'
            WHERE cod_excursie = i;
            COMMIT;
        END IF;
    END LOOP;
END;
/

SELECT * FROM excursie_Robertto;

-- Ex 3
-- VARIANTA 1
DROP TABLE excursie_Robertto;

CREATE OR REPLACE TYPE tip_orase_Robertto AS TABLE OF VARCHAR2(100);
/ 
CREATE TABLE excursie_Robertto (cod_excursie    NUMBER(4) PRIMARY KEY,
                                denumire        VARCHAR2(20),
                                orase           tip_orase_Robertto,
                                status          VARCHAR2(20)) NESTED TABLE orase STORE AS oras;
                                
SELECT * FROM excursie_Robertto;

-- a) Inserați 5 înregistrări în tabel.
INSERT INTO excursie_Robertto VALUES (
    1,
    'La arabi',
    tip_orase_Robertto('Dubai', 'Abu Dhabi', 'Schardscha'),
    'OPEN'
);

INSERT INTO excursie_Robertto VALUES (
    2,
    'Pe o insula Pustie',
    tip_orase_robertto('Hawai', 'Ceva frumos', 'Takimi'),
    'CLOSED'
);

INSERT INTO excursie_Robertto VALUES (
    3,
    'La munte',
    tip_orase_Robertto('Brasov', 'Tusnad', 'Sinaia'),
    'OPEN'
);

INSERT INTO excursie_Robertto VALUES (
    4,
    'In Suedia',
    tip_orase_Robertto('Umea', 'Stockholm', 'Ystad'),
    'CLOSED'
);

INSERT INTO excursie_Robertto VALUES (
    5,
    'In Italia',
    tip_orase_Robertto('Milano', 'Roma', 'Bergamo'),
    'OPEN'
);

COMMIT;

SELECT * FROM excursie_Robertto;

-- b) Actualizați coloana orase pentru o excursie specificată:
-- b1) adăugați un oraș nou în listă, ce va fi ultimul vizitat în excursia respectivă;
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := &cod_excursie;
    nume_oras               VARCHAR2(20) := '&nume_oras';
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    lista_orase.extend();
    lista_orase(lista_orase.LAST) := nume_oras;
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;
/

SELECT * FROM excursie_Robertto;

-- b2) adăugați un oraș nou în listă, ce va fi al doilea oraș vizitat în excursia respectivă;
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := &cod_excursie;
    nume_oras               VARCHAR2(20) := '&nume_oras';
    i                       BINARY_INTEGER;
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    lista_orase.extend();
    i := lista_orase.LAST();
    
    WHILE i <> lista_orase.FIRST LOOP
        lista_orase(i) := lista_orase(lista_orase.PRIOR(i));
        i := lista_orase.PRIOR(i);
    END LOOP;
    
    lista_orase(lista_orase.NEXT(lista_orase.FIRST())) := nume_oras;
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;
/

SELECT * FROM excursie_Robertto;

-- b3) inversați ordinea de vizitare a două dintre orașe al căror nume este specificat;
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := &cod_excursie;
    nume_oras1              VARCHAR2(20) := '&nume_oras1';
    nume_oras2              VARCHAR2(20) := '&nume_oras2';
    i                       BINARY_INTEGER;
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    i := lista_orase.FIRST();
    
    WHILE i <= lista_orase.LAST() LOOP
        IF lista_orase(i) = nume_oras1 THEN
            lista_orase(i) := nume_oras2;
        ELSIF lista_orase(i) = nume_oras2 THEN
            lista_orase(i) := nume_oras1;
        END IF;
        i := lista_orase.NEXT(i);
    END LOOP;
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;
/

SELECT * FROM excursie_Robertto;

-- b4) eliminați din listă un oraș al cărui nume este specificat. 
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := &cod_excursie;
    nume_oras               VARCHAR2(20) := '&nume_oras1';
    i                       BINARY_INTEGER;
    gasit                   BINARY_INTEGER := 0;
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    i := lista_orase.FIRST();
    
    WHILE i <= lista_orase.LAST() LOOP
        IF LOWER(lista_orase(i)) = LOWER(nume_oras) THEN
            gasit := 1;
        ELSIF gasit = 1 THEN
            lista_orase(lista_orase.PRIOR(i)) := lista_orase(i);
        END IF;
        i := lista_orase.NEXT(i);
    END LOOP;
    
    lista_orase.TRIM();
    
    UPDATE excursie_Robertto
    SET orase = lista_orase
    WHERE cod_excursie = input_cod_excursie;
    COMMIT;
END;
/

SELECT * FROM excursie_Robertto;

-- c) Pentru o excursie al cărui cod este dat, afișați numărul de orașe vizitate, respectiv numele orașelor.
DECLARE
    lista_orase             tip_orase_Robertto;
    input_cod_excursie      NUMBER(4) := &cod_excursie;
BEGIN
    SELECT orase INTO lista_orase
    FROM excursie_Robertto
    WHERE cod_excursie = input_cod_excursie;
        
    DBMS_OUTPUT.PUT_LINE('Numarul de orase vizitate este: ' || lista_orase.count());
    
    FOR i IN lista_orase.FIRST() .. lista_orase.LAST() LOOP
        DBMS_OUTPUT.PUT_LINE('Orasul cu numarul ' || i || ' este: ' ||  lista_orase(i));
    END LOOP;
END;
/

SELECT * FROM excursie_Robertto;

-- d) Pentru fiecare excursie afișați lista orașelor vizitate.
CREATE OR REPLACE TYPE lista_excursii_Robertto AS VARRAY(100) OF INTEGER;
/
DECLARE
    lista_orase     tip_orase_Robertto;
    lista_excursii  lista_excursii_Robertto;
BEGIN
    SELECT cod_excursie BULK COLLECT INTO lista_excursii
    FROM excursie_Robertto;
    
    FOR i IN lista_excursii.FIRST() .. lista_excursii.LAST() LOOP
        SELECT orase INTO lista_orase
        FROM excursie_Robertto
        WHERE cod_excursie = i;
        
        DBMS_OUTPUT.PUT_LINE('Numarul de orase vizitate este: ' || lista_orase.count());
        
        FOR j IN lista_orase.FIRST() .. lista_orase.LAST() LOOP
            DBMS_OUTPUT.PUT_LINE('Orasul cu numarul ' || j || ' este: ' || lista_orase(j));
        END LOOP;
    END LOOP;
END;
/

SELECT * FROM excursie_Robertto;

-- e) Anulați excursiile cu cele mai puține orașe vizitate.
CREATE OR REPLACE TYPE lista_excursii_Robertto AS VARRAY(100) OF INTEGER;
/
DECLARE
    lista_orase         tip_orase_Robertto;
    lista_excursii      lista_excursii_Robertto;
    v_min_viz           INTEGER := 1000000;
BEGIN
    SELECT cod_excursie BULK COLLECT INTO lista_excursii
    FROM excursie_Robertto;
    
    FOR i IN lista_excursii.FIRST()..lista_excursii.LAST() LOOP
        SELECT orase INTO lista_orase
        FROM excursie_Robertto
        WHERE cod_excursie = i;
        
        IF lista_orase.COUNT() < v_min_viz THEN
            v_min_viz := lista_orase.COUNT();
        END IF;
    END LOOP;
    
    FOR i IN lista_excursii.FIRST()..lista_excursii.LAST() LOOP
        SELECT orase INTO lista_orase
        FROM excursie_Robertto
        WHERE cod_excursie = i;
        
        IF lista_orase.COUNT() = v_min_viz THEN
            UPDATE excursie_Robertto
            SET status = 'CLOSED'
            WHERE cod_excursie = i;
            COMMIT;
        END IF;
    END LOOP;
END;
/

SELECT * FROM excursie_Robertto;

-- VARIANTA 2 CU OPERATORI MULTISET (FOLOSESC DOAR SQL!)
DROP TABLE excursie_Robertto CASCADE CONSTRAINTS;

CREATE OR REPLACE TYPE tip_orase_Robertto IS TABLE OF VARCHAR2(100);
/
CREATE TABLE excursie_Robertto (cod_excursie    NUMBER(4) PRIMARY KEY,
                                denumire        VARCHAR2(20),
                                orase           tip_orase_Robertto,
                                status          VARCHAR2(20)) NESTED TABLE orase STORE AS tabb_imb_orase;

SELECT * FROM excursie_Robertto;

--a) Inserați 5 înregistrări în tabel.
INSERT INTO excursie_Robertto VALUES (
    1,
    'La arabi',
    tip_orase_Robertto('Dubai', 'Abu Dhabi', 'Schardscha'),
    'OPEN'
);

INSERT INTO excursie_Robertto VALUES (
    2,
    'Pe o insula Pustie',
    tip_orase_robertto('Hawai', 'Ceva frumos', 'Takimi'),
    'CLOSED'
);

INSERT INTO excursie_Robertto VALUES (
    3,
    'La munte',
    tip_orase_Robertto('Brasov', 'Tusnad', 'Sinaia'),
    'OPEN'
);

INSERT INTO excursie_Robertto VALUES (
    4,
    'In Suedia',
    tip_orase_Robertto('Umea', 'Stockholm', 'Ystad'),
    'CLOSED'
);

INSERT INTO excursie_Robertto VALUES (
    5,
    'In Italia',
    tip_orase_Robertto('Milano', 'Roma', 'Bergamo'),
    'OPEN'
);

COMMIT;

SELECT * FROM excursie_Robertto;

--b) Actualizați coloana orase pentru o excursie specificată:
-- b1) adăugați un oraș nou în listă, ce va fi ultimul vizitat în excursia respectivă;

INSERT INTO TABLE  (SELECT orase
                    FROM excursie_Robertto
                    WHERE cod_excursie = 1)
VALUES('Habibi');

COMMIT;

SELECT * FROM excursie_Robertto;

-- b2) adăugați un oraș nou în listă, ce va fi al doilea oraș vizitat în excursia respectivă;
CREATE TABLE aux_imb (ID NUMBER(10), nume_oras VARCHAR2(100));

INSERT INTO aux_imb VALUES (1,(SELECT * FROM TABLE (SELECT orase
                                                    FROM excursie_Robertto
                                                    WHERE cod_excursie = 2)
                                WHERE ROWNUM = 1));
INSERT INTO aux_imb  VALUES  (2, 'Vulcanos');

UPDATE excursie_Robertto
SET orase = (SELECT CAST (COLLECT(nume_oras) AS tip_orase_Robertto)
             FROM aux_imb) MULTISET UNION  DISTINCT (SELECT orase 
                                                     FROM excursie_Robertto 
                                                     WHERE cod_excursie = 2)
WHERE cod_excursie = 2;
/

COMMIT;

SELECT * FROM excursie_Robertto;

--b3) - inversați ordinea de vizitare a două dintre orașe al căror nume este specificat;

UPDATE TABLE (SELECT orase 
             FROM excursie_Robertto 
             WHERE cod_excursie = 3) a
SET VALUE(a) =  CASE
                    WHEN COLUMN_VALUE = 'Brasov' THEN 'Tusnad'
                    WHEN COLUMN_VALUE = 'Tusnad' THEN 'Brasov'
                    WHEN COLUMN_VALUE <> 'Brasov' or  COLUMN_VALUE <> 'Tusnad' THEN COLUMN_VALUE
                END;

COMMIT;

SELECT * FROM excursie_Robertto;

--b4) - eliminați din listă un oraș al cărui nume este specificat. 
DELETE FROM TABLE (SELECT orase
                   FROM excursie_Robertto
                   WHERE cod_excursie = 4) a
WHERE COLUMN_VALUE = 'Umea';

COMMIT;

SELECT * FROM excursie_Robertto;

--c) Pentru o excursie al cărui cod este dat, afișați numărul de orașe vizitate, respectiv numele orașelor. 
SELECT cardinality(orase) Numar_orase_vizitate, orase
FROM excursie_Robertto
WHERE cod_excursie = 4;

--d) Pentru fiecare excursie afișați lista orașelor vizitate.
SELECT denumire, orase
FROM excursie_Robertto;

--e) Anulați excursiile cu cele mai puține orașe vizitate.
UPDATE excursie_Robertto
SET status = 'CLOSED'
WHERE cardinality(orase) =  (SELECT MIN(Numar) 
                             FROM (SELECT cardinality(orase) Numar, orase 
                                   FROM excursie_Robertto));
COMMIT;

SELECT * FROM excursie_Robertto;