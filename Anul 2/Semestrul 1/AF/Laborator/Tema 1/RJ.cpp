// Link: https://www.infoarena.ro/job_detail/2799191?action=view-source

#include <fstream>
#include <queue>

#define NMAX 102

using namespace std;

ifstream f("rj.in");
ofstream g("rj.out");

int n, m, xr, yr, xj, yj;
int dl[8] = {0, 1, 0, -1, -1, 1, -1, 1};
int dc[8] = {1, 0, -1, 0, -1, 1, 1, -1};
queue<pair<int, int>> coada;
char l[NMAX][NMAX];
int r[NMAX][NMAX];
int j[NMAX][NMAX];

void citire() {
//    char c;
    char c[NMAX];
    f >> n >> m;
    for (int i = 0; i <= n + 1; i++)
        l[i][0] = l[i][m + 1] = 'X';
    for (int i = 0; i <= m + 1; i++)
        l[0][i] = l[n + 1][i] = 'X';

    f.get();
    for (int i = 1; i <= n; i++) {
        f.get(c, NMAX);
        for (int k = 1; k <= m; k++) {
            l[i][k] = c[k - 1];
            if (l[i][k] == 'R') {
                xr = i;
                yr = k;
                l[i][k] = ' ';
            }
            if (l[i][k] == 'J') {
                xj = i;
                yj = k;
                l[i][k] = ' ';
            }
        }
        f.get();
    }
}

void lee(int liniePlecare, int coloanaPlecare, int matrice[102][102]) {
    for (int i = 0; i <= n + 1; i++)
        for (int k = 0; k <= m + 1; k++)
            matrice[i][k] = 0;
    matrice[liniePlecare][coloanaPlecare] = 1;
    coada.push(make_pair(liniePlecare, coloanaPlecare));
    matrice[liniePlecare][coloanaPlecare] = 1;
    while (!coada.empty()) {
        int x = coada.front().first; // linia
        int y = coada.front().second; // coloana
        coada.pop();
        for (int i = 0; i < 8; i++)
            if (l[x + dl[i]][y + dc[i]] == ' ' && matrice[x + dl[i]][y + dc[i]] == 0) {
                matrice[x + dl[i]][y + dc[i]] = 1 + matrice[x][y];
                coada.push(make_pair(x + dl[i], y + dc[i]));
            }
    }
}

void afisare() {
    int tmin = 102 * 102;
    int xmin = -1;
    int ymin = -1;

    for (int i = 1; i <= n; i++)
        for (int k = 1; k <= m; k++)
            if (r[i][k] == j[i][k])
                if (r[i][k] < tmin && r[i][k] != 0) {
                    tmin = r[i][k];
                    xmin = i;
                    ymin = k;
                }
    g << tmin << " " << xmin << " " << ymin << endl;
}

int main() {
    citire();
    lee(xr, yr, r); // pt Romeo
    lee(xj, yj, j); // pt Julieta
    afisare();
    return 0;
}