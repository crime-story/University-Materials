#include <fstream>
#include <queue>
 
#define INFINIT 1000000005
 
using namespace std;
 
ifstream f("distante.in");
ofstream g("distante.out");
 
 
vector<pair<int, int> > listaDeAdiacenta[50001];
priority_queue<pair<int, int>, vector<pair<int, int> >, greater<pair<int, int> > > minHeap;
int nrGrafuri, nrNoduri, nrMuchii, s, vizitat[50001], distante[50001], distanteDate[50001], ok = 0;
 
void citire() {
    f >> nrNoduri >> nrMuchii >> s;
    for (int j = 1; j <= nrNoduri; j++)
        f >> distanteDate[j];
    for (int i = 1; i <= nrMuchii; i++) {
        int x, y, c;
        f >> x >> y >> c;
        listaDeAdiacenta[x].push_back(make_pair(c, y));
        listaDeAdiacenta[y].push_back(make_pair(c, x));
    }
}
 
void dijsktra(int nodPlecare) {
    for (int i = 1; i <= nrNoduri; i++)
        distante[i] = INFINIT;
    distante[nodPlecare] = 0;
    minHeap.push(make_pair(distante[nodPlecare], nodPlecare));
    while (!minHeap.empty()) {
        int nodCurent = minHeap.top().second;
        minHeap.pop();
        for (auto &i: listaDeAdiacenta[nodCurent]) {
            int vecin = i.second;
            int cost = i.first;
            if (distante[nodCurent] + cost < distante[vecin]) {
                distante[vecin] = distante[nodCurent] + cost;
                if (!vizitat[vecin])
                    minHeap.push(make_pair(distante[vecin], vecin));
            }
        }
        vizitat[nodCurent] = 1;
    }
}
 
void afisare() {
    for (int i = 1; i <= nrNoduri; i++) {
        if (distanteDate[i] != distante[i]) {
            ok = 1;
            g << "NU" << '\n';
            break;
        }
    }
    if (ok == 0)
        g << "DA" << '\n';
}
 
void reinitializare() {
    for (int i = 1; i <= nrNoduri; i++) {
        listaDeAdiacenta[i].clear();
        vizitat[i] = 0;
        distante[nrNoduri] = INFINIT;
    }
    ok = 0;
}
 
int main() {
    f >> nrGrafuri;
    for (int i = 1; i <= nrGrafuri; i++) {
        citire();
        dijsktra(s);
        afisare();
        reinitializare();
    }
    return 0;
}