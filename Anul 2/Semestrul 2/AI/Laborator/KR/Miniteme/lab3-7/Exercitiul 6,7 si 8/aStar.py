from queue import PriorityQueue
from copy import deepcopy
import cProfile
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

    # operatori
    def __eq__(self, nod_nou):
        return (self.g == nod_nou.g and self.f == nod_nou.f)

    def __lt__(self, nod_nou):
        return self.f < nod_nou.f or (self.f == nod_nou.f and self.g > nod_nou.g)


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
    def __init__(self, noduri, matriceAdiacenta, costuri, start, scopuri, lista_h):
        self.noduri = noduri
        self.matriceAdiacenta = matriceAdiacenta
        # self.matricePonderi = matricePonderi
        self.costuri = costuri
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
                nodNou = NodParcurgere(i, self.noduri[i], nodCurent, nodCurent.g + self.costuri[i],
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
    [0, 0, 1, 0, 1, 0, 0, 0, 1, 1],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
]

costuri = [3, 6, 9, 10, 11, 21, 1, 2, 7, 9]

# mp = [
#     [0, 3, 9, 7, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 4, 100, 0, 0, 0, 0],
#     [0, 0, 0, 0, 10, 0, 5, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 4, 0, 0],
#     [0, 0, 1, 0, 0, 10, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 1, 7, 0, 0, 1, 0, 0],
#     [0, 0, 0, 0, 1, 0, 0, 0, 1, 1],
#     [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
# ]
start = "a"
scopuri = ["f"]
# exemplu de euristica banala (1 daca nu e nod scop si 0 daca este)
vect_h = [0, 10, 3, 7, 8, 0, 14, 3, 1, 2]

gr = Graph(noduri, m, costuri, start, scopuri, vect_h)
NodParcurgere.graf = gr;



def a_star(gr, nrSolutiiCautate):
    # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
    c = [NodParcurgere(gr.indiceNod(gr.start), gr.start, None, 0, gr.calculeaza_h(gr.start))]

    while len(c) > 0:
        print("Coada actuala: " + str(c))
        input()
        nodCurent = c.pop(0)

        if gr.testeaza_scop(nodCurent):
            print("Solutie: ")
            nodCurent.afisDrum()
            print("\n----------------\n")
            input()
            nrSolutiiCautate -= 1
            if nrSolutiiCautate == 0:
                return
        lSuccesori = gr.genereazaSuccesori(nodCurent)
        for s in lSuccesori:
            i = 0
            gasit_loc = False
            for i in range(len(c)):
                # diferenta fata de UCS e ca ordonez dupa f
                if c[i].f > s.f:
                    gasit_loc = True
                    break
                elif c[i].f == s.f and c[i].g < s.g:
                    gasit_loc = True
                    break
            if gasit_loc:
                c.insert(i, s)
            else:
                c.append(s)

def get_list_from_coada(c):
    # vectorul avea structura de heap
    l_sortat = []
    aux = PriorityQueue()
    aux.queue = deepcopy(c.queue)
    while not aux.empty():
        l_sortat.append(aux.get())
    return l_sortat

def a_star2(gr, nrSolutiiCautate):
    # in coada vom avea doar noduri de tip NodParcurgere (nodurile din arborele de parcurgere)
    # c = [NodParcurgere(gr.indiceNod(gr.start), gr.start, None, 0, gr.calculeaza_h(gr.start))]
    c = PriorityQueue()
    c.put(NodParcurgere(gr.indiceNod(gr.start), gr.start, None, 0, gr.calculeaza_h(gr.start)))

    while not c.empty():
        print("Coada actuala: " + str(get_list_from_coada(c)))
        # input()
        nodCurent = c.get()

        if gr.testeaza_scop(nodCurent):
            print("Solutie: ")
            nodCurent.afisDrum()
            print("\n----------------\n")
            input()
            nrSolutiiCautate -= 1
            if nrSolutiiCautate == 0:
                return
        lSuccesori = gr.genereazaSuccesori(nodCurent)
        for s in lSuccesori:
            c.put(s)

            # i = 0
            # gasit_loc = False
            # for i in range(len(c)):
            #     # diferenta fata de UCS e ca ordonez dupa f
            #     if c[i].f > s.f:
            #         gasit_loc = True
            #         break
            #     elif c[i].f == s.f and c[i].g < s.g:
            #         gasit_loc = True
            #         break
            # if gasit_loc:
            #     c.insert(i, s)
            # else:
            #     c.append(s)


# 5  2,4,8,10

a_star2(gr, nrSolutiiCautate=3)