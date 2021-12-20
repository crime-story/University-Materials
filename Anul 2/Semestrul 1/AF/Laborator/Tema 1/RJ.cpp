#include <fstream>
#include <queue>

#define NMAX 102

using namespace std;

ifstream fin("rj.in");
ofstream fout("rj.out");

int nrLinii, nrColoane, linieRomeo, coloanaRomeo, linieJulieta, coloanaJulieta;
int dl[8] = {0, 1, 0, -1, -1, 1, -1, 1};
int dc[8] = {1, 0, -1, 0, -1, 1, 1, -1};
queue<pair<int, int>> coada;
char a[NMAX][NMAX];
int matriceRomeo[NMAX][NMAX];
int matriceJulieta[NMAX][NMAX];

void citire() {
//    char c;
    char c[NMAX];
    fin >> nrLinii >> nrColoane;
    for (int i = 0; i <= nrLinii + 1; i++)
        a[i][0] = a[i][nrColoane + 1] = 'X';
    for (int i = 0; i <= nrColoane + 1; i++)
        a[0][i] = a[nrLinii + 1][i] = 'X';
    fin.get();
    for (int i = 1; i <= nrLinii; i++) {
        fin.get(c, NMAX);
        for (int j = 1; j <= nrColoane; j++) {
            a[i][j] = c[j - 1];
            if (a[i][j] == 'R') {
                linieRomeo = i;
                coloanaRomeo = j;
                a[i][j] = ' ';
            }
            if (a[i][j] == 'J') {
                linieJulieta = i;
                coloanaJulieta = j;
                a[i][j] = ' ';
            }
        }
        fin.get();
    }
}

void lee(int liniePlecare, int coloanaPlecare, int matrice[NMAX][NMAX]) {
    for (int i = 0; i <= nrLinii + 1; i++)
        for (int j = 0; j <= nrColoane + 1; j++)
            matrice[i][j] = 0;
    matrice[liniePlecare][coloanaPlecare] = 1;
    coada.push(make_pair(liniePlecare, coloanaPlecare));
    matrice[liniePlecare][coloanaPlecare] = 1;
    while (!coada.empty()) {
        int x = coada.front().first; // linia
        int y = coada.front().second; // coloana
        coada.pop();
        for (int i = 0; i < 8; i++)
            if (a[x + dl[i]][y + dc[i]] == ' ' && matrice[x + dl[i]][y + dc[i]] == 0) {
                matrice[x + dl[i]][y + dc[i]] = 1 + matrice[x][y];
                coada.push(make_pair(x + dl[i], y + dc[i]));
            }
    }
}

void afisare() {
    int tmin = NMAX * NMAX;
    int xmin = -1;
    int ymin = -1;

    for (int i = 1; i <= nrLinii; i++)
        for (int j = 1; j <= nrColoane; j++)
            if (matriceRomeo[i][j] == matriceJulieta[i][j])
                if (matriceRomeo[i][j] < tmin && matriceRomeo[i][j] != 0) {
                    tmin = matriceRomeo[i][j];
                    xmin = i;
                    ymin = j;
                }
    fout << tmin << " " << xmin << " " << ymin;
}

int main() {
    citire();
    lee(linieRomeo, coloanaRomeo, matriceRomeo); // pentru Romeo
    lee(linieJulieta, coloanaJulieta, matriceJulieta); // pentru Julieta
    afisare();
    return 0;
}