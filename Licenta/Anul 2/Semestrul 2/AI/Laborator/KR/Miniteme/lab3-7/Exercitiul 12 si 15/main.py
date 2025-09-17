"""
Dati enter dupa fiecare solutie afisata.

Presupunem ca avem costul de mutare al unui bloc egal cu indicele in alfabet, cu indicii incepănd de la 1 (care se calculează prin 1+ diferenta dintre valoarea codului ascii al literei blocului de mutat si codul ascii al literei "a" ) . Astfel A* are trebui sa prefere drumurile in care se muta intai blocurile cu infomatie mai mica lexicografic pentru a ajunge la una dintre starile scop
"""

import copy


# informatii despre un nod din arborele de parcurgere (nu din graful initial)
class NodParcurgere:
    def __init__(self, info, parinte, cost=0, h=0):
        self.info = info
        self.parinte = parinte  # parintele din arborele de parcurgere
        self.g = cost  # consider cost=1 pentru o mutare
        self.h = h
        self.f = self.g + self.h

    def obtineDrum(self):
        l = [self];
        nod = self
        while nod.parinte is not None:
            l.insert(0, nod.parinte)
            nod = nod.parinte
        return l

    def afisDrum(self, afisCost=False, afisLung=False):  # returneaza si lungimea drumului
        l = self.obtineDrum()
        for nod in l:
            print(str(nod))
        if afisCost:
            print("Cost: ", self.g)
        if afisCost:
            print("Lungime: ", len(l))
        return len(l)

    def contineInDrum(self, infoNodNou):
        nodDrum = self
        while nodDrum is not None:
            if (infoNodNou == nodDrum.info):
                return True
            nodDrum = nodDrum.parinte

        return False

    def __repr__(self):
        sir = ""
        sir += str(self.info)
        return (sir)

    # euristica banală: daca nu e stare scop, returnez 1, altfel 0

    def __str__(self):
        sir = ""
        maxInalt = max([len(stiva) for stiva in self.info])
        for inalt in range(maxInalt, 0, -1):
            for stiva in self.info:
                if len(stiva) < inalt:
                    sir += "  "
                else:
                    sir += stiva[inalt - 1] + " "
            sir += "\n"
        sir += "-" * (2 * len(self.info) - 1)
        return sir

    """
    def __str__(self):
        sir=""
        for stiva in self.info:
            sir+=(str(stiva))+"\n"
        sir+="--------------\n"
        return sir
    """


class Graph:  # graful problemei

    def este_stare_finala(self, stare):
        lungime = len(stare[0])
        for linie in stare:
            lungime1 = len(linie)
            if lungime != lungime1:
                return False
        return True

    def verificare_configuratie_initiala(self):
        nr_stive = len(self.start)
        nr_elem = 0
        for linie in self.start:
            nr_elem += len(linie)
        if nr_elem % nr_stive == 0:
            return True
        else:
            return False

    def input_valid(self):
        # caz in care contin elemente diferite
        elem_start = []
        for linie in self.start:
            for elem in linie:
                if elem not in elem_start:
                    elem_start.append(elem)
        elem_start.sort()
        ok = 0
        for stare in self.scopuri:
            elem_final = []
            for linie in stare:
                for elem in linie:
                    if elem not in elem_final:
                        elem_final.append(elem)
            elem_final.sort()
            if elem_final == elem_start:
                ok = 1
                break
        if ok == 0:
            return ok

        # caz in care au nr de stive diferite
        lungime_start = len(self.start)
        ok = 0
        for stare in self.scopuri:
            lungime_final = len(stare)
            if lungime_final == lungime_start:
                ok = 1
                break
        if ok == 0:
            return ok

        # caz in care avem doar o stiva si starile trebuie sa fie egale
        if len(self.start) == 1:
            ok = 0
            for stare in self.scopuri:
                if self.start == stare:
                    ok = 1
                    break
            if ok == 0:
                return ok

        # caz cu 2 stive
        if len(self.start) == 2:
            elem_start = []
            linie_1 = self.start[0]
            for elem in linie_1:
                elem_start.append(elem)
            linie_2 = self.start[1]
            linie_2.reverse()
            for elem in linie_2:
                elem_start.append(elem)
            ok = 0
            for stare in self.scopuri:
                elem_final = []
                linie_1 = stare
                for elem in linie_1:
                    elem_final.append(elem)
                linie_2 = stare
                linie_2.reverse()
                for elem in linie_2:
                    elem_final.append(elem)
                if elem_start == elem_final:
                    ok = 1
                    break
            if ok == 0:
                return ok
        return 1

    def __init__(self, nume_fisier):

        def obtineStive(sir):
            stiveSiruri = sir.strip().split("\n")  # ["a","c b","d"]
            listaStive = [sirStiva.strip().split() if sirStiva != "#" else [] for sirStiva in stiveSiruri]
            return listaStive

        f = open(nume_fisier, 'r')

        continutFisier = f.read()  # citesc tot continutul fisierului
        # siruriStari = continutFisier.split("stari_finale")
        siruriStari = continutFisier
        self.start = obtineStive(siruriStari)  # [["a"], ["c","b"],["d"]]
        # self.scopuri = []
        # siruriStariFinale = siruriStari[1].strip().split("---")
        # for scop in siruriStariFinale:
        #     self.scopuri.append(obtineStive(scop))
        print("Stare Initiala:", self.start)
        # print("Stari finale posibile:", self.scopuri)
        input()

    def testeaza_scop(self, nodCurent):
        # return nodCurent.info in self.scopuri;
        return self.este_stare_finala(nodCurent.info)



    # va genera succesorii sub forma de noduri in arborele de parcurgere

    def genereazaSuccesori(self, nodCurent, tip_euristica="euristica banala"):
        listaSuccesori = []
        stive_c = nodCurent.info  # stivele din nodul curent
        nr_stive = len(stive_c)
        for idx in range(nr_stive):  # idx= indicele stivei de pe care iau bloc

            if len(stive_c[idx]) == 0:
                continue
            copie_interm = copy.deepcopy(stive_c)
            bloc = copie_interm[idx].pop()  # iau varful stivei
            for j in range(nr_stive):  # j = indicele stivei pe care pun blocul
                if idx == j:  # nu punem blocul de unde l-am luat
                    continue
                stive_n = copy.deepcopy(copie_interm)  # lista noua de stive
                stive_n[j].append(bloc)  # pun blocul
                costMutareBloc = 1 + ord(bloc) - ord("a")
                if not nodCurent.contineInDrum(stive_n):
                    nod_nou = NodParcurgere(stive_n, nodCurent, cost=nodCurent.g + costMutareBloc,
                                            h=self.calculeaza_h(stive_n, tip_euristica))
                    listaSuccesori.append(nod_nou)

        return listaSuccesori

    # euristica banala
    def calculeaza_h(self, infoNod, tip_euristica="euristica banala"):
        if tip_euristica == "euristica banala":
            # if infoNod not in self.scopuri:
            #     return 1  # se pune costul minim pe o mutare
            if not self.este_stare_finala(infoNod):
                return 1
            return 0
        elif tip_euristica == "euristica admisibila 1":
            # calculez cate blocuri nu sunt la locul fata de fiecare dintre starile scop, si apoi iau minimul dintre aceste valori
            euristici = []
            for (iScop, scop) in enumerate(self.scopuri):
                h = 0
                for iStiva, stiva in enumerate(infoNod):
                    for iElem, elem in enumerate(stiva):
                        try:
                            # exista în stiva scop indicele iElem dar pe acea pozitie nu se afla blocul din infoNod
                            if elem != scop[iStiva][iElem]:
                                h += ord(elem) - ord("a") + 1  # adun cu costul minim pe o mutare (adica costul lui a)
                        except IndexError:
                            # nici macar nu exista pozitia iElem in stiva cu indicele iStiva din scop
                            h += ord(elem) - ord("a") + 1
                euristici.append(h)
            return min(euristici)
        elif tip_euristica == "euristica admisibila 2":
            # calculez cate blocuri nu sunt la locul fata de fiecare dintre starile scop, si apoi iau minimul dintre aceste valori
            euristici = []
            for (iScop, scop) in enumerate(self.scopuri):
                h = 0
                for iStiva, stiva in enumerate(infoNod):
                    for iElem, elem in enumerate(stiva):
                        try:
                            # exista în stiva scop indicele iElem dar pe acea pozitie nu se afla blocul din infoNod
                            if elem != scop[iStiva][iElem]:
                                h += ord(elem) - ord("a") + 1
                            else:  # elem==scop[iStiva][iElem]:
                                if stiva[:iElem] != scop[iStiva][:iElem]:
                                    h += 2 * (ord(elem) - ord("a") + 1)
                        except IndexError:
                            # nici macar nu exista pozitia iElem in stiva cu indicele iStiva din scop
                            h += ord(elem) - ord("a") + 1
                euristici.append(h)
            return min(euristici)
        else:  # tip_euristica=="euristica neadmisibila"
            euristici = []
            for (iScop, scop) in enumerate(self.scopuri):
                h = 0
                for iStiva, stiva in enumerate(infoNod):
                    for iElem, elem in enumerate(stiva):
                        try:
                            # exista în stiva scop indicele iElem dar pe acea pozitie nu se afla blocul din infoNod
                            if elem != scop[iStiva][iElem]:
                                h += 3
                            else:  # elem==scop[iStiva][iElem]:
                                if stiva[:iElem] != scop[iStiva][:iElem]:
                                    h += 2
                        except IndexError:
                            # nici macar nu exista pozitia iElem in stiva cu indicele iStiva din scop
                            h += 3
                euristici.append(h)
            return max(euristici)

    def __repr__(self):
        sir = ""
        for (k, v) in self.__dict__.items():
            sir += "{} = {}\n".format(k, v)
        return (sir)


def breadth_first(gr, nrSolutiiCautate):
    # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
    c = [NodParcurgere(gr.start, None)]

    while len(c) > 0:
        # print("Coada actuala: " + str(c))
        # input()
        nodCurent = c.pop(0)

        if gr.testeaza_scop(nodCurent):
            print("Solutie:")
            nodCurent.afisDrum(afisCost=True, afisLung=True)
            print("\n----------------\n")
            input()
            nrSolutiiCautate -= 1
            if nrSolutiiCautate == 0:
                return
        lSuccesori = gr.genereazaSuccesori(nodCurent)
        c.extend(lSuccesori)


def a_star(gr, nrSolutiiCautate, tip_euristica):
    # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
    c = [NodParcurgere(gr.start, None, 0, gr.calculeaza_h(gr.start))]

    while len(c) > 0:
        nodCurent = c.pop(0)

        if gr.testeaza_scop(nodCurent):
            print("Solutie: ")
            nodCurent.afisDrum(afisCost=True, afisLung=True)
            print("\n----------------\n")
            input()
            nrSolutiiCautate -= 1
            if nrSolutiiCautate == 0:
                return
        lSuccesori = gr.genereazaSuccesori(nodCurent, tip_euristica=tip_euristica)
        for s in lSuccesori:
            i = 0
            gasit_loc = False
            for i in range(len(c)):
                # diferenta fata de UCS e ca ordonez dupa f
                if c[i].f >= s.f:
                    gasit_loc = True
                    break;
            if gasit_loc:
                c.insert(i, s)
            else:
                c.append(s)


gr = Graph("input.txt")
# if gr.verificare_configuratie_initiala():
#     nrSolutiiCautate = 3
#     a_star(gr, nrSolutiiCautate=3, tip_euristica="euristica banala")
# Rezolvat cu breadth first
"""
print("Solutii obtinute cu breadth first:")
breadth_first(gr, nrSolutiiCautate=3)
"""

print("\n\n##################\nSolutii obtinute cu A*:")
print("\nObservatie: stivele sunt afisate pe orizontala, cu baza la stanga si varful la dreapta.")
nrSolutiiCautate = 3
a_star(gr, nrSolutiiCautate=3, tip_euristica="euristica banala")

"""
a b c
d e
g


g e c
d a b
|

"""