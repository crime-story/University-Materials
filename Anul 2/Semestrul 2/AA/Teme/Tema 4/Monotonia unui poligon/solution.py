n = int(input())

minim = float('inf')
maxim = float('-inf')

lista = []

for i in range(n):
    xa, ya = input().split(" ")
    xa = int(xa)
    ya = int(ya)
    lista.append([xa, ya])

aux1 = []
aux2 = []

pos_minim = 0
pos_maxim = 0

for i in range(len(lista)):
    if lista[i][0] < minim:
        minim = lista[i][0]
        pos_minim = i

    if lista[i][1] > maxim:
        maxim = lista[i][1]
        pos_maxim = i

aux1 = lista[pos_minim:] + lista[:pos_minim]
aux2 = lista[pos_maxim:] + lista[:pos_maxim]

count_aux1 = 0
count_aux2 = 0

for i in range(len(aux1) - 2):
    if aux1[i][0] < aux1[i + 1][0] and aux1[i + 1][0] > aux1[i + 2][0]:
        count_aux1 += 1

if count_aux1 == 1:
    print("YES")
else:
    print("NO")

for i in range(len(aux2) - 2):
    if aux2[i][1] > aux2[i + 1][1] and aux2[i + 1][1] < aux2[i + 2][1]:
        count_aux2 += 1

if count_aux2 == 1:
    print("YES")
else:
    print("NO")