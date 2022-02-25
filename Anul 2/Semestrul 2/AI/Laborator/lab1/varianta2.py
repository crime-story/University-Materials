import random
import time
from copy import deepcopy


class Activitate:
    def __init__(self, nume, factor_sanatate, factor_inteligenta, factor_oboseala, factor_dispozitie, durata):
        self.nume = nume
        self.factor_sanatate = int(factor_sanatate)
        self.factor_inteligenta = int(factor_inteligenta)
        self.factor_oboseala = int(factor_oboseala)
        self.factor_dispozitie = int(factor_dispozitie)
        self.durata = int(durata)

    def __str__(self) -> str:
        return self.nume + " " + str(self.factor_sanatate) + " " + str(self.factor_inteligenta) + " " + str(
            self.factor_oboseala) + " " + str(self.factor_dispozitie) + " " + str(self.durata)


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
        self.rapoarte = dict()
        self.ora_curenta = 9
        self.activitate_curenta: Activitate = None
        self.timp_executat_activ = 0

    def __repr__(self):  # de facut
        self.afiseaza_raport()
        return "{} => snt: {}, intel: {}, obos: {}, dispoz: {}".format(self.nume, self.sanatate, self.inteligenta,
                                                                       self.oboseala, self.buna_dispozitie)

    def actualizare_raport(self):
        self.rapoarte[self.activitate_curenta] = self.timp_executat_activ

    def afiseaza_raport(self):
        for activity in self.rapoarte:
            print(str(activity.nume) + ": " + str(self.rapoarte[activity]) + "/" + str(activity.durata))

    def testeaza_final(self, debug=False):
        if self.sanatate == 0 or self.buna_dispozitie == 0:
            # daca sanatatea sau buna dispozitie au ajuns 0, am gasit o stare finala si returnez true
            if debug:
                print("Din pacate, ati ajuns la spital...")
            return True
        elif self.inteligenta == 100:
            # daca inteligenta a ajusn 100, am gasit o stare finala returnez true
            if debug:
                print("Felicitari, elevul a terminat scoala!")
            return True
        else:
            # altfel inseamna ca nu am ajuns intr-o stare finala
            return False

    def trece_ora(self):

        if self.activitate_curenta.durata <= self.timp_executat_activ or self.testeaza_final():
            # daca elevul a petrecut deja timpul alocat activitatii sau daca e in stare finala
            return False

        ok = True

        factor_oboseala = self.activitate_curenta.factor_oboseala
        factor_inteligenta = self.activitate_curenta.factor_inteligenta
        factor_dispozitie = self.activitate_curenta.factor_dispozitie
        factor_sanatate = self.activitate_curenta.factor_sanatate
        nume_activitate_curenta = self.activitate_curenta.nume

        self.oboseala += factor_oboseala
        if self.oboseala > 100:
            self.oboseala = 100
        elif self.oboseala < 0:
            self.oboseala = 0

        if self.oboseala == 100:
            if factor_inteligenta > 0:
                factor_inteligenta = factor_inteligenta // 2
            if factor_dispozitie > 0:
                factor_dispozitie = factor_dispozitie // 2
            if factor_sanatate > 0:
                factor_sanatate = factor_sanatate // 2

        if (self.ora_curenta >= 22 or self.ora_curenta < 6) and nume_activitate_curenta != "dormit":
            self.sanatate += factor_sanatate - 1
        else:
            self.sanatate += factor_sanatate

        if self.sanatate < 0:
            self.sanatate = 0
            ok = False
        elif self.sanatate > 100:
            self.sanatate = 100

        self.inteligenta += factor_inteligenta
        if self.inteligenta > 100:
            self.inteligenta = 100
            ok = False
        elif self.inteligenta < 0:
            self.inteligenta = 0

        self.buna_dispozitie += factor_dispozitie
        if self.buna_dispozitie > 100:
            self.buna_dispozitie = 100
        elif self.buna_dispozitie < 0:
            self.buna_dispozitie = 0
            ok = False

        self.ora_curenta += 1
        if self.ora_curenta > 24:
            self.ora_curenta = 1

        return ok

    def desfasoara_activitate(self, activity):
        if activity != self.activitate_curenta:
            self.activitate_curenta = activity
            self.timp_executat_activ = 0

        if self.trece_ora():
            self.timp_executat_activ += 1
            self.actualizare_raport()
            return True

        return False


def porneste_simulare(default_lists_of_activities):
    numar_elevi = int(input("Introduceti numarul de elevi: "))

    for i in range(numar_elevi):
        elev = Elev()
        ok = False
        elev_list_of_activities = deepcopy(default_lists_of_activities)
        print("ORA START: " + str(elev.ora_curenta) + ":00")
        get_new_activity = True
        while len(elev_list_of_activities) and not elev.testeaza_final():
            try:
                word = input("Doriti sa continuati?\n Scrieti 'gata' pentru a opri simularea\n").lower()

                if word == "gata":
                    break
                elif word == "continua":
                    timp = float('+inf')
                else:
                    timp = int(word)

                while not elev.testeaza_final() and timp:
                    print("ORA CURENTA: " + str(elev.ora_curenta) + ":00")

                    if get_new_activity:
                        if len(elev_list_of_activities) == 0:
                            break
                        random_activity = random.choice(elev_list_of_activities)
                        elev_list_of_activities.remove(random_activity)
                        get_new_activity = False

                    if not elev.desfasoara_activitate(random_activity):
                        get_new_activity = True
                        continue
                    timp -= 1
                    if timp != float('+inf'):
                        print(elev)
                    time.sleep(1)  # asteptam 1 secunda interval intre 2 ore
            except Exception as e:
                print(e)
        print(elev)
        print("Stare finala: ", elev.testeaza_final(debug=True))


if __name__ == "__main__":
    fin = open("date.in", "r")
    input_file = fin.readlines()
    fin.close()

    lenght_of_input_file = len(input_file)
    lists_of_activities = []

    for i in range(lenght_of_input_file):
        current_line = input_file[i].split()
        lists_of_activities.append(
            Activitate(current_line[0], current_line[1], current_line[2], current_line[3], current_line[4],
                       current_line[5]))

    porneste_simulare(lists_of_activities)
