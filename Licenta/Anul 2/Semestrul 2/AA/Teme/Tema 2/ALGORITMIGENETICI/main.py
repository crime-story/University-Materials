from math import log2
import random
import matplotlib.pyplot as plt

class Cromozom:
    def __init__(self, fin, fout):
        def generarePopulatie(self):
            self.populatie = []
            for i in range(self.nrPopulatie):  # generam n cromozomi
                l = []
                for j in range(self.lCromozom):
                    l.append(random.choice([0, 1]))  # generam random prima generatie
                self.populatie.append(l)

        self.debug = True  # afisare detaliata in fisierul de iesire
        self.fout = fout
        s = fin.readline().split()
        self.nrPopulatie = int(s[-1])  # nr de cromozomi
        s = fin.readline().split()
        self.domain = (int(s[-2]), int(s[-1]))  # domeniul de definite al functiei
        s = fin.readline().split()
        self.function = (int(s[-3]), int(s[-2]), int(s[-1]))  # coeficientii functiei
        s = fin.readline().split()
        self.precisiton = int(s[-1])  # precizia de impartire a intervalului, din care vom calcula lungimea cromozomului
        s = fin.readline().split()
        self.pRecombinare = float(s[-1])  # procentul de crossingover
        s = fin.readline().split()
        self.pMutatie = float(s[-1])  # procentul de mutatie
        self.lCromozom = int(log2((self.domain[1] - self.domain[0]) * (10 ** self.precisiton)))  # Lungimea cromozomului
        self.populatie = []  # cromozomii generatiei actuale
        generarePopulatie(self)  # generam random prima generatie
        self.co = 1  # inicele generatiei

    def decodificarePopulatie(self):
        decod = []
        for i in self.populatie:  # pt fiecare cromozam
            b10 = 0  # calculam nr in baza 10
            for j in i:  # si ii aflam valoarea x
                b10 = b10 * 2 + j
            decod.append(((self.domain[1] - self.domain[0]) / ((1 << self.lCromozom) - 1)) * b10 + self.domain[0])
        return decod

    def fitnessPopulatie(self):  # calculam f(x)
        fit = []
        decod = self.decodificarePopulatie()
        if self.debug:
            self.fout.write("Generation " + str(self.co) + ":\n")
        co = 0
        for x in decod:  # pt fiecare x, calculam f(x); functia de fitness e chiar functia data
            fit.append(self.function[0] * x * x + self.function[1] * x + self.function[2])
            if self.debug:
                self.fout.write(str(co) + ". " + str(self.populatie[co]) + " x=" + str(x) + " f=" + str(fit[-1]) + '\n')
            co += 1
        return (decod, fit)

    def probabilitateSelectie(self):
        decod, fit = self.fitnessPopulatie()
        fitnessTotal = sum(fit)  # luam suma totala de fitness
        prob = []

        if self.debug:
            self.fout.write("\nProbabilitate selectie:\n")
        co = 0
        for i in fit:
            prob.append(i / fitnessTotal)  # calculam probabilitatea de selectie a fiecarui elem
            if self.debug:
                self.fout.write(str(co) + ". " + str(i) + '\n')
            co += 1

        return (decod, fit, prob)  #decodificarea cromozomului, fitnessul lui si probabilitatea sa de selectie

    def nextGeneration(self):
        def selectPopulation():  # selectam candidatii pt noua generatie
            decod, fit, prob = self.probabilitateSelectie()
            newPopulation = [
                self.populatie[fit.index(max(fit))].copy()]  # criteriu elitist - adaugam cel mai fit membru

            # generam intervalul de probabilitati
            for i in range(1, len(prob)):
                prob[i] += prob[i - 1]

            if self.debug:
                self.fout.write("\nInterval: " + str(prob) + '\n\n')

            for i in range(self.nrPopulatie - 1):  # mai luam n-1 cromozomi
                nr = random.random()  # generam random un nr intre 0 si 1

                # cautam binar carui cromozom apartine nr generat folosind int
                p = 1 << (int(log2(len(prob))))
                idx = 0
                while p > 0:
                    if idx + p < len(prob) and prob[idx + p] <= nr:
                        idx += p
                    p >>= 1
                if idx + 1 < len(prob):
                    idx += 1

                newPopulation.append(self.populatie[idx].copy())
                if self.debug:
                    self.fout.write("u=" + str(nr) + " cromozom:" + str(idx) + '\n')
            return newPopulation

        def crossingOver(population):  # facem crossingover
            def crossOver(crom):
                r = random.random()
                if r < self.pRecombinare:  # doar pt un anumit procent din cromozomi
                    brPoint = random.choice(range(self.lCromozom))
                    if self.debug:
                        self.fout.write("BrPoint: " + str(brPoint) + " :->: " + str(crom))
                    aux = [0 for i in range(self.lCromozom)]
                    for i in range(brPoint, self.lCromozom):  # iau in aux ultimele gene alele
                        aux[i] = crom[0][i]
                    for i in range(1, len(crom)):
                        for j in range(brPoint, self.lCromozom):
                            aux2 = aux[j]  # schimb genele intre cromozomul actual si cel anterior
                            aux[j] = crom[i][j]
                            crom[i][j] = aux2
                    for i in range(brPoint, self.lCromozom):
                        crom[0][i] = aux[i]  # ii atribui primului cromozom, valorile ultimului
                    if self.debug:
                        self.fout.write(' :->: ' + str(crom) + '\n')
                return crom

            if self.debug:
                self.fout.write("Cromozomes with recombination:\n")
            newPopulation = [population[0]]  # pe prima poz avem elem maxim care va trece nemodificat in noua generatie
            population[0] = population.pop()  # deci il scoatem, si mutam in locul lui, elem de pe ultima poza

            random.shuffle(population)  # ameste vectorul

            if len(population) % 2 == 1 and len(population) >= 3:  # daca am nr impar, pe ultimii 3 ii iau grupati
                newPopulation.extend(crossOver([population.pop(), population.pop(), population.pop()]))
            while len(population) > 0:
                newPopulation.extend(crossOver([population.pop(), population.pop()]))  # ii grupez 2 cate 2

            return newPopulation

        def mutation(population):
            for i in range(1, self.nrPopulatie):  # ignoram elem cu criteriu elitist care va trece nemodif
                for j in range(self.lCromozom):  # parcurg toate genele celorlalti cromozomi
                    r = random.random()  # pt un procent mic
                    if r < self.pMutatie:  # schimb gena
                        population[i][j] = 1 - population[i][j]
            return population

        intermediatePopulation = selectPopulation()  # selectam populatia initiala
        if self.debug:
            self.fout.write("\nselected population: " + str(intermediatePopulation) + '\n\n')
        intermediatePopulation = crossingOver(intermediatePopulation)  # facem cross over
        if self.debug:
            self.fout.write("\nafter crossover population: " + str(intermediatePopulation) + '\n')
        self.populatie = mutation(intermediatePopulation)  # populatia finala, dupa mutatie
        if self.debug:
            self.fout.write("\nnew population: " + str(intermediatePopulation) + '\n\n')
        self.co += 1  # incrementam nr generatiei


fin = open("date.in")
fout = open("sol.out", "w")
cromozom = Cromozom(fin, fout)
s = fin.readline().split()
nrEtape = int(s[-1])

xpoints = []
ypoints = []
for i in range(nrEtape):
    cromozom.nextGeneration()
    cromozom.debug = False

    x, l = cromozom.fitnessPopulatie()
    fout.write(str(i + 1) + ". Max number:" + str(max(l)) + "; avg: " + str(sum(l) / len(l)) + ' x: ' + str(
        x[l.index(max(l))]) + '\n')
    xpoints.append(x[l.index(max(l))])
    ypoints.append(max(l))

plt.plot(xpoints,ypoints)
plt.xlabel("generation")
plt.ylabel("maximum value")
plt.show()
fin.close()
fout.close()
