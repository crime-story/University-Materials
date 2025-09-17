# Ex 1 a)
def ex1_a():
    gMax = int(input("Introduceti gMax: "))  # gMax este K
    DP = [0] * (gMax + 1)  # suma maxima <= cu i pe care o pot obtine cu obiectele de la 0 pana la interatia curenta
    obiecte = [int(x) for x in input().split()]  # greutatile si valorile obiectelor

    for i in range(len(obiecte)):  # consideram obiectele de la 0 la i
        for j in range(gMax, 0,
                       -1):  # parcurg descresctor greutatile pentru a putea actualiza dinamica pe un singur vector
            if j - obiecte[i] >= 0:
                DP[j] = max(DP[j], obiecte[i] + DP[j - obiecte[i]])

    print(DP[gMax])


# ex1_a()

# Ex 2 b)
def ex1_b():
    gMax = int(input("Introduceti gMax: "))  # gMax este K
    s = 0  # suma aproximativa
    nr = 0  # numarul curent
    while True:
        nr = int(input("Valoare/greutate obiect: "))
        if nr < 0:  # asa semnalam ca am terminat de citit
            break
        if nr <= gMax:  # daca numarul citit este valid
            if s + nr <= gMax:  # daca suma curenta + numarul curent nu depaseste gMax
                s += nr  # adaugam la suma
            elif nr > s:  # altfel luam numarul mai mare si apoi se va da break
                s = nr  # pentru ca s + nr > gMax si nr > s atunci nr >= gMax / 2
        if s >= gMax / 2:  # da break cand s depaseste gMax / 2 pentru ca OPT <= gMax => s >= OPT / 2 => ALG 1/2 aproximativ
            break

    print(s)


ex1_b()