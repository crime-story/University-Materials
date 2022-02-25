import random
import time

class Activitate:
    def __init__(self, nume, factor_sanatate, factor_inteligenta, factor_oboseala, factor_dispozitie, durata):
        self.nume = nume
        self.factor_sanatate = int(factor_sanatate)
        self.factor_inteligenta = int(factor_inteligenta)
        self.factor_oboseala = int(factor_oboseala)
        self.factor_dispozitie = int(factor_dispozitie)
        self.durata = int(durata)


class Elev:
    id = 0

    @classmethod
    def increment_nume(cls):
        cls.id += 1
        return cls.id

    def __init__(self, nume=None, sanatate=90, inteligenta=20, oboseala=0, buna_dispozitie=100):
        if nume != None:
            self.nume = nume
        else:
            id_nume = self.increment_nume()
            self.nume = "Necunoscut_" + str(id_nume)
        self.inteligenta = inteligenta
        self.sanatate = sanatate
        self.oboseala = oboseala
        self.buna_dispozitie = buna_dispozitie

    def __repr__(self, dictionar):  # de facut
        sir = " (" + "snt: " + str(self.sanatate) + ", obos: " + str(self.oboseala) + ", dispoz: " + str(self.buna_dispozitie) + ")"
        return sir

    def afiseaza_raport(self, nume_activitate, timp_executat_activ, durata_max, dictionar):
        value = str(nume_activitate) + ": " + str(timp_executat_activ) + "/" + str(durata_max)
        dictionar.update({self.nume: value})
        return dictionar

    def testeaza_final(self):
        if self.sanatate == 0 or self.buna_dispozitie == 0:
            # daca sanatatea sau buna dispozitie au ajuns 0, am gasit o stare finala si returnez true
            return True
        elif self.inteligenta == 100:
            # daca inteligenta a ajusn 100, am gasit o stare finala returnez true
            print("Felicitari, elevul a terminat scoala!")
            return True
        else:
            # altfel inseamna ca nu am ajuns intr-o stare finala
            return False

    def trece_ora(self, nume_activitate_curenta, factor_sanatate, factor_inteligenta, factor_oboseala,
                  factor_dispozitie, durata_max,
                  timp_executat_activ, ora_curenta):
        if durata_max < timp_executat_activ:
            # daca elevul a petrecut deja timpul alocat activitatii
            return False

        interval_ore = False
        if (ora_curenta >= 22 or ora_curenta <= 6) and nume_activitate_curenta != "dormit":
            verif1 = self.sanatate + factor_sanatate - 1
            interval_ore = True
        else:
            verif1 = self.sanatate + factor_sanatate
        verif2 = self.inteligenta + factor_inteligenta
        verif3 = self.oboseala + factor_oboseala
        verif4 = self.buna_dispozitie + factor_dispozitie

        if verif1 > 100 or verif1 < 0 or verif2 > 100 or verif2 < 0 or verif3 > 100 or verif3 < 0 or verif4 > 100 or verif4 < 0:
            return False

        if interval_ore:
            self.sanatate -= 1

        self.sanatate += factor_sanatate
        self.inteligenta += factor_inteligenta
        self.oboseala += factor_oboseala
        self.buna_dispozitie += factor_dispozitie

        return True

    def desfasoara_activitate(self, activity, ora_curenta):
        activitate_curenta = activity
        timp_executat_activ = 0

        for camp in activitate_curenta:
            nume_activitate_curent = camp.nume
            factor_sanatate = camp.factor_sanatate
            factor_inteligenta = camp.factor_inteligenta
            factor_oboseala = camp.factor_oboseala
            factor_dispozitie = camp.factor_dispozitie
            durata_max = camp.durata

        for i in range(durata_max):
            if self.trece_ora(nume_activitate_curent, factor_sanatate, factor_inteligenta, factor_oboseala,
                              factor_dispozitie, durata_max,
                              timp_executat_activ, ora_curenta):
                timp_executat_activ += 1
                continue
            break

        dictionar = {}

        self.afiseaza_raport(nume_activitate_curent, timp_executat_activ, durata_max, dictionar)
        if timp_executat_activ:
            for k,v in dictionar.items():
                print(k + " " + v + self.__repr__(dictionar))

        return timp_executat_activ


def porneste_simulare(lists_of_activities):
    numar_elevi = int(input("Introduceti numarul de elevi: "))
    numar_ore =  int(input("Introduceti numarul de ore pentru elevi: "))
    copy = numar_ore
    
    for i in range(numar_elevi):
        elev = Elev()
        ora_start = 9
        ok = False
        print("ORA CURENTA: " + str(ora_start) + ":00")

        while True:
            if ok:
                ora_start += 1
                print("ORA CURENTA: " + str(ora_start) + ":00")
            random_activity = random.choice(lists_of_activities)

            for data in random_activity:
                numar_max_ore_random = data.durata

            while numar_max_ore_random > numar_ore:
                random_activity = random.choice(lists_of_activities)
                for data in random_activity:
                    numar_max_ore_random = data.durata

            numar_ore_facute = elev.desfasoara_activitate(random_activity, ora_start)

            if not numar_ore_facute:
                ok = False
            else:
                ok = True
                ora_start += 1

            aux = numar_ore - numar_ore_facute

            if aux > 0:
                numar_ore -= numar_ore_facute
            else:
                break

            if elev.testeaza_final():  # daca a ajuns intr-o stare finala
                break

            if elev.oboseala == 100:
                elev.inteligenta /= 2
                elev.sanatate /= 2
                elev.buna_dispozitie /= 2

            if ora_start > 24:
                ora_start = 1

            time.sleep(1)  # asteptam 1 secunda interval intre 2 ore

        word = input("Doriti sa continuati?\n Scrieti 'gata' pentru a opri simularea\n").lower()

        if word == "gata":
            break
        elif word == "continua":
            numar_ore = copy
            continue


if __name__ == "__main__":
    fin = open("date.in", "r")
    input_file = fin.readlines()
    fin.close()

    lenght_of_input_file = len(input_file)
    lists_of_activities = []

    for i in range(lenght_of_input_file):
        current_line = input_file[i].split()
        lists_of_activities.append([
            Activitate(current_line[0], current_line[1], current_line[2], current_line[3], current_line[4],
                       current_line[5])])

    porneste_simulare(lists_of_activities)
