-- Exercitiul 6
-- Produsele preferate pentru un client citit de la tastatura
-- Se afiseaza numele si prenumele clientului, produsele preferate si de cate ori le-a comandat.

CREATE OR REPLACE TYPE tab_imb IS
    TABLE OF NUMBER(10);
/

CREATE OR REPLACE FUNCTION afis_produse_preferate (my_id_client IN client.id_client%TYPE)
RETURN tab_imb IS
    TYPE tab_ind IS TABLE OF PLS_INTEGER INDEX BY PLS_INTEGER;
    v_contor                    tab_ind;
    v_index_max                 PLS_INTEGER;
    id                          PLS_INTEGER;
    v_id_feluri_preferate       tab_imb;
    ok                          BINARY_INTEGER := 0;
BEGIN
    v_id_feluri_preferate := tab_imb();
    FOR i IN (SELECT p.id_produs -- CURSOR IMPLICIT
              FROM produs p, continut_comanda cont, comanda cmd
              WHERE my_id_client = cmd.id_client
              AND cmd.id_comanda = cont.id_comanda
              AND cont.id_produs = p.id_produs) LOOP BEGIN
        v_contor(i.id_produs) := v_contor(i.id_produs) + 1; -- simulez un vector de frecventa, unde tin de cate ori a fost comandat produsul respectiv
        IF ok = 1 THEN
            IF v_contor(i.id_produs) > v_contor(v_index_max) THEN
                v_index_max := i.id_produs;
            END IF;
        END IF;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN -- intr-un tabel indexat daca nu exista indexul respectiv
        -- se arunca exceptia no data found
            v_contor(i.id_produs) := 1; -- initializam numarul de aparitii cu 1
            IF ok = 0 THEN -- daca e primul produs selectat de cursor
                ok := 1;
                v_index_max := i.id_produs;
            END IF;
    END;
    END LOOP;
    IF ok = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Acest client nu a comandat nimic in viata lui');
    ELSE
        id := v_contor.first; -- prima valoare din tabelul indexat (vector frecventa)
        LOOP
            EXIT WHEN id IS NULL;
            IF v_contor(id) = v_contor(v_index_max) THEN
                v_id_feluri_preferate.extend;
                v_id_feluri_preferate(v_id_feluri_preferate.LAST) := id;
            END IF;
            id := v_contor.NEXT(id);
        END LOOP;
    END IF;
    RETURN v_id_feluri_preferate;
END;
/

DECLARE
    v_id_client_citit       client.id_client%TYPE := &client_id;
    my_tab                  tab_imb;
    id                      PLS_INTEGER;
    v_nume_produs           produs.nume%TYPE;
    v_nume                  client.nume%TYPE;
    v_prenume               client.prenume%TYPE;
    v_contor                PLS_INTEGER := 0;
BEGIN
    my_tab := afis_produse_preferate(v_id_client_citit);
    SELECT c.nume, c.prenume INTO v_nume, v_prenume
    FROM client c
    WHERE c.id_client = v_id_client_citit;
    DBMS_OUTPUT.PUT_LINE('Clientul ' || v_nume || ' ' || v_prenume || ' are urmatoarele produse favorite:');
    id := my_tab.FIRST; -- prima valoare din tabelul indexat (vector frecventa)
    LOOP
        EXIT WHEN id IS NULL;
        SELECT p.nume INTO v_nume_produs
        FROM produs p
        WHERE p.id_produs = my_tab(id);
        -- afisam produsele cu numarul maxim de aparitii
        v_contor := v_contor + 1;
        DBMS_OUTPUT.PUT_LINE(v_contor || '. ' || v_nume_produs);
        id := my_tab.NEXT(id);
    END LOOP;
END;

-- Exercitiul 7
-- Sa se afiseze pentru fiecare bucatar numele si prenumele acestuia, daca a preparat vreun produs de cand s-a angajat.
-- Daca a preparat minim un produs se va afisa date corespunzatoare legate de acesta (numele produsului, descrierea acestuia si
-- cat a durat sa il prepare)

CREATE OR REPLACE PROCEDURE afis_durata_preparare_produs
IS TYPE refcursor IS REF CURSOR;
    CURSOR c IS SELECT a.id_angajat, a.nume, a.prenume, b.nr_stele, 
                CURSOR (SELECT prep.durata, p.nume, p.descriere, p.id_produs
                         FROM preparare prep, produs p
                         WHERE b.id_angajat = prep.id_angajat
                         AND p.id_produs = prep.id_produs)
                FROM angajat a, bucatar b
                WHERE a.id_angajat = b.id_angajat;
    TYPE rec IS RECORD (id_ang angajat.id_angajat%TYPE, dur preparare.durata%TYPE);
    TYPE tab_ind IS TABLE OF rec INDEX BY PLS_INTEGER;
    t                           tab_ind;
    v_cursor                    refcursor;
    v_id_bucatar                angajat.id_angajat%TYPE;
    v_nume_bucatar              angajat.nume%TYPE;
    v_prenume_bucatar           angajat.prenume%TYPE;
    v_nr_stele_bucatar          bucatar.nr_stele%TYPE;
    v_durata_preparare          preparare.durata%TYPE;
    v_id_produs                 produs.id_produs%TYPE;
    v_nume_produs               produs.nume%TYPE;
    v_descriere_produs          produs.descriere%TYPE;
    v_contor                    PLS_INTEGER := 0;
    v_nume_bucatar_best         angajat.nume%TYPE;
    v_prenume_bucatar_best      angajat.prenume%TYPE;
    v_nr_stele_bucatar_best     bucatar.nr_stele%TYPE;
BEGIN
    OPEN c;
    LOOP
        FETCH c INTO v_id_bucatar, v_nume_bucatar, v_prenume_bucatar, v_nr_stele_bucatar, v_cursor;
        EXIT WHEN c%NOTFOUND;
        FETCH v_cursor INTO v_durata_preparare, v_nume_produs, v_descriere_produs, v_id_produs;
        IF v_cursor%NOTFOUND THEN
            DBMS_OUTPUT.PUT_LINE('ATENTIE! Bucatarul ' || v_nume_bucatar || ' ' || v_prenume_bucatar || ' nu a preparat niciodata un produs!');
            DBMS_OUTPUT.NEW_LINE();
        ELSE
            DBMS_OUTPUT.PUT_LINE('Bucatarul ' || v_nume_bucatar || ' ' || v_prenume_bucatar || ' a preparat urmatoarele produse:');
            LOOP
                EXIT WHEN v_cursor%NOTFOUND;
                DBMS_OUTPUT.PUT_LINE(v_cursor%ROWCOUNT || '. produsul: ' || v_nume_produs || ' cu descrierea '  || v_descriere_produs || ' a durat ' || v_durata_preparare || ' minute.');
                IF t.EXISTS (v_id_produs) THEN
                    IF v_durata_preparare < t(v_id_produs).dur THEN
                        t(v_id_produs) := rec(v_id_bucatar, v_durata_preparare);
                    END IF;
                ELSE
                    t(v_id_produs) := rec(v_id_bucatar, v_durata_preparare);
                END IF;
                FETCH v_cursor INTO v_durata_preparare, v_nume_produs, v_descriere_produs, v_id_produs;
            END LOOP;
        END IF;
    END LOOP;
    CLOSE c;
    FOR i IN t.FIRST..t.LAST LOOP
        SELECT nume INTO v_nume_produs
        FROM produs
        WHERE id_produs = i;
        
        SELECT a.nume, a.prenume INTO v_nume_bucatar, v_prenume_bucatar
        FROM angajat a, bucatar b
        WHERE a.id_angajat = t(i).id_ang
        AND a.id_angajat =  b.id_angajat;
        DBMS_OUTPUT.PUT_LINE('Bucatarul ' || v_nume_bucatar || ' ' || v_prenume_bucatar || ' a preparat produsul ' || v_nume_produs || ' in ' || t(i).dur || ' minute.');
    END LOOP;
END;
/

BEGIN
    afis_durata_preparare_produs();
END;
/

-- Exercitiul 8
-- Sa se afiseze profitul generat de un client citit de la tastatura, de-a lungul timpului,
-- Daca aceasta a beneficiat de reduceri de-a lungul timpului se va afisa valoarea totala a acestor reduceri.
-- Daca profitul adus de-a lungul timpului este unul negativ inseamna ca clientul a beneficiat de prea multe reduceri
-- si se va afisa un mesaj corespunzator.
-- De asemenea se va afisa  profitul maxim oferit de un client si profitul obtinut de companie de-a lungul timpului.

CREATE OR REPLACE FUNCTION profit_client (my_id_client IN client.id_client%TYPE)
RETURN PLS_INTEGER IS
    TYPE tab_ind IS TABLE OF PLS_INTEGER INDEX BY PLS_INTEGER;
    v_nume_client               client.nume%TYPE;
    v_prenume_client            client.nume%TYPE;
    v_nr_telefon_client         client.nr_telefon%TYPE;
    v_profit_maxim_companie     PLS_INTEGER := 0;
    v_profit_maxim_client       PLS_INTEGER := 0;
    v_benef_red                 BINARY_INTEGER :=0;
    v_valoare_red               PLS_INTEGER :=0;
    invalid                     EXCEPTION;
    v_exista                    PLS_INTEGER := 0;
    t                           tab_ind;
BEGIN
    SELECT COUNT(*) INTO v_exista
    FROM client
    WHERE id_client = my_id_client;
    IF v_exista = 0 THEN
        RAISE invalid;
    END IF;
    
    SELECT nume, prenume, nr_telefon INTO v_nume_client, v_prenume_client, v_nr_telefon_client
    FROM client
    WHERE id_client = my_id_client;
    
    FOR i IN (SELECT cli.id_client, cli.nume, cli.prenume, cmd.pret, f.valoare, f.detalii 
              FROM client cli, comanda cmd, factura f
              WHERE cli.id_client = cmd.id_client 
              AND cmd.id_factura = f.id_factura) LOOP
        BEGIN
            IF i.id_client = my_id_client AND i.valoare < i.pret THEN
                v_benef_red := 1;
                v_valoare_red := v_valoare_red + (i.pret - i.valoare);
            ELSIF i.id_client = my_id_client THEN
                t(i.id_client) := t(i.id_client) + (i.valoare - i.pret);
            END IF;
            v_profit_maxim_companie  := v_profit_maxim_companie  + (i.valoare - i.pret);
            t(i.id_client) := t(i.id_client) + (i.valoare - i.pret);
        EXCEPTION -- pentru prima inserare in tabel
            WHEN NO_DATA_FOUND THEN
                t(i.id_client) := i.valoare - i.pret;
        END;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('Profitul obtinut in urma clientilor de pana acum este de ' || v_profit_maxim_companie || ' lei.');
    FOR i IN 1..t.LAST LOOP
        IF  t(i) > v_profit_maxim_client THEN
            v_profit_maxim_client := t(i);
        END IF;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('Profitul maxim adus de un client este de ' || v_profit_maxim_client || ' lei.');
    DBMS_OUTPUT.PUT_LINE('Clientul cu id-ul ' || my_id_client || ' este ' || v_nume_client || ' ' || v_prenume_client || ' cu nr de telefon: ' || v_nr_telefon_client || '.');
    IF v_benef_red = 1 THEN
        DBMS_OUTPUT.PUT_LINE('A beneficiat de reduceri in valoare de ' || v_valoare_red || ' lei.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Nu beneficiaza de reduceri!');
    END IF;
    RETURN t(my_id_client);
EXCEPTION
    WHEN invalid THEN
        RAISE_APPLICATION_ERROR(-20021, 'Nu a fost gasit niciun client cu id-ul introdus!');
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20022, 'Alta eroare!');
END;
/

DECLARE
    v_id_client_citit       client.id_client%TYPE := &client_id;
    v_rezultat              PLS_INTEGER;
    
BEGIN
    v_rezultat := profit_client(v_id_client_citit);
    IF v_rezultat > 0 THEN
        DBMS_OUTPUT.PUT_LINE('El a contribuit la profitul companiei pana acum cu ' || v_rezultat || ' lei.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Acesta a beneficiat de prea multe reduceri! Aducand un profit negativ companiei: ' || v_rezultat || ' lei.');
    END IF;
END;
/

-- Exercitiul 9
-- Sa se afiseze ce cantitate ingrediente contine fiecare produs, din fiecare comanda,
-- furnizate de un producator citit de la tastatura.

CREATE OR REPLACE PROCEDURE mai_multe_ingrediente_producator(my_id_comanda IN comanda.id_comanda%TYPE, my_id_produs IN produs.id_produs%TYPE, my_id_producator IN producator.id_producator%TYPE, my_numar_produse_comanda continut_comanda.numar_produse%TYPE)
IS
    TYPE rec IS RECORD (v_cantitate_produs cantitate_produs.cantitate%TYPE, v_nume_ingredient ingredient.nume%TYPE);
    TYPE tab_ind IS TABLE OF rec INDEX BY PLS_INTEGER;
    t   tab_ind;
BEGIN
    SELECT cnt.cantitate, i.nume BULK COLLECT INTO t
    FROM cantitate_produs cnt, ingredient i, producator prod, produs p, continut_comanda cmd
    WHERE cmd.id_comanda = my_id_comanda
    AND cmd.id_produs = my_id_produs
    AND p.id_produs = cmd.id_produs
    AND cnt.id_produs = p.id_produs
    AND i.id_ingredient = cnt.id_ingredient
    AND prod.id_producator = i.id_producator
    AND my_id_producator = prod.id_producator;
    FOR i IN 1..t.LAST LOOP
        DBMS_OUTPUT.PUT_LINE('-> ' || my_numar_produse_comanda*t(i).v_cantitate_produs || 'g de ' ||t(i).v_nume_ingredient || '.');
    END LOOP;
END;
/

CREATE OR REPLACE PROCEDURE afis_cantitate_ingredient_producator_produs(my_id_producator IN producator.id_producator%TYPE) IS
    CURSOR mycursor IS (SELECT p.id_produs, p.nume, cmd.numar_produse, cmd.id_comanda
                        FROM produs p, continut_comanda cmd
                        WHERE p.id_produs=cmd.id_produs);
    TYPE tab_ind IS TABLE OF ingredient.nume%TYPE INDEX BY PLS_INTEGER;
    v_id_produs                         produs.id_produs%TYPE;
    v_id_comanda                        continut_comanda.id_comanda%TYPE;
    v_nume_produs                       produs.nume%TYPE;
    v_numar_produse                     continut_comanda.numar_produse%TYPE;
    v_cantitate_produs                  cantitate_produs.cantitate%TYPE;
    v_nume_ingredient                   ingredient.nume%TYPE;
    v_nume_producator                   producator.nume%TYPE;
    v_nr_telefon_producator             producator.nr_telefon%TYPE;
    lista_ingrediente_producator        tab_ind;
BEGIN
    SELECT nume, nr_telefon INTO v_nume_producator, v_nr_telefon_producator
    FROM producator
    WHERE id_producator = my_id_producator;
    SELECT nume BULK COLLECT INTO lista_ingrediente_producator
    FROM ingredient
    WHERE id_producator = my_id_producator;
    DBMS_OUTPUT.PUT_LINE('Numele producatorului selectat este ' || v_nume_producator || '.');
    DBMS_OUTPUT.PUT_LINE('El poate fi contactat la nurmarul de telefon: ' || v_nr_telefon_producator || '.');
    DBMS_OUTPUT.PUT_LINE('Acesta produce urmatoarele ingrediente:');
    FOR i IN 1..lista_ingrediente_producator.LAST LOOP
        DBMS_OUTPUT.PUT_LINE('-> ' || lista_ingrediente_producator(i) || ';');
    END LOOP;
    DBMS_OUTPUT.NEW_LINE();
    OPEN mycursor;
    LOOP
        FETCH mycursor INTO v_id_produs,v_nume_produs, v_numar_produse, v_id_comanda;
        EXIT WHEN mycursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('-- ' || v_nume_produs || ' are de la producatorul ' || v_nume_producator || ' in comanda cu id-ul ' || v_id_comanda || ' urmatorul ingredient:');
        BEGIN
            SELECT cnt.cantitate, i.nume INTO v_cantitate_produs, v_nume_ingredient
            FROM cantitate_produs cnt, ingredient i, producator prod, produs p, continut_comanda cmd
            WHERE cmd.id_comanda = v_id_comanda
            AND cmd.id_produs = v_id_produs
            AND p.id_produs = cmd.id_produs
            AND cnt.id_produs = p.id_produs
            AND i.id_ingredient = cnt.id_ingredient
            AND prod.id_producator = i.id_producator
            AND my_id_producator = prod.id_producator;
            DBMS_OUTPUT.PUT_LINE('-> ' || v_numar_produse*v_cantitate_produs || 'g de ' || v_nume_ingredient || '.');
            DBMS_OUTPUT.NEW_LINE();
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                DBMS_OUTPUT.PUT_LINE('ATENTIE! Nu exista niciun ingredient al acestui producator in acest preparat!');
                DBMS_OUTPUT.NEW_LINE();
            WHEN TOO_MANY_ROWS THEN
                DBMS_OUTPUT.PUT_LINE('ATENTIE! Mai multe ingrediente ale acestui producator detectate in acest preparat!');
                BEGIN
                    mai_multe_ingrediente_producator(v_id_comanda, v_id_produs, my_id_producator, v_numar_produse);
                END;
                DBMS_OUTPUT.NEW_LINE();
        END;
    END LOOP;
    CLOSE mycursor;
END;
/

DECLARE 
    v_id_producator producator.id_producator%TYPE := &producator_id;
BEGIN
    afis_cantitate_ingredient_producator_produs(v_id_producator);
END;
/

-- Exercitiul 13
CREATE OR REPLACE PACKAGE pachet_cerinte AS
--    TYPE tab_imb IS TABLE OF NUMBER(10);
    -- Exercitiul 6
    FUNCTION afis_produse_preferate (my_id_client IN client.id_client%TYPE) RETURN tab_imb;
    -- Exercitiul 7
    PROCEDURE afis_durata_preparare_produs;
    -- Exercitiul 8
    FUNCTION profit_client (my_id_client IN client.id_client%TYPE) RETURN PLS_INTEGER;
    -- Exercitiul 9
    PROCEDURE mai_multe_ingrediente_producator(my_id_comanda IN comanda.id_comanda%TYPE, my_id_produs IN produs.id_produs%TYPE, my_id_producator IN producator.id_producator%TYPE, my_numar_produse_comanda continut_comanda.numar_produse%TYPE);
    PROCEDURE afis_cantitate_ingredient_producator_produs(my_id_producator IN producator.id_producator%TYPE);
END pachet_cerinte;
/

CREATE OR REPLACE PACKAGE BODY pachet_cerinte AS
    TYPE tab_imb IS TABLE OF NUMBER(10);
    FUNCTION afis_produse_preferate (my_id_client IN client.id_client%TYPE)
    RETURN tab_imb IS
        TYPE tab_ind IS TABLE OF PLS_INTEGER INDEX BY PLS_INTEGER;
        v_contor                    tab_ind;
        v_index_max                 PLS_INTEGER;
        id                          PLS_INTEGER;
        v_id_feluri_preferate       tab_imb;
        ok                          BINARY_INTEGER := 0;
    BEGIN
        v_id_feluri_preferate := tab_imb();
        FOR i IN (SELECT p.id_produs -- CURSOR IMPLICIT
                  FROM produs p, continut_comanda cont, comanda cmd
                  WHERE my_id_client = cmd.id_client
                  AND cmd.id_comanda = cont.id_comanda
                  AND cont.id_produs = p.id_produs) LOOP BEGIN
            v_contor(i.id_produs) := v_contor(i.id_produs) + 1; -- simulez un vector de frecventa, unde tin de cate ori a fost comandat produsul respectiv
            IF ok = 1 THEN
                IF v_contor(i.id_produs) > v_contor(v_index_max) THEN
                    v_index_max := i.id_produs;
                END IF;
            END IF;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN -- intr-un tabel indexat daca nu exista indexul respectiv
            -- se arunca exceptia no data found
                v_contor(i.id_produs) := 1; -- initializam numarul de aparitii cu 1
                IF ok = 0 THEN -- daca e primul produs selectat de cursor
                    ok := 1;
                    v_index_max := i.id_produs;
                END IF;
        END;
        END LOOP;
        IF ok = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Acest client nu a comandat nimic in viata lui');
        ELSE
            id := v_contor.first; -- prima valoare din tabelul indexat (vector frecventa)
            LOOP
                EXIT WHEN id IS NULL;
                IF v_contor(id) = v_contor(v_index_max) THEN
                    v_id_feluri_preferate.extend;
                    v_id_feluri_preferate(v_id_feluri_preferate.LAST) := id;
                END IF;
                id := v_contor.NEXT(id);
            END LOOP;
        END IF;
        RETURN v_id_feluri_preferate;
    END afis_produse_preferate;
    
    PROCEDURE afis_durata_preparare_produs
    IS TYPE refcursor IS REF CURSOR;
        CURSOR c IS SELECT a.id_angajat, a.nume, a.prenume, b.nr_stele, 
                    CURSOR (SELECT prep.durata, p.nume, p.descriere, p.id_produs
                             FROM preparare prep, produs p
                             WHERE b.id_angajat = prep.id_angajat
                             AND p.id_produs = prep.id_produs)
                    FROM angajat a, bucatar b
                    WHERE a.id_angajat = b.id_angajat;
        TYPE rec IS RECORD (id_ang angajat.id_angajat%TYPE, dur preparare.durata%TYPE);
        TYPE tab_ind IS TABLE OF rec INDEX BY PLS_INTEGER;
        t                           tab_ind;
        v_cursor                    refcursor;
        v_id_bucatar                angajat.id_angajat%TYPE;
        v_nume_bucatar              angajat.nume%TYPE;
        v_prenume_bucatar           angajat.prenume%TYPE;
        v_nr_stele_bucatar          bucatar.nr_stele%TYPE;
        v_durata_preparare          preparare.durata%TYPE;
        v_id_produs                 produs.id_produs%TYPE;
        v_nume_produs               produs.nume%TYPE;
        v_descriere_produs          produs.descriere%TYPE;
        v_contor                    PLS_INTEGER := 0;
        v_nume_bucatar_best         angajat.nume%TYPE;
        v_prenume_bucatar_best      angajat.prenume%TYPE;
        v_nr_stele_bucatar_best     bucatar.nr_stele%TYPE;
    BEGIN
        OPEN c;
        LOOP
            FETCH c INTO v_id_bucatar, v_nume_bucatar, v_prenume_bucatar, v_nr_stele_bucatar, v_cursor;
            EXIT WHEN c%NOTFOUND;
            FETCH v_cursor INTO v_durata_preparare, v_nume_produs, v_descriere_produs, v_id_produs;
            IF v_cursor%NOTFOUND THEN
                DBMS_OUTPUT.PUT_LINE('ATENTIE! Bucatarul ' || v_nume_bucatar || ' ' || v_prenume_bucatar || ' nu a preparat niciodata un produs!');
                DBMS_OUTPUT.NEW_LINE();
            ELSE
                DBMS_OUTPUT.PUT_LINE('Bucatarul ' || v_nume_bucatar || ' ' || v_prenume_bucatar || ' a preparat urmatoarele produse:');
                LOOP
                    EXIT WHEN v_cursor%NOTFOUND;
                    DBMS_OUTPUT.PUT_LINE(v_cursor%ROWCOUNT || '. produsul: ' || v_nume_produs || ' cu descrierea '  || v_descriere_produs || ' a durat ' || v_durata_preparare || ' minute.');
                    IF t.EXISTS (v_id_produs) THEN
                        IF v_durata_preparare < t(v_id_produs).dur THEN
                            t(v_id_produs) := rec(v_id_bucatar, v_durata_preparare);
                        END IF;
                    ELSE
                        t(v_id_produs) := rec(v_id_bucatar, v_durata_preparare);
                    END IF;
                    FETCH v_cursor INTO v_durata_preparare, v_nume_produs, v_descriere_produs, v_id_produs;
                END LOOP;
            END IF;
        END LOOP;
        CLOSE c;
        FOR i IN t.FIRST..t.LAST LOOP
            SELECT nume INTO v_nume_produs
            FROM produs
            WHERE id_produs = i;
            
            SELECT a.nume, a.prenume INTO v_nume_bucatar, v_prenume_bucatar
            FROM angajat a, bucatar b
            WHERE a.id_angajat = t(i).id_ang
            AND a.id_angajat =  b.id_angajat;
            DBMS_OUTPUT.PUT_LINE('Bucatarul ' || v_nume_bucatar || ' ' || v_prenume_bucatar || ' a preparat produsul ' || v_nume_produs || ' in ' || t(i).dur || ' minute.');
        END LOOP;
    END afis_durata_preparare_produs;
    
    FUNCTION profit_client (my_id_client IN client.id_client%TYPE)
    RETURN PLS_INTEGER IS
        TYPE tab_ind IS TABLE OF PLS_INTEGER INDEX BY PLS_INTEGER;
        v_nume_client               client.nume%TYPE;
        v_prenume_client            client.nume%TYPE;
        v_nr_telefon_client         client.nr_telefon%TYPE;
        v_profit_maxim_companie     PLS_INTEGER := 0;
        v_profit_maxim_client       PLS_INTEGER := 0;
        v_benef_red                 BINARY_INTEGER :=0;
        v_valoare_red               PLS_INTEGER :=0;
        invalid                     EXCEPTION;
        v_exista                    PLS_INTEGER := 0;
        t                           tab_ind;
    BEGIN
        SELECT COUNT(*) INTO v_exista
        FROM client
        WHERE id_client = my_id_client;
        IF v_exista = 0 THEN
            RAISE invalid;
        END IF;
        
        SELECT nume, prenume, nr_telefon INTO v_nume_client, v_prenume_client, v_nr_telefon_client
        FROM client
        WHERE id_client = my_id_client;
        
        FOR i IN (SELECT cli.id_client, cli.nume, cli.prenume, cmd.pret, f.valoare, f.detalii 
                  FROM client cli, comanda cmd, factura f
                  WHERE cli.id_client = cmd.id_client 
                  AND cmd.id_factura = f.id_factura) LOOP
            BEGIN
                IF i.id_client = my_id_client AND i.valoare < i.pret THEN
                    v_benef_red := 1;
                    v_valoare_red := v_valoare_red + (i.pret - i.valoare);
                ELSIF i.id_client = my_id_client THEN
                    t(i.id_client) := t(i.id_client) + (i.valoare - i.pret);
                END IF;
                v_profit_maxim_companie  := v_profit_maxim_companie  + (i.valoare - i.pret);
                t(i.id_client) := t(i.id_client) + (i.valoare - i.pret);
            EXCEPTION -- pentru prima inserare in tabel
                WHEN NO_DATA_FOUND THEN
                    t(i.id_client) := i.valoare - i.pret;
            END;
        END LOOP;
        DBMS_OUTPUT.PUT_LINE('Profitul obtinut in urma clientilor de pana acum este de ' || v_profit_maxim_companie || ' lei.');
        FOR i IN 1..t.LAST LOOP
            IF  t(i) > v_profit_maxim_client THEN
                v_profit_maxim_client := t(i);
            END IF;
        END LOOP;
        DBMS_OUTPUT.PUT_LINE('Profitul maxim adus de un client este de ' || v_profit_maxim_client || ' lei.');
        DBMS_OUTPUT.PUT_LINE('Clientul cu id-ul ' || my_id_client || ' este ' || v_nume_client || ' ' || v_prenume_client || ' cu nr de telefon: ' || v_nr_telefon_client || '.');
        IF v_benef_red = 1 THEN
            DBMS_OUTPUT.PUT_LINE('A beneficiat de reduceri in valoare de ' || v_valoare_red || ' lei.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Nu beneficiaza de reduceri!');
        END IF;
        RETURN t(my_id_client);
    EXCEPTION
        WHEN invalid THEN
            RAISE_APPLICATION_ERROR(-20021, 'Nu a fost gasit niciun client cu id-ul introdus!');
        WHEN OTHERS THEN
            RAISE_APPLICATION_ERROR(-20022, 'Alta eroare!');
    END profit_client;
    
    PROCEDURE mai_multe_ingrediente_producator(my_id_comanda IN comanda.id_comanda%TYPE, my_id_produs IN produs.id_produs%TYPE, my_id_producator IN producator.id_producator%TYPE, my_numar_produse_comanda continut_comanda.numar_produse%TYPE)
    IS
        TYPE rec IS RECORD (v_cantitate_produs cantitate_produs.cantitate%TYPE, v_nume_ingredient ingredient.nume%TYPE);
        TYPE tab_ind IS TABLE OF rec INDEX BY PLS_INTEGER;
        t   tab_ind;
    BEGIN
        SELECT cnt.cantitate, i.nume BULK COLLECT INTO t
        FROM cantitate_produs cnt, ingredient i, producator prod, produs p, continut_comanda cmd
        WHERE cmd.id_comanda = my_id_comanda
        AND cmd.id_produs = my_id_produs
        AND p.id_produs = cmd.id_produs
        AND cnt.id_produs = p.id_produs
        AND i.id_ingredient = cnt.id_ingredient
        AND prod.id_producator = i.id_producator
        AND my_id_producator = prod.id_producator;
        FOR i IN 1..t.LAST LOOP
            DBMS_OUTPUT.PUT_LINE('-> ' || my_numar_produse_comanda*t(i).v_cantitate_produs || 'g de ' ||t(i).v_nume_ingredient || '.');
        END LOOP;
    END mai_multe_ingrediente_producator;
    
    PROCEDURE afis_cantitate_ingredient_producator_produs(my_id_producator IN producator.id_producator%TYPE) IS
        CURSOR mycursor IS (SELECT p.id_produs, p.nume, cmd.numar_produse, cmd.id_comanda
                            FROM produs p, continut_comanda cmd
                            WHERE p.id_produs=cmd.id_produs);
        TYPE tab_ind IS TABLE OF ingredient.nume%TYPE INDEX BY PLS_INTEGER;
        v_id_produs                         produs.id_produs%TYPE;
        v_id_comanda                        continut_comanda.id_comanda%TYPE;
        v_nume_produs                       produs.nume%TYPE;
        v_numar_produse                     continut_comanda.numar_produse%TYPE;
        v_cantitate_produs                  cantitate_produs.cantitate%TYPE;
        v_nume_ingredient                   ingredient.nume%TYPE;
        v_nume_producator                   producator.nume%TYPE;
        v_nr_telefon_producator             producator.nr_telefon%TYPE;
        lista_ingrediente_producator        tab_ind;
    BEGIN
        SELECT nume, nr_telefon INTO v_nume_producator, v_nr_telefon_producator
        FROM producator
        WHERE id_producator = my_id_producator;
        SELECT nume BULK COLLECT INTO lista_ingrediente_producator
        FROM ingredient
        WHERE id_producator = my_id_producator;
        DBMS_OUTPUT.PUT_LINE('Numele producatorului selectat este ' || v_nume_producator || '.');
        DBMS_OUTPUT.PUT_LINE('El poate fi contactat la nurmarul de telefon: ' || v_nr_telefon_producator || '.');
        DBMS_OUTPUT.PUT_LINE('Acesta produce urmatoarele ingrediente:');
        FOR i IN 1..lista_ingrediente_producator.LAST LOOP
            DBMS_OUTPUT.PUT_LINE('-> ' || lista_ingrediente_producator(i) || ';');
        END LOOP;
        DBMS_OUTPUT.NEW_LINE();
        OPEN mycursor;
        LOOP
            FETCH mycursor INTO v_id_produs,v_nume_produs, v_numar_produse, v_id_comanda;
            EXIT WHEN mycursor%NOTFOUND;
            DBMS_OUTPUT.PUT_LINE('-- ' || v_nume_produs || ' are de la producatorul ' || v_nume_producator || ' in comanda cu id-ul ' || v_id_comanda || ' urmatorul ingredient:');
            BEGIN
                SELECT cnt.cantitate, i.nume INTO v_cantitate_produs, v_nume_ingredient
                FROM cantitate_produs cnt, ingredient i, producator prod, produs p, continut_comanda cmd
                WHERE cmd.id_comanda = v_id_comanda
                AND cmd.id_produs = v_id_produs
                AND p.id_produs = cmd.id_produs
                AND cnt.id_produs = p.id_produs
                AND i.id_ingredient = cnt.id_ingredient
                AND prod.id_producator = i.id_producator
                AND my_id_producator = prod.id_producator;
                DBMS_OUTPUT.PUT_LINE('-> ' || v_numar_produse*v_cantitate_produs || 'g de ' || v_nume_ingredient || '.');
                DBMS_OUTPUT.NEW_LINE();
            EXCEPTION
                WHEN NO_DATA_FOUND THEN
                    DBMS_OUTPUT.PUT_LINE('ATENTIE! Nu exista niciun ingredient al acestui producator in acest preparat!');
                    DBMS_OUTPUT.NEW_LINE();
                WHEN TOO_MANY_ROWS THEN
                    DBMS_OUTPUT.PUT_LINE('ATENTIE! Mai multe ingrediente ale acestui producator detectate in acest preparat!');
                    BEGIN
                        mai_multe_ingrediente_producator(v_id_comanda, v_id_produs, my_id_producator, v_numar_produse);
                    END;
                    DBMS_OUTPUT.NEW_LINE();
            END;
        END LOOP;
        CLOSE mycursor;
    END afis_cantitate_ingredient_producator_produs;
END pachet_cerinte;
/

-- Exercitiul 6
-- Produsele preferate pentru un client citit de la tastatura
-- Se afiseaza numele si prenumele clientului, produsele preferate si de cate ori le-a comandat.

DECLARE
    v_id_client_citit       client.id_client%TYPE := &client_id;
    my_tab                  tab_imb;
    id                      PLS_INTEGER;
    v_nume_produs           produs.nume%TYPE;
    v_nume                  client.nume%TYPE;
    v_prenume               client.prenume%TYPE;
    v_contor                PLS_INTEGER := 0;
BEGIN
    my_tab := pachet_cerinte.afis_produse_preferate(v_id_client_citit);
    SELECT c.nume, c.prenume INTO v_nume, v_prenume
    FROM client c
    WHERE c.id_client = v_id_client_citit;
    DBMS_OUTPUT.PUT_LINE('Clientul ' || v_nume || ' ' || v_prenume || ' are urmatoarele produse favorite:');
    id := my_tab.FIRST; -- prima valoare din tabelul indexat (vector frecventa)
    LOOP
        EXIT WHEN id IS NULL;
        SELECT p.nume INTO v_nume_produs
        FROM produs p
        WHERE p.id_produs = my_tab(id);
        -- afisam produsele cu numarul maxim de aparitii
        v_contor := v_contor + 1;
        DBMS_OUTPUT.PUT_LINE(v_contor || '. ' || v_nume_produs);
        id := my_tab.NEXT(id);
    END LOOP;
END;

-- Exercitiul 7
-- Sa se afiseze pentru fiecare bucatar numele si prenumele acestuia, daca a preparat vreun produs de cand s-a angajat.
-- Daca a preparat minim un produs se va afisa date corespunzatoare legate de acesta (numele produsului, descrierea acestuia si
-- cat a durat sa il prepare).

BEGIN
    pachet_cerinte.afis_durata_preparare_produs();
END;
/

-- Exercitiul 8
-- Sa se afiseze profitul generat de un client citit de la tastatura, de-a lungul timpului,
-- Daca aceasta a beneficiat de reduceri de-a lungul timpului se va afisa valoarea totala a acestor reduceri.
-- Daca profitul adus de-a lungul timpului este unul negativ inseamna ca clientul a beneficiat de prea multe reduceri
-- si se va afisa un mesaj corespunzator.
-- De asemenea se va afisa  profitul maxim oferit de un client si profitul obtinut de companie de-a lungul timpului.

DECLARE
    v_id_client_citit       client.id_client%TYPE := &client_id;
    v_rezultat              PLS_INTEGER;
    
BEGIN
    v_rezultat := pachet_cerinte.profit_client(v_id_client_citit);
    IF v_rezultat > 0 THEN
        DBMS_OUTPUT.PUT_LINE('El a contribuit la profitul companiei pana acum cu ' || v_rezultat || ' lei.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Acesta a beneficiat de prea multe reduceri! Aducand un profit negativ companiei: ' || v_rezultat || ' lei.');
    END IF;
END;
/

-- Exercitiul 9
-- Sa se afiseze ce cantitate ingrediente contine fiecare produs, din fiecare comanda,
-- furnizate de un producator citit de la tastatura.

DECLARE 
    v_id_producator producator.id_producator%TYPE := &producator_id;
BEGIN
    afis_cantitate_ingredient_producator_produs(v_id_producator);
END;
/

-- Exercitiul 10