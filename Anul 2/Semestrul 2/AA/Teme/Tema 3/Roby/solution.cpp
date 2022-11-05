#include <iostream>

using namespace std;

long long calculateDeterminant(long long &xp, long long &yp, long long &xq, long long &yq, long long &xr, long long &yr) {
    long long result = xq * yr + xp * yq + yp * xr - xq * yp - xr * yq - yr * xp;
    return result;
}

int main() {
    long long numberOfPoints;
    long long countLeft = 0, countRight = 0, countTouch = 0;
    long long point0[2], point1[2], point2[2], point3[2];
    cin >> numberOfPoints;
    for (long long i = 0; i < numberOfPoints; i++) {
        long long x, y;
        cin >> x >> y;
        if (i == 0) {
            point0[0] = x;
            point0[1] = y;
            point1[0] = x;
            point1[1] = y;
        }
        else if (i == 1) {
            point2[0] = x;
            point2[1] = y;
        }
        else if (i == 2) {
            point3[0] = x;
            point3[1] = y;
            long long determinantResult = calculateDeterminant(point1[0], point1[1], point2[0], point2[1], point3[0], point3[1]);
            if (determinantResult > 0)
                countLeft++;
            else if (determinantResult < 0)
                countRight++;
            else
                countTouch++;
        }
        else {
            point1[0] = point2[0];
            point1[1] = point2[1];
            point2[0] = point3[0];
            point2[1] = point3[1];
            point3[0] = x;
            point3[1] = y;

            long long determinantResult = calculateDeterminant(point1[0], point1[1], point2[0], point2[1], point3[0], point3[1]);

            if (determinantResult > 0)
                countLeft++;
            else if (determinantResult < 0)
                countRight++;
            else
                countTouch++;
        }
        if (i == numberOfPoints - 1) {
            long long determinantResult = calculateDeterminant(point2[0], point2[1], point3[0], point3[1], point0[0], point0[1]);
            if (determinantResult > 0)
                countLeft++;
            else if (determinantResult < 0)
                countRight++;
            else
                countTouch++;
        }
    }
    cout << countLeft << " " << countRight << " " << countTouch;
    return 0;
}