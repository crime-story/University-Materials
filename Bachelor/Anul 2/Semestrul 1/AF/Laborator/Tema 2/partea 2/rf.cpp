#include <fstream>
 
#define NMAX 257
 
using namespace std;
 
ifstream fin("rf.in");
ofstream fout("rf.out");
 
int n;
int distante[NMAX][NMAX], frecventa[NMAX][NMAX];
 
void afisare(int matrice[NMAX][NMAX]) {
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++)
            fout << matrice[i][j] << " ";
        fout << "\n";
    }
}
 
void royFloyd() {
    for (int k = 1; k <= n; k++)
        for (int i = 1; i <= n; i++)
            for (int j = i + 1; j <= n; j++)
                if (distante[i][j] > distante[i][k] + distante[k][j]) {
                    frecventa[i][j] = frecventa[i][k] + frecventa[k][j];
                    distante[i][j] = distante[i][k] + distante[k][j];
                    frecventa[j][i] = frecventa[i][j];
                    distante[j][i] = distante[i][j];
                } else if (frecventa[i][j] < frecventa[i][k] + frecventa[k][j]
                           && distante[i][j] == distante[i][k] + distante[k][j]) {
                    frecventa[i][j] = frecventa[i][k] + frecventa[k][j];
                    frecventa[j][i] = frecventa[i][j];
                }
}
 
void citire() {
    fin >> n;
    for (int i = 1; i <= n; i++)
        for (int j = 1; j <= n; j++) {
            fin >> distante[i][j];
            if (distante[i][j] != 0)
                frecventa[i][j] = 1;
        }
}
 
int main() {
    citire();
    royFloyd();
    afisare(distante);
    afisare(frecventa);
 
    fin.close();
    fout.close();
    return 0;
}