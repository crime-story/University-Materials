def getcofactor(m, i, j):
    return [row[: j] + row[j + 1:] for row in (m[: i] + m[i + 1:])]


def determinantOfMatrix(mat):
    if (len(mat) == 2):
        value = mat[0][0] * mat[1][1] - mat[1][0] * mat[0][1]
        return value

    Sum = 0

    for current_column in range(len(mat)):
        sign = (-1) ** (current_column)

        sub_det = determinantOfMatrix(getcofactor(mat, 0, current_column))

        Sum += (sign * mat[0][current_column] * sub_det)

    return Sum


if __name__ == '__main__':
    xa, ya = input().split(" ")
    xb, yb = input().split(" ")
    xc, yc = input().split(" ")

    xa = int(xa)
    ya = int(ya)
    xb = int(xb)
    yb = int(yb)
    xc = int(xc)
    yc = int(yc)

    n = int(input())
    for i in range(n):
        xd, yd = input().split(" ")
        xd = int(xd)
        yd = int(yd)
        mat = [[xa, ya, xa ** 2 + ya ** 2, 1],
               [xb, yb, xb ** 2 + yb ** 2, 1],
               [xc, yc, xc ** 2 + yc ** 2, 1],
               [xd, yd, xd ** 2 + yd ** 2, 1]]

        result = determinantOfMatrix(mat)
        if result > 0:
            print("INSIDE")
        elif result < 0:
            print("OUTSIDE")
        else:
            print("BOUNDARY")