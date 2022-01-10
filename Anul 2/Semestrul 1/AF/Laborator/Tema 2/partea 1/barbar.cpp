#include <fstream>
#include <vector>
#include <queue>
 
using namespace std;
 
ifstream fin("barbar.in");
ofstream fout("barbar.out");
 
int dl[4] = {0, 1, 0, -1};
int dc[4] = {1, 0, -1, 0};
 
int n, m;
vector<vector<int>> matrice, drum;
queue<pair<int, int>> coada;
int linieStart, coloanaStart, linieEnd, coloanaEnd;
int mini;
 
int cautDrum(int &valoareMaxima) {
    while (!coada.empty())
        coada.pop();
    drum.clear();
 
    drum.resize(n + 2, vector<int>(m + 2, 0));
    coada.push(make_pair(linieStart, coloanaStart));
    drum[linieStart][coloanaStart] = 1;
    int rez = 0;
    while (!coada.empty() && rez == 0) {
        int x = coada.front().first;
        int y = coada.front().second;
        coada.pop();
        for (int i = 0; i < 4; i++)
            if (drum[x + dl[i]][y + dc[i]] == 0 && (matrice[x + dl[i]][y + dc[i]] - 1) >= valoareMaxima) {
                drum[x + dl[i]][y + dc[i]] = 1;
                if (x + dl[i] == linieEnd && y + dc[i] == coloanaEnd)
                    rez = 1;
                coada.push(make_pair(x + dl[i], y + dc[i]));
            }
    }
    return rez;
}
 
int cautareBinara(int start, int stop) {
    int pivot;
    while (stop - start > 1) {
        pivot = (start + stop) >> 1;
        if (cautDrum(pivot) == 1)
            start = pivot;
        else
            stop = pivot;
    }
    if (start == 0)
        return -1;
    else
        return start;
 
}
 
void lee() {
    while (!coada.empty()) {
        int x = coada.front().first;
        int y = coada.front().second;
        coada.pop();
        for (int i = 0; i < 4; i++)
            if (matrice[x + dl[i]][y + dc[i]] == 0) {
                matrice[x + dl[i]][y + dc[i]] = 1 + matrice[x][y];
                coada.push(make_pair(x + dl[i], y + dc[i]));
            }
    }
}
 
void bordare() {
    for (int i = 0; i <= n + 1; i++)
        matrice[i][0] = matrice[i][m + 1] = -1;
    for (int i = 0; i <= m + 1; i++)
        matrice[0][i] = matrice[n + 1][i] = -1;
}
 
int main() {
    fin >> n >> m;
    matrice.resize(n + 2, vector<int>(m + 2, 0));
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            char caracter;
            fin >> caracter;
            if (caracter == '*')
                matrice[i][j] = -1;
            else if (caracter == 'D') {
                coada.push(make_pair(i, j));
                matrice[i][j] = 1;
            } else if (caracter == 'I') {
                linieStart = i;
                coloanaStart = j;
            } else if (caracter == 'O') {
                linieEnd = i;
                coloanaEnd = j;
            }
        }
    }
    bordare();
    lee();
    if (matrice[linieStart][coloanaStart] <= matrice[linieEnd][coloanaEnd])
        mini = matrice[linieStart][coloanaStart];
    else
        mini = matrice[linieEnd][coloanaEnd];
    fout << cautareBinara(0, mini);
 
    fin.close();
    fout.close();
    return 0;
}