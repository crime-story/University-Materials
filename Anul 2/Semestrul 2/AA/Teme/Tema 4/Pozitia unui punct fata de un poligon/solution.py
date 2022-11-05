def calculateDeterminant(x1, y1, x2, y2, x3, y3):
    return x2 * y3 + x1 * y2 + x3 * y1 - x2 * y1 - x3 * y2 - x1 * y3


def number_of_intersections(a, b, point1, point2):
    det1 = calculateDeterminant(point1[0], point1[1], point2[0], point2[1], a, b)
    det2 = calculateDeterminant(point1[0], point1[1], point2[0], point2[1], Mx, My)
    det3 = calculateDeterminant(Mx, My, a, b, point1[0], point1[1])
    det4 = calculateDeterminant(Mx, My, a, b, point2[0], point2[1])
    if ((det1 >= 0 >= det2) or (det1 <= 0 <= det2)) and ((det3 >= 0 >= det4) or (det3 <= 0 <= det4)):
        return 1
    return 0


def is_on_border(a, b, point1, point2):
    det = calculateDeterminant(point1[0], point1[1], point2[0], point2[1], a, b)
    if det == 0 and (point1[0] <= a <= point2[0] or point1[0] >= a >= point2[0]) and (
            point1[1] <= b <= point2[1] or point1[1] >= b >= point2[1]):
        return 1
    return 0


if __name__ == '__main__':
    n = int(input())
    point = []
    for i in range(n):
        x, y = input().split(" ")
        x = int(x)
        y = int(y)
        point.append((x, y))

    m = int(input())

    for i in range(m):
        a, b = input().split(" ")
        a = int(a)
        b = int(b)
        count = 0
        border = 0
        Mx = 9999999999
        My = b + 5
        for j in range(n - 1):
            count += number_of_intersections(a, b, point[j], point[j + 1])
            border += is_on_border(a, b, point[j], point[j + 1])
        count += number_of_intersections(a, b, point[n - 1], point[0])
        border += is_on_border(a, b, point[n - 1], point[0])
        if border:
            print("BOUNDARY")
        elif count % 2 == 0:
            print("OUTSIDE")
        else:
            print("INSIDE")