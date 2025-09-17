import tkinter
import functools
import copy
import time
import statistics


class Stare:
    decodif_poz_matrice = [(1, 1), (1, 4), (1, 7), (2, 2), (2, 4), (2, 6), (3, 3), (3, 4), (3, 5), (4, 1), (4, 2), (4, 3), (4, 5), (4, 6), (4, 7), (5, 3), (5, 4), (5, 5), (6, 2), (6, 4), (6, 6), (7, 1), (7, 4), (7, 7)]

    @staticmethod
    def generare_matrice():
        """
        Jocul este tinut pe o matrice 8x3
        None=Nu exista nicio piesa intr-o casuta, True=piesa alba, False=piesa neagra
        :return: Intoarce o matrice initiala
        """
        return [[None for _ in range(3)] for _ in range(8)]

    def __init__(self, matrix, piese_albe_nefolosite=9, piese_albe_pe_tabla=0, piese_negre_nefolosite=9, piese_negre_pe_tabla=0, jucator_curent_alb=True, tata=None, se_scoate_o_piesa=False):
        """
        Starea este retinuta sub forma unei matrici 8x3, cate o linie pt fiecare rand (linia din mijloc cu 6 intrari se considera doua linii separate, una pt stanga si una pt dreapta)
        :param piese_albe_nefolosite: nr de piese ce mai trb puse pe tabla (etapa1). Initial fiecare jucator are 9 piese
        :param piese_albe_pe_tabla: cate piese se gasesc pe tabla (fara cele din mana si fara cele scoase din joc)
        :param piese_negre_nefolosite: idem
        :param piese_negre_pe_tabla: idem
        :param jucator_curent_alb: ce jucator urmeaza sa mute. True daca e alb, False daca e Negru
        :param tata: starea din care s-a generat aceasta mutare
        :param matrix: matricea actuala a tablei de bord
        :param se_scoate_o_piesa: True daca la aceasta mutare se elimina o piesa a adversarului, False daca se continua jocul
        :param self.l_succesori va retine lista cu mutari posibile sau None daca nu a fost inca generata
        :param self.estimare va retine scorul estimat de euristica aleasa sau None daca nu a fost inca estimat
        :param self.decodif_poz_matrice contine pozitile reale pe care se afla butoanele
        """
        self.tata = tata
        self.matrix = matrix
        self.piese_albe_nefolosite = piese_albe_nefolosite
        self.piese_albe_pe_tabla = piese_albe_pe_tabla
        self.piese_negre_nefolosite = piese_negre_nefolosite
        self.piese_negre_pe_tabla = piese_negre_pe_tabla
        self.jucator_curent_alb = jucator_curent_alb
        self.se_scoate_o_piesa = se_scoate_o_piesa
        self.l_succesori = None
        self.estimare = None

    def __eq__(self, o):
        """
        Doua stari sunt echivalente daca cele doua matrici sunt identice, fiecare jucator are tot atate piese, daca urmeaza sa mute acelasi jucator si daca este o mutare in care se
        elimina sau nu piese
        :param o: obiectul cu care se compara
        :return: True daca starile sunt echivalente
        """
        if self.__class__ == o.__class__:
            return self.matrix == o.matrix and self.jucator_curent_alb == o.jucator_curent_alb and self.piese_albe_nefolosite == o.piese_albe_nefolosite and \
                   self.piese_albe_pe_tabla == o.piese_albe_pe_tabla and self.piese_negre_nefolosite == o.piese_negre_nefolosite and self.piese_negre_pe_tabla == o.piese_negre_pe_tabla \
                   and self.se_scoate_o_piesa == o.se_scoate_o_piesa
        else:
            return False

    def print_matrix(self):
        """
        afiseaza matricea in consola pentru debug
        :return: None
        """
        for i in range(len(self.matrix)):
            if i != 3:
                print(self.matrix[i])
            else:
                print(self.matrix[i], end="     ")
        print("...............")

    @classmethod
    def este_in_moara(cls, matrix, poz):
        """
        Verifica daca o piesa dintr-o matrice se afla intr-o configuratie tip moara
        :param matrix: o matrice 8x3 pe care se face verificarea
        :param poz: pozitia pentru care se verifica
        :return: True daca piesa respectiva se afla in moara, False altfel
        """
        lin_reala, col_reala = cls.decodif_poz_matrice[poz[0] * 3 + poz[1]]
        ok_moara_col = True
        ok_moara_lin = True
        for i in range(len(cls.decodif_poz_matrice)):
            lin_in_matrice = i // 3
            col_in_matrice = i % 3
            if cls.decodif_poz_matrice[i][0] == lin_reala:
                if matrix[lin_in_matrice][col_in_matrice] != matrix[poz[0]][poz[1]] and (lin_reala != 4 or not (col_reala < 4 < cls.decodif_poz_matrice[i][1] or col_reala > 4 > cls.decodif_poz_matrice[i][1])):
                    ok_moara_col = False
            if cls.decodif_poz_matrice[i][1] == col_reala:
                if matrix[lin_in_matrice][col_in_matrice] != matrix[poz[0]][poz[1]] and (col_reala != 4 or not (lin_reala < 4 < cls.decodif_poz_matrice[i][0] or lin_reala > 4 > cls.decodif_poz_matrice[i][0])):
                    ok_moara_lin = False
        return ok_moara_lin or ok_moara_col

    @classmethod
    def aproape_moara(cls, matrix, poz):
        """
        Aproape moara înseamna ca pe o linie sau o coloana exista doua piese de aceeași culoare
        :param matrix:o matrice 8x3 pe care se face verificarea
        :param poz:pozitia pentru care se verifica
        :return:True daca piesa respectiva se afla pe aceeași linie sau coloana cu o alta piesa de aceeași culoare, False altfel
        """
        lin_reala, col_reala = cls.decodif_poz_matrice[poz[0] * 3 + poz[1]]
        for i in range(len(cls.decodif_poz_matrice)):
            lin_in_matrice = i // 3
            col_in_matrice = i % 3
            if matrix[lin_in_matrice][col_in_matrice] == matrix[poz[0]][poz[1]] and matrix[lin_in_matrice][col_in_matrice] is not None:
                if cls.decodif_poz_matrice[i][0] == lin_reala and cls.decodif_poz_matrice[i][1] != col_reala:
                    if lin_reala != 4 or not (col_reala < 4 < cls.decodif_poz_matrice[i][1] or col_reala > 4 > cls.decodif_poz_matrice[i][1]):
                        return True
                if cls.decodif_poz_matrice[i][1] == col_reala and cls.decodif_poz_matrice[i][0] != lin_reala:
                    if col_reala != 4 or not (lin_reala < 4 < cls.decodif_poz_matrice[i][0] or lin_reala > 4 > cls.decodif_poz_matrice[i][0]):
                        return True
        return False

    @classmethod
    def se_poate_deplasa(cls, matrix, poz):
        """
        Aceasta classmethod verifica daca o piesa este libera sau blocate. Este folositoare in scrierea euristicilor
        :param matrix: o matrice 8x3 pe care se face verificarea
        :param poz: pozitia pentru care se verifica
        :return: True daca piesa se poate deplasa; False daca piesa e blocata
        """
        linie_reala_actuala, coloana_reala_actuala = Stare.decodif_poz_matrice[poz[0] * 3 + poz[1]]
        for lin, col in [(-1, 0), (0, 1), (1, 0), (0, -1)]:
            try:
                idx_pozitie_noua = cls.decodif_poz_matrice.index((linie_reala_actuala + lin * max(1, abs(coloana_reala_actuala - 4)), coloana_reala_actuala + col * max(1, abs(linie_reala_actuala - 4))))
                new_lin = idx_pozitie_noua // 3
                new_col = idx_pozitie_noua % 3
                if matrix[new_lin][new_col] is None:
                    return True
            except ValueError:
                continue
        return False

    def generare_succesori(self):
        """
        Va parcurge toate elementele din matrix. Daca nu mai sunt piese de adaugat, pentru elementele care au aceeasi culoare cu a jucatorului curent,
        se vor incerca din cele maxim 4 directi posibile de deplasare, care sunt libere si se va genera noul succesor
        :return: intoarce lista mutarilor posibile pentru jucatorul curent
        """
        if self.l_succesori is None:
            self.l_succesori = []
            for i in range(8):
                for j in range(3):
                    if self.se_scoate_o_piesa:
                        try:
                            self.l_succesori.append(self.eliminare_piesa(stare=self, lin=i, col=j))
                        except ValueError:
                            continue
                    else:
                        if (self.jucator_curent_alb and self.piese_albe_nefolosite > 0) or ((not self.jucator_curent_alb) and self.piese_negre_nefolosite > 0):
                            try:
                                self.l_succesori.append(self.adaugare_piesa(stare=self, lin=i, col=j))
                            except ValueError:
                                continue
                        else:
                            if self.matrix[i][j] == self.jucator_curent_alb:
                                if (self.jucator_curent_alb and self.piese_albe_pe_tabla > 3) or ((not self.jucator_curent_alb) and self.piese_negre_pe_tabla > 3):
                                    for lin, col in [(-1, 0), (0, 1), (1, 0), (0, -1)]:
                                        try:
                                            """
                                            obtinem linia si coloana reala a pozitiei curente. Apoi ne uitam sus, jos, stanga, dreapta, iar pt acele pozitii care exista
                                            obtinem linia si coloana din matrice la care se afla elementul adiacent. Daca este o pozitie libera, generam un nou succesor
                                            """
                                            linie_reala_actuala, coloana_reala_actuala = Stare.decodif_poz_matrice[i * 3 + j]
                                            idx_pozitie_noua = Stare.decodif_poz_matrice.index((linie_reala_actuala + lin * max(1, abs(coloana_reala_actuala - 4)), coloana_reala_actuala + col * max(1, abs(linie_reala_actuala - 4))))
                                            self.l_succesori.append(self.muta_piesa(stare=self, old_lin=i, old_col=j, new_lin=idx_pozitie_noua // 3, new_col=idx_pozitie_noua % 3))
                                        except ValueError:
                                            continue
                                else:
                                    """
                                    au ramas 3 piese pentru jucatorul curent, deci poate sa sara cum vrea
                                    """
                                    for lin in range(8):
                                        for col in range(3):
                                            if self.matrix[lin][col] is None:
                                                try:
                                                    self.l_succesori.append(self.muta_piesa(stare=self, old_lin=i, old_col=j, new_lin=lin, new_col=col))
                                                except ValueError:
                                                    continue
        return self.l_succesori

    @classmethod
    def muta_piesa(cls, stare, old_lin, old_col, new_lin, new_col):
        """
        Muta o piesa de pe pozitia veche pe o pozitie noua. Ridica exceptia ValueError pentru o mutare incorecta
        :param stare: starea pe care se face mutarea
        :param old_lin: linia pozitiei de pe care se muta
        :param old_col: coloana pozitiei de pe care se muta
        :param new_lin: noua linie la care se doreste sa se mute
        :param new_col: noua coloana la care se doreste sa se mute
        :return: Intoarce o noua stare in care s-a realizat mutarea, sau ridica o exceptie de tip ValueError daca s-a introdus o mutare invalida
        """
        if stare.matrix[new_lin][new_col] is None:
            new_matrix = copy.deepcopy(stare.matrix)
            new_matrix[old_lin][old_col] = None
            new_matrix[new_lin][new_col] = stare.jucator_curent_alb
            eliminare = Stare.este_in_moara(new_matrix, (new_lin, new_col))
            if eliminare:
                jucator_nou = stare.jucator_curent_alb
            else:
                jucator_nou = not stare.jucator_curent_alb
            stare_noua = Stare(tata=stare, matrix=new_matrix, piese_albe_nefolosite=stare.piese_albe_nefolosite, piese_albe_pe_tabla=stare.piese_albe_pe_tabla,
                               piese_negre_pe_tabla=stare.piese_negre_pe_tabla, piese_negre_nefolosite=stare.piese_negre_nefolosite, se_scoate_o_piesa=eliminare, jucator_curent_alb=jucator_nou)
            return stare_noua
        else:
            raise ValueError

    @classmethod
    def eliminare_piesa(cls, stare, lin, col):
        """
        Primind o stare, elimina o piesa aflata pe o pozitie data. Ridica exceptia ValueError pentru o mutare incorecta
        :param stare: starea pe care se face mutarea
        :param lin: linia la care se afla piesa de eliminar
        :param col: coloana la care se afla piesa de eliminat
        :return: Intoarce o noua stare in care s-a realizat stergerea, sau ridica o exceptie de tip ValueError daca s-a introdus o mutare invalida
        """
        if (not stare.jucator_curent_alb) == stare.matrix[lin][col] and (not Stare.este_in_moara(stare.matrix, (lin, col))):
            new_matrix = copy.deepcopy(stare.matrix)
            new_matrix[lin][col] = None
            negre_ramase = stare.piese_negre_pe_tabla
            albe_ramase = stare.piese_albe_pe_tabla
            if stare.jucator_curent_alb:
                negre_ramase -= 1
            else:
                albe_ramase -= 1
            stare_noua = Stare(tata=stare, matrix=new_matrix, piese_albe_nefolosite=stare.piese_albe_nefolosite, piese_albe_pe_tabla=albe_ramase,
                               piese_negre_pe_tabla=negre_ramase, piese_negre_nefolosite=stare.piese_negre_nefolosite, se_scoate_o_piesa=False, jucator_curent_alb=not stare.jucator_curent_alb)
            return stare_noua
        else:
            raise ValueError

    @classmethod
    def adaugare_piesa(cls, stare, lin, col):
        """
        Primind o stare, adauga o noua piesa pe tabla (in ordinea in care urmeaza in functie de stare.jucjucator_curent_alb).
        Ridica exceptia ValueError pentru o mutare incorecta
        :param stare: starea pe care se face mutarea
        :param lin: linia pe care se doreste sa se adauge piesa
        :param col: coloana linia pe care se doreste sa se adauge piesa
        :return: Intoarce o noua stare in care s-a realizat adaugarea, sau ridica o exceptie de tip ValueError daca s-a introdus o mutare invalida
        """
        if stare.matrix[lin][col] is None:
            negre_nefolosite = stare.piese_negre_nefolosite
            negre_ramase = stare.piese_negre_pe_tabla
            albe_nefolosite = stare.piese_albe_nefolosite
            albe_ramase = stare.piese_albe_pe_tabla
            if stare.jucator_curent_alb:
                albe_ramase += 1
                albe_nefolosite -= 1
            else:
                negre_ramase += 1
                negre_nefolosite -= 1
            new_matrix = copy.deepcopy(stare.matrix)
            new_matrix[lin][col] = stare.jucator_curent_alb
            eliminare = Stare.este_in_moara(new_matrix, (lin, col))
            if eliminare:
                jucator_nou = stare.jucator_curent_alb
            else:
                jucator_nou = not stare.jucator_curent_alb
            stare_noua = Stare(tata=stare, matrix=new_matrix, piese_albe_nefolosite=albe_nefolosite, piese_albe_pe_tabla=albe_ramase,
                               piese_negre_pe_tabla=negre_ramase, piese_negre_nefolosite=negre_nefolosite, se_scoate_o_piesa=eliminare, jucator_curent_alb=jucator_nou)
            return stare_noua
        else:
            raise ValueError

    def is_final_state(self):
        """
        Intrucat regulile jocului nu precizau ce se intampla pe anumite cazuri, am decis sa creez urmatoarele reguli care mi s-au parut sa aiba cel mai mult sens.
        Daca se ajunge intr-o stare fara succesori si niciunul dintre jucatori nu a ramas fara 3 piese, se declara REMIZA (indiferent cine a blocat pe cine)
        Se castiga doar daca adversarul ramane cu mai puin de 3 piese
        :return: True daca e stare finala, False altfel
        """
        if self.piese_albe_nefolosite == 0 and self.piese_negre_nefolosite == 0 and (self.piese_negre_pe_tabla < 3 or self.piese_albe_pe_tabla < 3):
            return True
        else:
            return len(self.generare_succesori()) == 0


class MorrisBoard(tkinter.Tk):
    """
    Aceasta clasa controleaza jocul si interfata grafica (mostenind clasa tkinter.TK)
    Are nevoie de clasa Stare pentru a functiona
    """
    buttons = []

    def __init__(self, algoritm=2, jucator_om=1, adancime_maxima=2, euristica=True):
        """
        initializarea jocului
        :param algoritm: Primeste algoritmul cu care se joaca. 0=om vs om, 1=minimax, 2=alpha-beta
        :param jucator_om:primeste culoarea cu care joaca omul. True=alb, False=negru
        :param adancime_maxima:adancimea arborelui folosit de algoritmii minimax si alphabeta
        :param euristica: True pentru o euristica mai buna, False pentru una mai neinspirata
        :param self.geometry indica dimensiunea ferestrei care se va deschide
        :param self.title va seta titlul ferestrei
        :param self.board_frame va tine frame-ul care se va deschide in fereastra(care va tine toate celelalte componente grafice)
        :param self.label va adauga imaginea de fundal a tablei de tintar
        :param self.stare_curenta va retine configuratia actuala a jocului la care s-a ajuns
        :param self.exit_button contine butonul de iesire din centrul tablei (care si anunta la final castigatotul jocului)
        :param self.buttons sste o lista ce retine toate butoanele pe care se poate deplasa o piesa
        :param self.poz_piesa_care_se_muta este folosit cand trebuie sa mutam o piesa, astfel incat in urma selectarii sa nu se treaca la alt jucator. Are valoarea None cand se poate trece
        :param self.jucator_ai contine culoarea cu care joaca calculatorul
        :param self.utilizator_ready retine daca omul si-a terminat toate mutarile si poate sa vina randul la ai
        :param self.t_ai este o lista folosita pentru statisticile legate de timpul algoritmului minimax/alpha-beta
        :param self.nr_noduri_ai este o lista folosita pentru statisticile legate de nr de noduri generate de algoritmului minimax/alpha-beta
        :param self.nr_noduri_ai_curent retine nr de noduri generate de algoritmului minimax/alpha-beta la pasul curent
        :param self.t0 retine timpul de inceput al jocului
        :param self.t retine timpul la care s-a efectuat ultima mutare (deci time.time()-self.t va returna durata unei mutari)
        :param self.co_apeluri_ai numara cate mutari a facut calculatorul
        :param self.co_apeluri_om numara cate mutari a facut omul
        :param self.finalizat retine daca s-a finaliat jocul. Ia valoarea True dupa ce se executa functia de finalizare, blocand tabla
        """
        super().__init__()
        self.geometry("440x552")
        self.title("Popescu Paullo Robertto Karloss - Tintar")
        self.board_frame = tkinter.Frame(self, width=440, height=512)
        bg = tkinter.PhotoImage(file="board13.png")
        self.label = tkinter.Label(self.board_frame, image=bg)
        self.label.place(x=0, y=0)
        self.stare_curenta = Stare(Stare.generare_matrice())
        self.exit_button = None
        self.add_buttons(5)
        self.poz_piesa_care_se_muta = None
        self.jucator_ai = not jucator_om
        self.algoritm = algoritm
        self.board_frame.grid(row=1, column=1)
        self.adancime_maxima = adancime_maxima
        self.utilizator_ready = False
        self.t_ai = []
        self.nr_noduri_ai = []
        self.nr_noduri_ai_curent = 0
        self.t = time.time()
        self.t0 = time.time()
        self.co_apeluri_ai = 0
        self.co_apeluri_om = 0
        self.finalizat = False
        self.euristica = euristica

        if jucator_om == 2:
            while not Stare.is_final_state(self.stare_curenta):
                """
                joc ai vs ai
                """
                self.ai_play()
                self.euristica = not self.euristica
                self.jucator_ai = not self.jucator_ai
        else:
            """
            daca omul e negru, AI muta primul
            """
            if self.algoritm > 0 and self.jucator_ai:
                self.ai_play()

        """incepe ascultarea inputului pana la finalizarea jocului"""
        self.mainloop()

        self.finalizare(forced_quit=True)

    def play_next_move(self, poz):
        """
        In aceasta metoda se executa mutarile unui utilizator uman. Este chemata de apasarea unui buton pe tabla
        :param poz: primeste pozitia butonului
        :return: None
        """
        if not self.finalizat:
            utilizator = "alb" if self.stare_curenta.jucator_curent_alb else "negru"
            print("Este randul jucatorului " + utilizator)
            self.utilizator_ready = False
            try:
                idx = Stare.decodif_poz_matrice.index(poz)
                lin, col = (idx // 3, idx % 3)
                if self.stare_curenta.se_scoate_o_piesa:
                    """
                    se scoate o piesa a adversarului care nu este in moara
                    """
                    self.eliminare_piesa(idx=idx, lin=lin, col=col)
                else:
                    if (self.stare_curenta.jucator_curent_alb and self.stare_curenta.piese_albe_nefolosite > 0) or ((not self.stare_curenta.jucator_curent_alb) and self.stare_curenta.piese_negre_nefolosite > 0):
                        """
                        se adauga o noua piese pe o pozitie goala
                        """
                        self.adaugare_piesa(idx=idx, lin=lin, col=col)
                    else:
                        """
                        se muta o piesa
                        """
                        self.mutare_piesa(poz=poz, idx=idx, lin=lin, col=col)
                print("Timp de gandire {}: {}s".format(utilizator, time.time() - self.t))
                self.stare_curenta.print_matrix()
                self.t = time.time()
                self.co_apeluri_om += 1
                if self.algoritm > 0 and self.utilizator_ready and (not self.stare_curenta.se_scoate_o_piesa) and self.algoritm > 0:
                    self.ai_play()
            except ValueError:
                return
            self.finalizare()

    def ai_play(self):
        """
        Executa mutarea calculatorului. Daca dupa prima mutare se ajunge intr-o stare in care trebuie sa se scoata o piesa a adversarului
        se mai genereaza inca un arbore al algoritmului minimax/alpha-beta
        :return: None
        """
        if not self.finalizat:
            utilizator = "alb" if self.jucator_ai else "negru"
            print("Este randului AI: jucator " + utilizator)
            self.nr_noduri_ai_curent = 0
            if self.algoritm == 1:
                self.stare_curenta = self.mini_max(stare=self.stare_curenta, jucator_curent=self.jucator_ai, adancime_ramasa=self.adancime_maxima)
                if self.stare_curenta.se_scoate_o_piesa:
                    self.stare_curenta = self.mini_max(stare=self.stare_curenta, jucator_curent=self.jucator_ai, adancime_ramasa=self.adancime_maxima)
                    self.co_apeluri_ai += 1
            else:
                self.stare_curenta = self.alpha_beta(stare=self.stare_curenta, alpha=float('-inf'), beta=float('inf'), jucator_curent=self.jucator_ai, adancime_ramasa=self.adancime_maxima)
                if self.stare_curenta.se_scoate_o_piesa:
                    self.stare_curenta = self.alpha_beta(stare=self.stare_curenta, alpha=float('-inf'), beta=float('inf'), jucator_curent=self.jucator_ai, adancime_ramasa=self.adancime_maxima)
                    self.co_apeluri_ai += 1

            for i in range(len(self.stare_curenta.matrix)):
                for j in range(len(self.stare_curenta.matrix[i])):
                    if self.stare_curenta.matrix[i][j] is True:
                        self.buttons[i * 3 + j].configure(bg='white')
                    elif self.stare_curenta.matrix[i][j] is False:
                        self.buttons[i * 3 + j].configure(bg='black')
                    else:
                        self.buttons[i * 3 + j].configure(bg='grey')

            timp_gandire = time.time() - self.t
            self.t_ai.append(timp_gandire)
            self.t = time.time()
            self.co_apeluri_ai += 1
            self.nr_noduri_ai.append(self.nr_noduri_ai_curent)
            print("Timp de gandire AI-{}: {}s".format(utilizator, timp_gandire))
            euristica = "estimeaza_scor_by_pioni" if self.euristica else "estimeaza_scor_by_moara"
            print("Estimare Scor AI: {}, folosind Euristica={}".format(self.stare_curenta.estimare, euristica))
            print("Nr noduri generate de AI: {}".format(self.nr_noduri_ai_curent))
            self.stare_curenta.print_matrix()
            self.finalizare()

    def add_buttons(self, dim=5):
        """
        :param dim: dimensiunea unui buton. Clasa e configurata sa arate bine cu butoane de dimensiune 5
        :return: None
        :param self.buttons va retine butoanele adaugate
        """
        for lin, col in Stare.decodif_poz_matrice:
            button = tkinter.Button(self.board_frame, height=dim, width=dim, command=functools.partial(self.play_next_move, (lin, col)), bg="grey", activebackground='green')
            button.grid(row=lin, column=col)
            self.buttons.append(button)
        self.exit_button = tkinter.Button(self.board_frame, height=3, width=3, command=self._root().destroy, activebackground='red')
        self.exit_button.grid(row=4, column=4)

    def eliminare_piesa(self, idx, lin, col):
        """
        Se elimina o piesa aflata la o pozitie data si se actualizeaza butonul si starea curenta daca a fost eliminata cu scucces
        Are nevoie de metoda eliminare_piesa din clasa Stare
        :param idx: poz butonului care trebuie actualizat
        :param lin: linia de stergere
        :param col: coloana de stergere
        :return: None daca s-a eliminat cu bine, actualizand self.stare curenta si butonul, sau ridica exceptia ValueError pt o mutare incorecta
        """
        if self.stare_curenta.matrix[lin][col] == (not self.stare_curenta.jucator_curent_alb) and (not Stare.este_in_moara(self.stare_curenta.matrix, (lin, col))):
            stare = Stare.eliminare_piesa(stare=self.stare_curenta, lin=lin, col=col)
            if stare in self.stare_curenta.generare_succesori():
                self.stare_curenta = stare
                self.buttons[idx].configure(bg='grey')
                self.utilizator_ready = True
            else:
                raise ValueError
        else:
            raise ValueError

    def adaugare_piesa(self, idx, lin, col):
        """
        Se adauga o piesa aflata la o pozitie data si se actualizeaza butonul si starea curenta daca a fost eliminata cu scucces
        Are nevoie de metoda adaugare_piesa din clasa Stare
        :param idx: poz butonului care trebuie actualizat
        :param lin: linia de adaugare
        :param col: coloana de adaugare
        :return: None daca s-a eliminat cu bine, actualizand self.stare curenta si butonul, sau ridica exceptia ValueError pt o mutare incorecta
        """
        if self.stare_curenta.matrix[lin][col] is None:
            stare = Stare.adaugare_piesa(stare=self.stare_curenta, lin=lin, col=col)
            if stare in self.stare_curenta.generare_succesori():
                if self.stare_curenta.jucator_curent_alb:
                    self.buttons[idx].configure(bg='white')
                else:
                    self.buttons[idx].configure(bg='black')
                self.stare_curenta = stare
                self.utilizator_ready = True
            else:
                raise ValueError
        else:
            raise ValueError

    def mutare_piesa(self, poz, idx, lin, col):
        """
        Se muta o piesa aflata la o pozitie data dintr-o alta pozitie data si se actualizeaza butonul si starea curenta daca a fost eliminata cu scucces
        Are nevoie de metoda mutare_piesa din clasa Stare
        :param poz: pozitia de la care se muta
        :param idx: indexul butonului de actualizat (in lista self,buttons)
        :param lin: linia noua
        :param col: coloana noua
        :return: None daca s-a eliminat cu bine, actualizand self.stare curenta si butonul, sau ridica exceptia ValueError pt o mutare incorecta
        """
        if self.poz_piesa_care_se_muta is None:
            if self.stare_curenta.matrix[lin][col] == self.stare_curenta.jucator_curent_alb:
                self.poz_piesa_care_se_muta = poz
                self.buttons[idx].configure(bg='cyan')
                raise ValueError
        else:
            old_idx = Stare.decodif_poz_matrice.index(self.poz_piesa_care_se_muta)
            old_lin, old_col = (old_idx // 3, old_idx % 3)
            if self.stare_curenta.jucator_curent_alb:
                self.buttons[old_idx].configure(bg='white')
            else:
                MorrisBoard.buttons[old_idx].configure(bg='black')
            if self.stare_curenta.matrix[lin][col] is None:
                stare = Stare.muta_piesa(stare=self.stare_curenta, old_lin=old_lin, old_col=old_col, new_lin=lin, new_col=col)
                if stare in self.stare_curenta.generare_succesori():
                    self.buttons[old_idx].configure(bg='grey')
                    if self.stare_curenta.jucator_curent_alb:
                        self.buttons[idx].configure(bg='white')
                    else:
                        self.buttons[idx].configure(bg='black')
                    self.stare_curenta = stare
                    self.utilizator_ready = True
                else:
                    self.poz_piesa_care_se_muta = None
                    raise ValueError
            else:
                self.poz_piesa_care_se_muta = None
                raise ValueError
            self.poz_piesa_care_se_muta = None

    def estimeaza_scor(self, stare):
        """
        functiile de estimare a scorului, in functie de euristica aleasa
        :param stare: starea pt care se face estimarea
        :return: valoarea estimarii
        """

        def estimeaza_scor_by_pioni(stare):
            """
            Influentat de cate piese are alb vs negru si daca o piesa e blocata sau nu
            :param stare: starea pt care se face estimarea
            :return: valoarea estimarii
            """
            co = 0
            for i in range(len(stare.matrix)):
                for j in range(len(stare.matrix[i])):
                    if stare.matrix[i][j] is True:
                        co += 1
                        if Stare.se_poate_deplasa(stare.matrix, (i, j)):
                            co += 0.5
                    elif stare.matrix[i][j] is False:
                        co -= 1
                        if not Stare.se_poate_deplasa(stare.matrix, (i, j)):
                            co += 0.5
            return co

        def estimeaza_scor_by_moara(stare):
            """
            Influentat de cate piese are alb vs negru si daca avem mori sau nu
            :param stare: starea pt care se face estimarea
            :return: valoarea estimarii
            """
            co = 0
            for i in range(len(stare.matrix)):
                for j in range(len(stare.matrix[i])):
                    if stare.matrix[i][j] is True:
                        co += 1
                        if Stare.este_in_moara(stare.matrix, (i, j)):
                            co += 1
                        elif Stare.aproape_moara(stare.matrix, (i, j)):
                            co += 0.5
                    elif stare.matrix[i][j] is False:
                        co -= 1
                        if Stare.este_in_moara(stare.matrix, (i, j)):
                            co -= 1
                        elif Stare.aproape_moara(stare.matrix, (i, j)):
                            co -= 0.5
            return co

        if self.euristica:
            return estimeaza_scor_by_pioni(stare)
        else:
            return estimeaza_scor_by_moara(stare)

    def mini_max(self, stare, adancime_ramasa, jucator_curent):
        """
        algoritmul minimax
        :param stare: starea la care s-a ajuns
        :param adancime_ramasa: adancimea ramasa(scade cu 1 la fiecare apel recursiv)
        :param jucator_curent: daca se joaca ca jucator maxim(==jucator ai) sau minim (==not jucator ai)
        :return:
        """
        if stare.is_final_state() or adancime_ramasa == 0:
            stare.estimare = self.estimeaza_scor(stare)
            return stare
        else:
            scoruri = [self.mini_max(x, adancime_ramasa - 1, not jucator_curent) for x in stare.generare_succesori()]
            self.nr_noduri_ai_curent += len(scoruri)
            if jucator_curent == self.jucator_ai:
                stare_aleasa = max(scoruri, key=lambda stare_x: stare_x.estimare)
            else:
                stare_aleasa = min(scoruri, key=lambda stare_x: stare_x.estimare)
            stare.estimare = stare_aleasa.estimare
            if adancime_ramasa < self.adancime_maxima:
                return stare
            else:
                return stare_aleasa

    def alpha_beta(self, stare, alpha, beta, adancime_ramasa, jucator_curent):
        """
        Algoritmul alpha-beta. Listele succesorilor se sorteaza in prealabil pt a asigura o taiere optima (alpha>=beta)
        :param stare: starea la care s-a ajuns
        :param alpha: alpha
        :param beta: beta
        :param adancime_ramasa: adancimea ramasa(scade cu 1 la fiecare apel recursiv)
        :param jucator_curent:  daca se joaca ca jucator maxim(==jucator ai) sau minim (==not jucator ai)
        :return:
        """
        if stare.is_final_state() or adancime_ramasa == 0:
            stare.estimare = self.estimeaza_scor(stare)
            return stare

        elif alpha > beta:
            return stare

        else:
            stare_aleasa = stare
            if jucator_curent == self.jucator_ai:
                estimare_curenta = float('-inf')
                for stare_noua in sorted(stare.generare_succesori(), key=lambda stare_x: self.estimeaza_scor(stare_x), reverse=True):
                    stare_noua_cu_aproximare = self.alpha_beta(stare_noua, alpha, beta, adancime_ramasa - 1, not jucator_curent)
                    self.nr_noduri_ai_curent += 1
                    if estimare_curenta < stare_noua_cu_aproximare.estimare:
                        stare_aleasa = stare_noua_cu_aproximare
                        estimare_curenta = stare_noua_cu_aproximare.estimare
                    if alpha < stare_noua_cu_aproximare.estimare:
                        alpha = stare_noua_cu_aproximare.estimare
                        if alpha >= beta:
                            break
            else:
                estimare_curenta = float('inf')
                for stare_noua in sorted(stare.generare_succesori(), key=lambda stare_x: self.estimeaza_scor(stare_x)):
                    stare_noua_cu_aproximare = self.alpha_beta(stare_noua, alpha, beta, adancime_ramasa - 1, not jucator_curent)
                    self.nr_noduri_ai_curent += 1
                    if estimare_curenta > stare_noua_cu_aproximare.estimare:
                        stare_aleasa = stare_noua_cu_aproximare
                        estimare_curenta = stare_noua_cu_aproximare.estimare
                    if beta > stare_noua_cu_aproximare.estimare:
                        beta = stare_noua_cu_aproximare.estimare
                        if alpha >= beta:
                            break
            stare.estimare = stare_aleasa.estimare
            if adancime_ramasa < self.adancime_maxima:
                return stare
            else:
                return stare_aleasa

    def finalizare(self, forced_quit=False):
        """
        afiseaza statisticile de final si blocheaza tabla prin self.finalizat
        :param forced_quit: daca jocul s-a incheiat folosind butonul, inainte de finalizare este True; altfel False
        :return: None
        """
        if (not self.finalizat) and (self.stare_curenta.is_final_state() or forced_quit):
            self.finalizat = True
            if self.algoritm > 0 and self.co_apeluri_ai > 0:
                print("Statistici Timp AI: min={}, max={}, avg={}, mediana={}".format(min(self.t_ai), max(self.t_ai), round(sum(self.t_ai) / self.co_apeluri_ai, 5), statistics.median(self.t_ai)))
                print("Statistici Nr noduri create de AI: min={}, max={}, avg={}, mediana={}".format(min(self.nr_noduri_ai), max(self.nr_noduri_ai), round(sum(self.nr_noduri_ai) / self.co_apeluri_ai, 5), statistics.median(self.nr_noduri_ai)))
            print("Timp total de joc: {}s".format(time.time() - self.t0))
            print("AI apelat de {} ori".format(self.co_apeluri_ai))
            print("Om-ul a avut {} mutari".format(self.co_apeluri_om))
            if not forced_quit:
                if self.stare_curenta.piese_albe_pe_tabla >= 3 > self.stare_curenta.piese_negre_pe_tabla:
                    print("A castigat jucatorul ALB")
                    self.exit_button.configure(bg='white')
                elif self.stare_curenta.piese_negre_pe_tabla >= 3 > self.stare_curenta.piese_albe_pe_tabla:
                    print("A castigat jucatorul NEGRU")
                    self.exit_button.configure(bg='black')
                else:
                    print("REMIZA")
                    self.exit_button.configure(bg='purple')


if __name__ == "__main__":
    """
    Input de la utilizator
    """

    tip_algoritm = 2
    raspuns_valid = False
    while not raspuns_valid:
        tip_algoritm = input("Algorimul folosit? (raspundeti cu 0, 1 sau 2)\n 0.Om vs Om\n 1.Minimax\n 2.Alpha-beta\n ")
        if tip_algoritm in ['0', '1', '2']:
            raspuns_valid = True
        else:
            print("Nu ati ales o varianta corecta...")

    jucator = '1'
    if tip_algoritm != '0':
        raspuns_valid = False
        while not raspuns_valid:
            jucator = input("Jucator? (raspundeti cu 0, 1 sau 2)\n 0.negru\n 1.alb\n 2.AI vs AI\n ")
            if jucator in ['0', '1', '2']:
                raspuns_valid = True
            else:
                print("Nu ati ales o varianta corecta...")

    nivel = '0'
    if tip_algoritm != '0':
        raspuns_valid = False
        while not raspuns_valid:
            nivel = input("Nivel dificultate? (raspundeti cu 0, 1 sau 2)\n 1.Usor\n 2.Mediu\n 3.Dificil\n 4.Imbatabil:)\n ")
            if nivel in ['1', '2', '3', '4']:
                raspuns_valid = True
            else:
                print("Nu ati ales o varianta corecta...")

    euristica = '1'
    if tip_algoritm != '0' and jucator != '2':
        raspuns_valid = False
        while not raspuns_valid:
            euristica = input("Euristica? (raspundeti cu 0 sau 1)\n 0.Usor(dupa moara)\n 1.Greu(dupa pioni)\n ")
            if euristica in ['0', '1']:
                raspuns_valid = True
            else:
                print("Nu ati ales o varianta corecta...")

    MorrisBoard(algoritm=int(tip_algoritm), jucator_om=int(jucator), adancime_maxima=int(nivel), euristica=bool(int(euristica)))
