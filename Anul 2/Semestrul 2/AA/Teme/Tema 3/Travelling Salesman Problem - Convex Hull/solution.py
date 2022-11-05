def determinant(point1, point2, point3):
    return point2[0] * point3[1] + point1[0] * point2[1] + point3[0] * point1[1] - point2[0] * point1[1] - point2[1] * point3[0] - point3[1] * point1[0]

def tsp():
    def grahamScan(l):
        l.sort()
        # calculam frontiera de jos
        s = [l[0]]
        for i in range(1,len(l)):
            while len(s) > 1 and determinant(s[-2], s[-1], l[i]) <= 0:
                s.pop()
            s.append(l[i])

        # calculam frontiera de sus
        s2 = [l[len(l)-1]]  # incepem de la vf din extrema dreapta
        for i in range(len(l)-2,-1,-1):
            while len(s2) > 1 and determinant(s2[-2], s2[-1], l[i]) <= 0:
                s2.pop()
            s2.append(l[i])
        s.extend(s2[1:-1])  # scoatem primul (extrema dreapta) si ultimul vf (extrema stanga) din s2 pt ca se repeta
        return s


    def distEuclidiana(a, b):  # distanta euclidiana in planul 2d
        from math import sqrt
        a = (round(a[0], 7), round(a[1], 7))
        b = (round(b[0], 7), round(b[1], 7))
        ox = round(a[0] - b[0], 7)
        oy = round(a[1] - b[1], 7)
        d = sqrt(round(ox ** 2, 7) + round(oy ** 2, 7))
        return round(d, 7)

    n = int(input())
    l = []
    for i in range(n):
        l.append(tuple([int(x) for x in input().split()]))
    tsp = grahamScan(l)# initializam mai intai cu punctele de pe infasuratoarea convexa; trb sortate mai intai pt ca la ex2 se dadeau in ordinea parcurgerii poligonului
    pctRamase = list(set(l) - set(tsp))

    while len(pctRamase) > 0:
        rapMin = float('inf')
        pozRapMin = None
        pctAles = None
        for pct in pctRamase:  # cautam cea mai buna distanta fata de un pct aflat deja in tsp; luam de fiecare data toate punctele ramase
            dMin = float('inf')
            pozMin = None
            for i in range(len(tsp)):  # selectam pct cu cel mai mic impact asupra distantei
                d1 = distEuclidiana(pct, tsp[i])
                d2 = distEuclidiana(pct, tsp[(i + 1) % len(tsp)])
                d3 = distEuclidiana(tsp[i], tsp[(i + 1) % len(tsp)])
                d = round(round(d1 + d2, 7) - d3, 7)

                if d < dMin:
                    dMin = d
                    pozMin = i
            # verificam raportul minim
            d1 = distEuclidiana(tsp[pozMin], pct)
            d2 = distEuclidiana(pct, tsp[(pozMin + 1) % len(tsp)])
            d3 = distEuclidiana(tsp[pozMin], tsp[(pozMin + 1) % len(tsp)])
            d = round(round(d1 + d2, 7) / d3, 7)
            if d < rapMin:
                rapMin = d
                pozRapMin = pozMin  # stabilim unde adaug punctul ales
                pctAles = pct  # stabilim care pct il adaugam
        try:
            pctRamase.remove(pctAles)
            tsp.insert(pozRapMin + 1, pctAles)  # adaug punctul in tsp
        except Exception:
            break
    tsp.append(tsp[0])  # inchidem drumul din tsp
    return tsp


if __name__ == '__main__':
    l = tsp()
    for x,y in l:
        print(x,y,sep=' ')