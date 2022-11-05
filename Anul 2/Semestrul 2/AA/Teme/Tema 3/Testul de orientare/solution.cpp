#include <iostream>
#include <vector>

using namespace std;

long long calculateDeterminant(long long &xp, long long &yp, long long &xq, long long &yq, long long &xr, long long &yr) {
    long long result = xq * yr + xp * yq + yp * xr - xq * yp - xr * yq - yr * xp;
    return result;
}

int main() {
    long long numberOfTests;
    cin >> numberOfTests;
    vector<string> answears;
    answears.resize(numberOfTests);
    for (long long i = 0; i < numberOfTests; i++) {
        long long xp, yp, xq, yq, xr, yr;
        cin >> xp >> yp >> xq >> yq >> xr >> yr;
        long long determinantResult = calculateDeterminant(xp, yp, xq, yq, xr, yr);
        if (determinantResult > 0)
            answears[i] = "LEFT";
        else if (determinantResult < 0)
            answears[i] = "RIGHT";
        else
            answears[i] = "TOUCH";
    }
    for (long long i = 0; i < answears.size(); i++)
        cout << answears[i] << "\n";
    return 0;
}