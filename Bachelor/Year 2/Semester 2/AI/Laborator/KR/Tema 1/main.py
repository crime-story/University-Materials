import heapq
import os.path
import time
from math import sqrt
from os import listdir
from queue import PriorityQueue, Queue
from argparse import ArgumentParser


class NodParcurgere:
    def __eq__(self, nod_nou):
        return (self.g == nod_nou.g and self.f == nod_nou.f and self.pozitie_bilet == nod_nou.pozitie_bilet)

    def __lt__(self, nod_nou):
        return self.f < nod_nou.f or (self.f == nod_nou.f and self.g > nod_nou.g)

    def __init__(self, pozitie_bilet, parinte, timp_actual, cost=0, h=0):
        self.pozitie_bilet = pozitie_bilet
        self.parinte = parinte  # parintele din arborele de parcurgere
        self.timp_actual = timp_actual
        self.g = cost  # consider cost=1 pentru o mutare
        self.h = h
        self.f = self.g + self.h

    def obtineDrum(self):
        '''
        :return: lista cu nodurile gasite pana la radacina
        '''
        l = [self]
        nod = self
        while nod.parinte is not None:
            l.insert(0, nod.parinte)
            nod = nod.parinte
        return l

    def afisDrum(self, afisCost=False, afisLung=False):  # returneaza si lungimea drumului
        '''
        Afiseaza in fisierul de iesire pasii pana la solutie (numarul de ordine si pozitia biletului - nume),
        precum si configuratia drumului.
        :param afisCost: true daca vreau sa afisez costul de la radacina pana la nodul nou construit (g)
        :param afisLung: true daca vreau sa afisez cate noduri am pana la nodul nou construit inclusiv
        :return: cate noduri am parcurs pana la nodul nou construit inclusiv
        '''
        lista_noduri = self.obtineDrum()
        sol = ""
        for i in range(len(lista_noduri)):
            fout.write("Step " + str(i + 1) + ":\n")
            linie_bilet, coloana_bilet = lista_noduri[i].pozitie_bilet
            if i > 0:
                linie_predecesor, coloana_predecesor = lista_noduri[i - 1].pozitie_bilet
                if linie_predecesor == linie_bilet and coloana_predecesor % 2 == 0 and coloana_predecesor + 1 == coloana_bilet:
                    # sunt colegi de banca
                    sol += " > "
                elif linie_predecesor == linie_bilet and coloana_predecesor % 2 == 1 and coloana_predecesor - 1 == coloana_bilet:
                    sol += " < "
                elif linie_predecesor == linie_bilet and coloana_predecesor < coloana_bilet:
                    sol += " >> "
                elif linie_predecesor == linie_bilet and coloana_predecesor > coloana_bilet:
                    sol += " << "
                elif coloana_bilet == coloana_predecesor and linie_predecesor < linie_bilet:
                    sol += " V "
                elif coloana_bilet == coloana_predecesor and linie_predecesor > linie_bilet:
                    sol += " ^ "
                elif coloana_bilet == coloana_predecesor and linie_predecesor == linie_bilet:  # daca biletul sta pe loc
                    sol += " # "
            fout.write(str(graph.matrice[linie_bilet][coloana_bilet]) + "\n\n")
            sol += str(graph.matrice[linie_bilet][coloana_bilet])
        fout.write("The route of the ticket:\n")
        fout.write(sol + "\n")
        if afisCost:
            fout.write("\nThe cost of the road (f = g + h): " + str(self.f) + "\n")
            fout.write("Real Cost (g): " + str(self.g) + "\n")
            fout.write("Estimate Cost (h): " + str(self.h) + "\n")
        if afisLung:
            fout.write("Length of the road: " + str(len(lista_noduri)) + "\n")
        return len(lista_noduri)

    def contineInDrum(self, nodNou):
        '''
        :param nodNou: nodul de verificat
        :return: daca nodul primit ca parametru este in drum
        '''
        nodDrum = self
        while nodDrum is not None:
            if nodNou == nodDrum:
                return True
            nodDrum = nodDrum.parinte
        return False

    def __repr__(self):
        sir = ""
        sir += str(self.pozitie_bilet)
        return (sir)


class Graph:
    def __init__(self, fin, tip_euristica="banala"):
        '''
        :param fin: fisierul de intrare
        :param tip_euristica: ce fel de euristica dorim sa folosim pentru calcularea h-ului si, implicit f-ului
        In constructor vom initializa matrice, care contine configuratia.
        Dictionarul suparati retine pentru un elev o lista cu elevii pe care este suparat (sau lista goala daca nu este suparat pe nimeni).
        Dictionarul pozitie_elev retine pentru numele unui elev, tuplul (x, y) unde este localizat.
        '''
        linie = fin.readline()
        self.matrice = []
        self.suparati = dict()
        self.pozitie_elev = dict()
        self.total = 1
        self.maxim = 0
        self.start_time = 0

        i = 0
        while "suparati" not in linie:
            linie = linie.split()
            if i == 0:
                assert len(linie) % 2 == 0, "nr impar de elevi pe prima linie"
            else:
                assert len(linie) == len(self.matrice[-1]), "nr diferit de elevi pe randul {}".format(i)
            self.matrice.append(linie)
            j = 0
            for elev in self.matrice[-1]:
                if elev != "liber":
                    assert elev not in self.pozitie_elev, "elev cu acelasi nume {}".format(elev)
                    self.suparati[elev] = []
                    self.pozitie_elev[elev] = (i, j)
                j += 1
            linie = fin.readline()
            i += 1

        linie = fin.readline()
        while "ascultati" not in linie:
            a, b = linie.split()
            self.suparati[a].append(b)
            self.suparati[b].append(a)
            linie = fin.readline()

        self.timp_ascultare = int(fin.readline())

        self.ascultati = []

        linie = fin.readline()
        while "mesaj:" not in linie:
            self.ascultati.append(linie.strip())
            linie = fin.readline()

        self.start = linie.split()[1]
        self.stop = linie.split()[3]

        self.tip_euristica = tip_euristica

    def stare_finala(self, nodCurent: NodParcurgere):
        '''
        :param nodCurent: un nod de verificat
        :return: true, daca nodul dat ca parametru este stare finala
        '''
        x, y = nodCurent.pozitie_bilet
        return self.matrice[x][y] == self.stop

    def calculeaza_h_euristica(self, nodCurent):
        '''
        :param nodCurent: un nod pentru care se calculeaza un cost aproximativ pana la nodul scope
        :return: costul aproximativ pana la nodul scope in functie de euristica setata in self.tip_euristica
        '''
        if self.tip_euristica == "banala":
            if nodCurent is None:
                return True
            return not self.stare_finala(nodCurent)

        if nodCurent is None:
            x, y = self.pozitie_elev[self.start]
        else:
            x, y = nodCurent.pozitie_bilet
        x_scop, y_scop = self.pozitie_elev[self.stop]

        if self.tip_euristica == 'manhattan':
            return abs(x - x_scop) + abs(y - y_scop)
        if self.tip_euristica == 'euclidiana':
            return sqrt((x - x_scop) ** 2 + (y - y_scop) ** 2)
        if self.tip_euristica == 'neadmisibila':
            return 50 * len(self.matrice) * len(self.matrice[0]) + x + y + ( 2**((x+y)**10) if nodCurent is not None and nodCurent.parinte is not None and nodCurent.pozitie_bilet==nodCurent.parinte.pozitie_bilet else 0)
            # return float('+inf')
        raise Exception("euristica necunoscuta")

    def profesor_a_terminat_de_ascultat(self, timp):
        '''
        :param timp: numarul de minute de cand a inceput ora
        :return: true daca profesorul a terminat de ascultat toti copiii
        '''
        return timp >= self.timp_ascultare * len(self.ascultati)

    def e_profesor(self, lin, col, timp):
        '''
        Este apelata de genereazaSuccesori pentru a stii daca putem inainta pe aceasta pozitie.
        :param lin: linia
        :param col: coloana
        :param timp: timpul la care se face verificarea
        :return: true daca profesorul vede pozitia data la acel moment (daca profesorul este vecinatatea respectiva, la minutul dat)
        '''
        if self.profesor_a_terminat_de_ascultat(timp):
            return False

        idx_elev_ascultat = timp // self.timp_ascultare
        elev_ascultat = self.ascultati[idx_elev_ascultat]
        poz_prof_lin, poz_prof_col = self.pozitie_elev[elev_ascultat]

        return abs(lin - poz_prof_lin) <= 1 and abs(col - poz_prof_col) <= 1

    def e_mutare_legala(self, nodCurent, lin_nou, col_nou):
        '''
        :param nodCurent: nodul, care contine linia si coloana unde se afla biletul actual
        :param lin_nou: linia pe care se muta biletul
        :param col_nou: coloana pe care se muta biletul
        :return: true daca biletul se poate muta pe noua pozitie (fara a tine cont de profesor)
        '''
        if not (0 <= lin_nou < len(self.matrice) and 0 <= col_nou < len(
                self.matrice[0])):  # daca sunt in afara matricei
            return False
        if self.matrice[lin_nou][col_nou] == 'liber':
            return False

        if self.matrice[nodCurent.pozitie_bilet[0]][nodCurent.pozitie_bilet[1]] in self.suparati[
            self.matrice[lin_nou][col_nou]]:  # daca pe noua pozitie se afla un elev pe care suntem suparati
            return False
        if nodCurent.pozitie_bilet == (lin_nou, col_nou):  # daca ramanem pe aceeasi pozitie
            return True
        if nodCurent.pozitie_bilet[0] != lin_nou:
            return True
        if nodCurent.pozitie_bilet[1] % 2 == 0:  # sunt pe un scaun stang in banca
            if col_nou == nodCurent.pozitie_bilet[1] + 1:  # mut in aceeasi banca la dreapta
                return True
            else:  # mut pe alta coloana in stanga
                return lin_nou >= len(self.matrice) - 2
        else:  # sunt pe un scaun drept in banca
            if col_nou == nodCurent.pozitie_bilet[1] - 1:  # mut in aceeasi banca la stanga
                return True
            else:  # mut pe alta coloana la dreapta
                return lin_nou >= len(self.matrice) - 2

    def calculeaza_cost(self, nodCurent, lin_nou, col_nou):
        '''
        :param nodCurent: nodul, care contine linia si coloana unde se afla biletul actual
        :param lin_nou: linia pe care se muta biletul
        :param col_nou: coloana pe care se muta biletul
        :return: costul mutarii
        '''
        if nodCurent.pozitie_bilet == (lin_nou, col_nou):  # daca ramane pe loc
            return 0
        elif nodCurent.pozitie_bilet[0] != lin_nou:  # mutare pe coloana sus-jos
            return 1
        else:
            if nodCurent.pozitie_bilet[1] % 2 == 0:  # sunt pe un scaun stang in banca
                if col_nou == nodCurent.pozitie_bilet[1] + 1:  # mut in aceeasi banca la dreapta
                    return 0
                else:  # mut pe alta coloana in stanga
                    return 2
            else:  # sunt pe un scaun drept in banca
                if col_nou == nodCurent.pozitie_bilet[1] - 1:  # mut in aceeasi banca la stanga
                    return 0
                else:  # mut pe alta coloana la dreapta
                    return 2

    def testeaza_scop(self, nodCurent: NodParcurgere):
        '''
        :param nodCurent: nodul pentru care se testeaza daca este o stare finala
        :return: true daca am ajuns intr-o stare finala
        '''
        return nodCurent.pozitie_bilet == self.pozitie_elev[self.stop]

    def testeaza_scop_initial(self, start: NodParcurgere, nrSolutiiCautate):
        if self.testeaza_scop(start):
            end_time = time.time()
            fout.write("Solution:\n")
            start.afisDrum(True, True)
            fout.write("Time: " + str(round(end_time - self.start_time, 4)) + "s\n")
            fout.write("\n----------------\n")
            nrSolutiiCautate -= 1
            if nrSolutiiCautate == 0:
                fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
                fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")
        return nrSolutiiCautate

    def genereazaSuccesori(self, nodCurent: NodParcurgere):
        '''
        :param nodCurent: nodul pentru care se genereaza succesori
        :return: o lista de posibili succesori (ulterior trebuie filtrat in functie de algoritm, sa nu fi trecut deja prin pozitia cea noua, de exemplu la DFS trebuie
        sa avem grija sa nu intram intr-un nod parinte)
        '''
        listaSuccesori = []
        dir = [(-1, 0), (0, 1), (1, 0), (0, -1)]  # sus, dreapta, jos, stanga
        for i in range(len(dir)):
            lin_nou = nodCurent.pozitie_bilet[0] + dir[i][0]
            col_nou = nodCurent.pozitie_bilet[1] + dir[i][1]
            timp_nou = nodCurent.timp_actual + 1
            if self.e_mutare_legala(nodCurent, lin_nou, col_nou):
                if not self.e_profesor(lin_nou, col_nou, timp_nou):
                    poz_nou = (lin_nou, col_nou)
                    cost_nou = self.calculeaza_cost(nodCurent, lin_nou, col_nou)
                    nodNou = NodParcurgere(poz_nou, nodCurent, timp_nou, cost_nou + nodCurent.g, 0)
                    h_nou = self.calculeaza_h_euristica(nodNou)
                    nodNou.h = h_nou
                    nodNou.f = nodNou.g + nodNou.h
                    if not nodCurent.contineInDrum(nodNou):
                        listaSuccesori.append(nodNou)
                elif not self.e_profesor(nodCurent.pozitie_bilet[0], nodCurent.pozitie_bilet[1], timp_nou):
                    poz_nou = nodCurent.pozitie_bilet
                    cost_nou = nodCurent.g
                    nodNou = NodParcurgere(poz_nou, nodCurent, timp_nou, cost_nou, 0)
                    h_nou = self.calculeaza_h_euristica(nodNou)
                    nodNou.h = h_nou
                    nodNou.f = nodNou.g + nodNou.h

                    listaSuccesori.append(nodNou)

        return listaSuccesori

    def breadth_first_queue(self, nrSolutiiCautate=1):
        # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
        self.total = 1
        self.maxim = 0
        self.start_time = time.time()
        coada = Queue()
        start = NodParcurgere(self.pozitie_elev[self.start], None, 0, 0, self.calculeaza_h_euristica(None))
        coada.put(start)

        nrSolutiiCautate = self.testeaza_scop_initial(start, nrSolutiiCautate)

        while nrSolutiiCautate > 0 and not coada.empty():
            self.maxim = max(self.maxim, coada.qsize())
            nodCurent = coada.get()

            lSuccesori = self.genereazaSuccesori(nodCurent)

            if time.time() - self.start_time > timeout:
                fout.write("TIMEOUT!!!\n")
                break

            for i in lSuccesori:
                self.total += 1
                if self.testeaza_scop(i):
                    end_time = time.time()
                    fout.write("Solution:\n")
                    i.afisDrum(True, True)
                    fout.write("Time: " + str(round(end_time - self.start_time, 4)) + "s\n")
                    fout.write("\n----------------\n")
                    nrSolutiiCautate -= 1
                    if nrSolutiiCautate == 0:
                        fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
                        fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")
                        return
                coada.put(i)

    def depth_first(self, nrSolutiiCautate=1):
        # vom simula o stiva prin relatia de parinte a nodului curent
        vizitat = []

        def df(nodCurent, nrSolutiiCautate):
            if nrSolutiiCautate <= 0:  # testul acesta s-ar valida doar daca in apelul initial avem df(start,if nrSolutiiCautate=0)
                return nrSolutiiCautate
            if self.testeaza_scop(nodCurent):
                end_time = time.time()
                fout.write("Solution:\n")
                nodCurent.afisDrum(True, True)
                fout.write("Time: " + str(round(end_time - self.start_time, 4)) + "s\n")
                fout.write("\n----------------\n")
                nrSolutiiCautate -= 1
                if nrSolutiiCautate == 0:
                    return nrSolutiiCautate
            lSuccesori = self.genereazaSuccesori(nodCurent)

            if time.time() - self.start_time > timeout:
                fout.write("TIMEOUT!!!\n")
                return

            self.maxim = max(self.maxim, len(nodCurent.obtineDrum()))
            for i in lSuccesori:
                self.total += 1
                if nrSolutiiCautate != 0:
                    if i.pozitie_bilet == nodCurent.pozitie_bilet or i.pozitie_bilet not in vizitat:
                        vizitat.append(i.pozitie_bilet)
                        nrSolutiiCautate = df(i, nrSolutiiCautate)
                        vizitat.pop()
            return nrSolutiiCautate

        self.start_time = time.time()
        self.total = 1
        self.maxim = 0

        start = NodParcurgere(self.pozitie_elev[self.start], None, 0, 0,
                              self.calculeaza_h_euristica(None))
        nrSolutiiCautate = self.testeaza_scop_initial(start, nrSolutiiCautate)

        if nrSolutiiCautate > 0:
            df(start, nrSolutiiCautate)
        fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
        fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")

    def dfi(self, nodCurent, adancime, nrSolutiiCautate, areTimeout):
        if adancime == 1 and self.testeaza_scop(nodCurent):
            end_time = time.time()
            fout.write("Solution:\n")
            nodCurent.afisDrum(True, True)
            fout.write("Time: " + str(round(end_time - self.start_time, 4)) + "s\n")
            fout.write("\n----------------\n")
            nrSolutiiCautate -= 1
            if nrSolutiiCautate == 0:
                return nrSolutiiCautate, areTimeout
        if adancime > 1:
            lSuccesori = self.genereazaSuccesori(nodCurent)

            if time.time() - self.start_time > timeout:
                areTimeout = True
                return nrSolutiiCautate, areTimeout

            self.maxim = max(self.maxim, len(nodCurent.obtineDrum()))
            if not areTimeout:
                for sc in lSuccesori:
                    self.total += 1
                    if nrSolutiiCautate != 0:
                        nrSolutiiCautate, areTimeout = self.dfi(sc, adancime - 1, nrSolutiiCautate, areTimeout)
        return nrSolutiiCautate, areTimeout

    def depth_first_iterativ(self, nrSolutiiCautate=1):
        self.start_time = time.time()
        self.total = 1
        self.maxim = 0
        areTimeout = False
        for i in range(1, len(self.matrice) * len(self.matrice[0]) + 1):
            if nrSolutiiCautate == 0:
                break
            nod_start = NodParcurgere(self.pozitie_elev[self.start], None, 0, 0, self.calculeaza_h_euristica(None))
            nrSolutiiCautate = self.testeaza_scop_initial(nod_start, nrSolutiiCautate)
            if nrSolutiiCautate > 0:
                nrSolutiiCautate, areTimeout = self.dfi(nod_start, i, nrSolutiiCautate, areTimeout)
            if areTimeout:
                fout.write("TIMEOUT!!!\n")
                break
        fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
        fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")

    def a_star(self, nrSolutiiCautate=1):
        # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
        self.start_time = time.time()
        self.total = 1
        self.maxim = 0
        c = PriorityQueue()
        nod_start = NodParcurgere(self.pozitie_elev[self.start], None, 0, 0, self.calculeaza_h_euristica(None))
        c.put(nod_start)

        nrSolutiiCautate = self.testeaza_scop_initial(nod_start, nrSolutiiCautate)

        if nrSolutiiCautate > 0:
            while not c.empty():
                self.maxim = max(self.maxim, c.qsize())
                nodCurent = c.get()

                if self.testeaza_scop(nodCurent):
                    end_time = time.time()
                    fout.write("Solution:\n")
                    nodCurent.afisDrum(True, True)
                    fout.write("Time: " + str(round(end_time - self.start_time, 4)) + "s\n")
                    fout.write("\n----------------\n")
                    nrSolutiiCautate -= 1
                    if nrSolutiiCautate == 0:
                        break

                if time.time() - self.start_time > timeout:
                    fout.write("TIMEOUT!!!\n")
                    break

                lSuccesori = self.genereazaSuccesori(nodCurent)
                for s in lSuccesori:
                    self.total += 1
                    c.put(s)

        fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
        fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")

    def a_star_optimizat(self):
        self.start_time = time.time()
        self.total = 1
        self.maxim = 0
        # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
        nod_start = NodParcurgere(self.pozitie_elev[self.start], None, 0, 0, self.calculeaza_h_euristica(None))
        nrSolutiiCautate = self.testeaza_scop_initial(nod_start, 1)
        if nrSolutiiCautate:
            l_open = [nod_start]
            nr_elemente_inlocuite_open = 0
            nr_elemente_inlocuite_closed = 0
            # l_open contine nodurile candidate pentru expandare (este echivalentul lui c din A* varianta neoptimizata)

            # l_closed contine nodurile expandate
            l_closed = dict()
            while len(l_open) > 0:
                nodCurent = heapq.heappop(l_open)
                # print(nodCurent.pozitie_bilet)
                l_closed[nodCurent.pozitie_bilet] = nodCurent
                if self.testeaza_scop(nodCurent):
                    end_time = time.time()
                    fout.write("Solution:\n")
                    nodCurent.afisDrum(True, True)
                    fout.write("Time: " + str(end_time - self.start_time) + "s\n")
                    fout.write("\n----------------\n")
                    fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
                    fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")
                    return
                lSuccesori = self.genereazaSuccesori(nodCurent)
                if time.time() - self.start_time > timeout:
                    fout.write("TIMEOUT!!!\n")
                    break
                for s in lSuccesori:
                    self.total += 1
                    gasitC = False
                    for nodC in l_open:
                        if s.pozitie_bilet == nodC.pozitie_bilet:
                            gasitC = True
                            if s.f >= nodC.f:
                                lSuccesori.remove(s)
                            else:  # s.f<nodC.f
                                l_open.remove(nodC)
                                heapq.heapify(
                                    l_open)  # scot nodul curent din Heap, refac heap-ul cu lista care mi-a ramas
                                nr_elemente_inlocuite_open += 1
                            break
                    if not gasitC:
                        if s.pozitie_bilet in l_closed:
                            if s.f >= l_closed[s.pozitie_bilet].f:
                                lSuccesori.remove(s)
                            else:  # s.f<nodC.f
                                l_closed.pop(s.pozitie_bilet)
                                nr_elemente_inlocuite_closed += 1
                            break
                for s in lSuccesori:
                    heapq.heappush(l_open, s)
                self.maxim = max(self.maxim, len(l_open) + len(l_closed))
        fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
        fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")

    def ida_star(self, nrSolutiiCautate=1):
        self.first_path_was_found = False

        def construieste_drum(nodCurent, limita, nrSolutiiCautate, areTimeout):
            if nodCurent.f > limita:
                return nrSolutiiCautate, nodCurent.f, areTimeout

            if self.testeaza_scop(nodCurent) and (nodCurent.f == limita or not self.first_path_was_found):
                self.first_path_was_found = True
                end_time = time.time()
                fout.write("Solution:\n")
                nodCurent.afisDrum(True, True)
                fout.write("Time: " + str(round(end_time - self.start_time, 4)) + "s\n")
                fout.write("\n----------------\n")
                nrSolutiiCautate -= 1
                if nrSolutiiCautate == 0:
                    return 0, "gata", areTimeout

            lSuccesori = self.genereazaSuccesori(nodCurent)
            if time.time() - self.start_time > timeout:
                areTimeout = True
                return -1, " ", areTimeout
            self.maxim = max(self.maxim, len(nodCurent.obtineDrum()))
            minim = float('inf')
            if not areTimeout:
                for s in lSuccesori:
                    self.total += 1
                    nrSolutiiCautate, rez, areTimeout = construieste_drum(s, limita, nrSolutiiCautate, areTimeout)
                    if rez == "gata" or rez == " ":
                        return 0, rez, areTimeout
                    if rez < minim:
                        minim = rez
            return nrSolutiiCautate, minim, areTimeout

        self.start_time = time.time()
        self.total = 1
        self.maxim = 0
        areTimeout = False
        nodStart = NodParcurgere(self.pozitie_elev[self.start], None, 0, 0, self.calculeaza_h_euristica(None))
        nrSolutiiCautate = self.testeaza_scop_initial(nodStart, nrSolutiiCautate)
        if nrSolutiiCautate > 0:
            limita = nodStart.f

            while True:
                nrSolutiiCautate, rez, areTimeout = construieste_drum(nodStart, limita, nrSolutiiCautate, areTimeout)
                if rez == "gata":
                    break
                if rez == float('inf'):
                    fout.write("Nu mai exista solutii!\n")
                    break
                if areTimeout:
                    fout.write("TIMEOUT!!!\n")
                    break
                limita = rez

            fout.write("The total number of nodes calculated: " + str(self.total) + "\n")
            fout.write("The maximum number of nodes in the memory: " + str(self.maxim) + "\n")


if __name__ == "__main__":
    """
    Programul se apeleaza: python main.py -i "folder_input" -o "folder_output" -n "5" -t "30"
    """
    parser = ArgumentParser(usage=__file__ + ' '
                                             '-i/--input in'
                                             '-o/--output out'
                                             '-n/--nrsolutii nsol'
                                             '-t/--timeoout timeout',
                            description='Problema de cautare (un mesaj...)')

    parser.add_argument('-i', '--input',
                        dest='input',
                        default='folder_input',
                        help='Folder Input!')

    parser.add_argument('-o', '--output',
                        dest='output',
                        default='folder_output',
                        help='Folder Output!')

    parser.add_argument('-n', '--nrsolutii',
                        dest='nrsolutii',
                        default='1',
                        help='Numarul de solutii!')

    parser.add_argument('-t', '--timeout',
                        dest='timeout',
                        default='100',
                        help='Timeout in secunde!')

    args = vars(parser.parse_args())

    inputPath = args['input']
    outputPath = args['output']
    nSol = int(args['nrsolutii'])
    timeout = float(args['timeout'])

    assert nSol > 0, "nSol must be > 0"
    assert timeout > 0, "timeout must be > 0"

    # creez folder-ul de output daca acesta nu exista
    if not os.path.exists(outputPath):
        os.mkdir(outputPath)

    for inputFile in listdir('./' + inputPath):
        try:
            fin = open('./' + inputPath + '/' + inputFile, 'r')
            fout = open(outputPath + '/' + inputFile.replace('.in', '') + '.out', 'w')

            print("Rulare input " + str(inputFile))
            graph = Graph(fin, tip_euristica='banala')
            # BFS
            print("BFS is running...\n")
            graph.breadth_first_queue(nSol)
            fout.write("\n\nBFS DONE!\n\n")
            print("BFS finished!\n")

            # DFS
            print("DFS is running...")
            graph.depth_first(nSol)
            fout.write("\n\nDFS DONE!\n\n")
            print("DFS finished!\n")

            # DFI
            print("DFI is running...\n")
            graph.depth_first_iterativ(nSol)
            fout.write("\n\nDFI DONE!\n\n")
            print("DFI finished!\n")

            # A STAR
            print("A STAR with ordinary heuristic is running...\n")
            graph.tip_euristica = "banala"
            graph.a_star(nSol)
            fout.write("\n\nA STAR WITH ORDINARY HEURISTIC DONE!\n\n")
            print("A STAR with ordinary heuristic finished!\n")

            print("A STAR with manhattan heuristic is running...\n")
            graph.tip_euristica = "manhattan"
            graph.a_star(nSol)
            fout.write("\n\nA STAR WITH MANHATTAN HEURISTIC DONE!\n\n")
            print("A STAR with manhattan heuristic finished!\n")

            print("A STAR with euclidian heuristic is running...\n")
            graph.tip_euristica = "euclidiana"
            graph.a_star(nSol)
            fout.write("\n\nA STAR WITH EUCLIDIAN HEURISTIC DONE!\n\n")
            print("A STAR with euclidian heuristic finished!\n")

            print("A STAR with unacceptable heuristic is running...\n")
            graph.tip_euristica = "neadmisibila"
            graph.a_star(nSol)
            fout.write("\n\nA STAR WITH UNACCEPTABLE HEURISTIC DONE!\n\n")
            print("A STAR with unacceptable heuristic finished!\n")

            # A STAR OPTIMIZAT
            print("A STAR OPT with ordinary heuristic is running...\n")
            graph.tip_euristica = "banala"
            graph.a_star_optimizat()
            fout.write("\n\nA STAR OPT WITH ORDINARY HEURISTIC DONE!\n\n")
            print("A STAR OPT with ordinary heuristic finished!\n")

            print("A STAR OPT with manhattan heuristic is running...\n")
            graph.tip_euristica = "manhattan"
            graph.a_star_optimizat()
            fout.write("\n\nA STAR OPT WITH MANHATTAN HEURISTIC DONE!\n\n")
            print("A STAR OPT with manhattan heuristic finished!\n")

            print("A STAR OPT with euclidian heuristic is running...\n")
            graph.tip_euristica = "euclidiana"
            graph.a_star_optimizat()
            fout.write("\n\nA STAR OPT WITH EUCLIDIAN HEURISTIC DONE!\n\n")
            print("A STAR OPT with euclidian heuristic finished!\n")

            print("A STAR OPT with unacceptable heuristic is running...\n")
            graph.tip_euristica = "neadmisibila"
            graph.a_star_optimizat()
            fout.write("\n\nA STAR OPT WITH UNACCEPTABLE HEURISTIC DONE!\n\n")
            print("A STAR OPT with unacceptable heuristic finished!\n")

            # IDA STAR
            print("IDA STAR with ordinary heuristic is running...\n")
            graph.tip_euristica = "banala"
            graph.ida_star(nSol)
            fout.write("\n\nIDA STAR WITH ORDINARY HEURISTIC DONE!\n\n")
            print("IDA STAR with orindary heuristic finished!\n")

            print("IDA STAR with manhattan heuristic is running...\n")
            graph.tip_euristica = "manhattan"
            graph.ida_star(nSol)
            fout.write("\n\nIDA STAR WITH MANHATTAN HEURISTIC DONE!\n\n")
            print("IDA STAR with manhattan heuristic finished!\n")

            print("IDA STAR with euclidian heuristic is running...\n")
            graph.tip_euristica = "euclidiana"
            graph.ida_star(nSol)
            fout.write("\n\nIDA STAR WITH EUCLIDIAN HEURISTIC DONE!\n\n")
            print("IDA STAR with euclidian heuristic finished!\n")

            print("IDA STAR with unacceptable heuristic is running...\n")
            graph.tip_euristica = "neadmisibila"
            graph.ida_star(nSol)
            fout.write("\n\nIDA STAR WITH UNACCEPTABLE HEURISTIC DONE!\n\n")
            print("IDA STAR with unacceptable heuristic finished!\n")

            fin.close()
            fout.close()
        except Exception as e:
            print(e)
