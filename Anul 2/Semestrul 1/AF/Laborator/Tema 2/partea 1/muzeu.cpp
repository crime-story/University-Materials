#include <fstream>
#include <queue>

using namespace std;

ifstream fin("muzeu.in");
ofstream fout("muzeu.out");

int n;
int dl[4] = {0, 1, 0, -1};
int dc[4] = {1, 0, -1, 0};

void lee(vector<vector<int>> &matrice, queue<pair<int, int>> &coada) {
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

void bordare(vector<vector<int>> &matrice) {
    for (int i = 0; i <= n + 1; i++)
        matrice[0][i] = matrice[n + 1][i] = matrice[i][0] = matrice[i][n + 1] = -1; // bordez matricea cu -1
}

void construiesteMatricea(vector<vector<int>> &matrice, queue<pair<int, int>> &coada) {
    for (int i = 1; i <= n; i++)
        for (int j = 1; j <= n; j++) {
            char simbol;
            fin >> simbol;
            if (simbol == 'P') {
                matrice[i][j] = 1;
                coada.push(make_pair(i, j));
            } else if (simbol == '#')
                matrice[i][j] = -1;
        }
}

void afisare(vector<vector<int>> &matrice) {
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++)
            fout << matrice[i][j] - 1 << " ";
        fout << "\n";
    }
}

int main() {
    fin >> n;
    vector<vector<int>> matrice(n + 2, vector<int>(n + 2, 0));
    queue<pair<int, int>> coada;
    construiesteMatricea(matrice, coada);
    bordare(matrice);
    lee(matrice, coada);
    afisare(matrice);

    fin.close();
    fout.close();
    return 0;
}