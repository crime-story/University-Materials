def determinant(point1, point2, point3):
    return point2[0] * point3[1] + point1[0] * point2[1] + point3[0] * point1[1] - point2[0] * point1[1] - point2[1] * point3[0] - point3[1] * point1[0]

def rez(l=None):
    if l is None:
        n = int(input())
        l = []
        for i in range(n):
            l.append(tuple([int(x) for x in input().split()]))

    vfStanga = l.index(min(l))
    l = l[vfStanga:] + l[:vfStanga]
    vfDreapta = l.index(max(l))

    # calculam frontiera de jos
    s = [l[0]]
    for i in range(1, vfDreapta + 1):
        while len(s) > 1 and determinant(s[-2], s[-1], l[i]) <= 0:
            s.pop()
        s.append(l[i])

    # calculam frontiera de sus
    s2 = [l[vfDreapta]]  # incepem de la vf din extrema dreapta
    l.append(l[0])  # adaugam vf stang la capatul listei ca sa se inchida poligonul
    for i in range(vfDreapta + 1, len(l)):
        while len(s2) > 1 and determinant(s2[-2], s2[-1], l[i]) <= 0:
            s2.pop()
        s2.append(l[i])
    s.extend(s2[1:-1])  # scoatem primul (extrema dreapta) si ultimul vf (extrema stanga) din s2 pt ca se repeta
    return s

if __name__ == '__main__':
    l = rez()
    print(len(l))
    for x, y in l:
        print(x, y, sep=' ')