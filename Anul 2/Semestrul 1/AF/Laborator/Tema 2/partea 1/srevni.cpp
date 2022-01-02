#include <fstream>
#include <vector>
#include <algorithm>

using namespace std;

ifstream fin("srevni.in");
ofstream fout("srevni.out");

int nrNoduri, nrMuchii;
vector<pair<int, int>> noduri;
vector<vector<int>> listaDeAdiacenta;
vector<int> vizitat, rezultat;

void citire() {
    fin >> nrNoduri >> nrMuchii;
    listaDeAdiacenta.resize(nrNoduri + 1);
    noduri.resize(nrNoduri + 1);
    vizitat.resize(nrNoduri + 1);
    rezultat.resize(nrNoduri + 1);
    for (int i = 1; i <= nrNoduri; i++) {
        int pretAliment;
        fin >> pretAliment;
        noduri[i] = {pretAliment, i};
    }
    int sursa, destinatie;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> sursa >> destinatie;
        listaDeAdiacenta[destinatie].push_back(sursa);
    }
}

void DFS(int &nodPlecare, int &pret) {
    vizitat[nodPlecare] = 1;
    rezultat[nodPlecare] = pret;
    for (auto &i: listaDeAdiacenta[nodPlecare])
        if (!vizitat[i])
            DFS(i, pret);
}

void rezolva() {
    sort(noduri.begin(), noduri.end());
    for (int i = 1; i <= nrNoduri; i++)
        if (!vizitat[noduri[i].second])
            DFS(noduri[i].second, noduri[i].first);
}

void afisare() {
    for (int i = 1; i <= nrNoduri; i++)
        fout << rezultat[i] << " ";
}

int main() {
    citire();
    rezolva();
    afisare();

    fin.close();
    fout.close();
    return 0;
}