import heapq
from copy import deepcopy
import cProfile, time

"""
Observatie pentru cei absenti la laborator: trebuie sa dati enter după fiecare afișare a cozii până vă apare o soluție. Afișarea era ca să vedem progresul algoritmului. Puteți să o dezactivați comentând print-ul cu coada și input()
"""


# informatii despre un nod din arborele de parcurgere (nu din graful initial)
class NodParcurgere:
    graf = None  # static

    def __init__(self, id, info, parinte, cost, h):
        self.id = id  # este indicele din vectorul de noduri
        self.info = info
        self.parinte = parinte  # parintele din arborele de parcurgere
        self.g = cost  # costul de la radacina la nodul curent
        self.h = h
        self.f = self.g + self.h

    def __eq__(self, nod_nou):
        return (self.g == nod_nou.g and self.f == nod_nou.f)

    def __lt__(self, nod_nou):
        return self.f < nod_nou.f or (self.f == nod_nou.f and self.g > nod_nou.g)

    def __lte__(self, nod_nou):
        return self.__lt__(nod_nou) or self.__eq__(nod_nou)

    def __gt__(self, nod_nou):
        return not self.__lte__(nod_nou)

    def obtineDrum(self):
        l = [self.info];
        nod = self
        while nod.parinte is not None:
            l.insert(0, nod.parinte.info)
            nod = nod.parinte
        return l

    def afisDrum(self):  # returneaza si lungimea drumului
        l = self.obtineDrum()
        print(("->").join(l))
        print("Cost: ", self.g)
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
        sir += self.info + "("
        sir += "id = {}, ".format(self.id)
        sir += "drum="
        drum = self.obtineDrum()
        sir += ("->").join(drum)
        sir += " g:{}".format(self.g)
        sir += " h:{}".format(self.h)

        sir += " f:{})".format(self.f)
        return (sir)


class Graph:  # graful problemei
    def __init__(self, noduri, matriceAdiacenta, matricePonderi, start, scopuri, lista_h):
        self.noduri = noduri
        self.matriceAdiacenta = matriceAdiacenta
        self.matricePonderi = matricePonderi
        self.nrNoduri = len(matriceAdiacenta)
        self.start = start
        self.scopuri = scopuri
        self.lista_h = lista_h

    def indiceNod(self, n):
        return self.noduri.index(n)

    def testeaza_scop(self, nodCurent):
        return nodCurent.info in self.scopuri;

    # va genera succesorii sub forma de noduri in arborele de parcurgere
    def genereazaSuccesori(self, nodCurent):
        listaSuccesori = []
        for i in range(self.nrNoduri):
            if self.matriceAdiacenta[nodCurent.id][i] == 1 and not nodCurent.contineInDrum(self.noduri[i]):
                nodNou = NodParcurgere(i, self.noduri[i], nodCurent, nodCurent.g + self.matricePonderi[nodCurent.id][i],
                                       self.calculeaza_h(self.noduri[i]))
                listaSuccesori.append(nodNou)
        return listaSuccesori

    def calculeaza_h(self, infoNod):
        return self.lista_h[self.indiceNod(infoNod)]

    def __repr__(self):
        sir = ""
        for (k, v) in self.__dict__.items():
            sir += "{} = {}\n".format(k, v)
        return (sir)


##############################################################################################
#                                 Initializare problema                                      #
##############################################################################################

# pozitia i din vectorul de noduri da si numarul liniei/coloanei corespunzatoare din matricea de adiacenta
noduri = ["a", "b", "c", "d", "e", "f", "g", "i", "j", "k"]

m = [
    [0, 1, 1, 1, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 1, 1, 0, 0, 0, 0],
    [0, 0, 0, 0, 1, 0, 1, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 1, 0, 0],
    [0, 0, 1, 0, 0, 1, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 1, 1, 0, 0, 1, 0, 0],
    [0, 0, 1, 0, 1, 0, 0, 0, 2, 1],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
]
mp = [
    [0, 3, 9, 7, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 4, 100, 0, 0, 0, 0],
    [0, 0, 0, 0, 10, 0, 5, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 4, 0, 0],
    [0, 0, 1, 0, 0, 10, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 1, 7, 0, 0, 1, 0, 0],
    [0, 0, 0, 0, 1, 0, 0, 0, 1, 1],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
]
start = "a"
scopuri = ["f"]
# exemplu de euristica banala (1 daca nu e nod scop si 0 daca este)
vect_h = [0, 10, 3, 7, 8, 0, 14, 3, 1, 2]

gr = Graph(noduri, m, mp, start, scopuri, vect_h)
NodParcurgere.graf = gr;

def a_star(gr):
    # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
    l_open = [NodParcurgere(gr.noduri.index(gr.start), gr.start, None, 0, gr.calculeaza_h(gr.start))]
    nr_elemente_inlocuite_open = 0
    nr_elemente_inlocuite_closed = 0

    # l_open contine nodurile candidate pentru expandare (este echivalentul lui c din A* varianta neoptimizata)

    # l_closed contine nodurile expandate
    l_closed = []
    while len(l_open) > 0:
        print("Coada actuala: " + str(l_open))
        input()
        nodCurent = l_open.pop(0)
        l_closed.append(nodCurent)
        if gr.testeaza_scop(nodCurent):
            print("Solutie: ", end="")
            nodCurent.afisDrum()
            print("\n----------------\n")
            print("Numarul de noduri din open inlocuite este: " + str(nr_elemente_inlocuite_open))
            print("Numarul de nodrui din closed inlocuite este: " + str(nr_elemente_inlocuite_closed))
            return
        lSuccesori = gr.genereazaSuccesori(nodCurent)
        for s in lSuccesori:
            gasitC = False
            for nodC in l_open:
                if s.info == nodC.info:
                    gasitC = True
                    if s.f >= nodC.f:
                        lSuccesori.remove(s)
                    else:  # s.f<nodC.f
                        print("A fost scos din open nodul: " + str(nodC))
                        l_open.remove(nodC)
                        nr_elemente_inlocuite_open += 1
                    break
            if not gasitC:
                for nodC in l_closed:
                    if s.info == nodC.info:
                        if s.f >= nodC.f:
                            lSuccesori.remove(s)
                        else:  # s.f<nodC.f
                            print("A fost scos din closed nodul: " + str(nodC))
                            l_closed.remove(nodC)
                            nr_elemente_inlocuite_closed += 1
                        break
        for s in lSuccesori:
            i = 0
            gasit_loc = False
            for i in range(len(l_open)):
                # diferenta fata de UCS e ca ordonez crescator dupa f
                # daca f-urile sunt egale ordonez descrescator dupa g
                if l_open[i].f > s.f or (l_open[i].f == s.f and l_open[i].g <= s.g):
                    gasit_loc = True
                    break
            if gasit_loc:
                l_open.insert(i, s)
            else:
                l_open.append(s)
            print("A fost adaugat in open nodul " + str(s))

def get_list_from_coada(l_open):
    # vectorul avea structura de heap
    l_sortat = []
    aux = deepcopy(l_open)
    while len(aux) > 0:
        l_sortat.append(heapq.heappop(aux))
    return l_sortat


def a_star2(gr):
    # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
    l_open = [NodParcurgere(gr.noduri.index(gr.start), gr.start, None, 0, gr.calculeaza_h(gr.start))]
    nr_elemente_inlocuite_open = 0
    nr_elemente_inlocuite_closed = 0
    time0 = time.time()
    # l_open contine nodurile candidate pentru expandare (este echivalentul lui c din A* varianta neoptimizata)

    # l_closed contine nodurile expandate
    l_closed = dict()
    while len(l_open) > 0:
        print("Coada actuala: " + str(get_list_from_coada(l_open)))
        # input()
        nodCurent = heapq.heappop(l_open)
        l_closed[nodCurent.info] = nodCurent
        if gr.testeaza_scop(nodCurent):
            print("Solutie: ", end="")
            nodCurent.afisDrum()
            print("\n----------------\n")
            print("Numarul de noduri din open inlocuite este: " + str(nr_elemente_inlocuite_open))
            print("Numarul de nodrui din closed inlocuite este: " + str(nr_elemente_inlocuite_closed))
            print("l_closed: {}".format(str(l_closed)))
            print("Timp: " + str(time.time() - time0))
            return
        lSuccesori = gr.genereazaSuccesori(nodCurent)
        for s in lSuccesori:
            gasitC = False
            for nodC in l_open:
                if s.info == nodC.info:
                    gasitC = True
                    if s.f >= nodC.f:
                        lSuccesori.remove(s)
                    else:  # s.f<nodC.f
                        print("A fost scos din open nodul: " + str(nodC))
                        l_open.remove(nodC)
                        heapq.heapify(l_open)  # scot nodul curent din Heap, refac heap-ul cu lista care mi-a ramas
                        nr_elemente_inlocuite_open += 1
                    break
            if not gasitC:
                if s.info in l_closed:
                    if s.f >= l_closed[s.info].f:
                        lSuccesori.remove(s)
                    else:  # s.f<nodC.f
                        print("A fost scos din closed nodul: " + str(l_closed[s.info]))
                        l_closed.pop(s.info)
                        nr_elemente_inlocuite_closed += 1
                    break
        for s in lSuccesori:
            heapq.heappush(l_open, s)
            # i = 0
            # gasit_loc = False
            # for i in range(len(l_open)):
            #     # diferenta fata de UCS e ca ordonez crescator dupa f
            #     # daca f-urile sunt egale ordonez descrescator dupa g
            #     if l_open[i].f > s.f or (l_open[i].f == s.f and l_open[i].g <= s.g):
            #         gasit_loc = True
            #         break
            # if gasit_loc:
            #     l_open.insert(i, s)
            # else:
            # l_open.append(s)
            print("A fost adaugat in open nodul " + str(s))


# 1,4,5 8,10 <---9			20
# (f=10,g=7)(f=14,g=3)    <----(f=10,g=3)

# a_star2(gr)
# cProfile.run("a_star2(gr)")
cProfile.run("a_star(gr)")