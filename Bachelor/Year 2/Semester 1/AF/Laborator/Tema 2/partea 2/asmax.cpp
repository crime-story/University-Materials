#include <fstream>
#include <vector>
 
using namespace std;
ifstream fin("asmax.in");
ofstream fout("asmax.out");
 
vector<int> listaDeAdiacenta[16001];
vector<int> vizitat;
int valoare[100001];
int n, maxi = -10001;
 
int DFS(int nodPlecare) {
    vizitat[nodPlecare] = 1;
    for (auto &i: listaDeAdiacenta[nodPlecare]) {
        if (!vizitat[i]) {
            int suma = DFS(i);
            if (valoare[nodPlecare] + suma > valoare[nodPlecare])
                valoare[nodPlecare] += suma;
        }
    }
    if (maxi < valoare[nodPlecare])
        maxi = valoare[nodPlecare];
    return valoare[nodPlecare];
}
 
int main() {
    int i;
    fin >> n;
    for (int i = 1; i <= n; i++)
        fin >> valoare[i];
    for (i = 1; i <= n; i++) {
        int x, y;
        fin >> x >> y;
        listaDeAdiacenta[x].push_back(y);
        listaDeAdiacenta[y].push_back(x);
    }
    vizitat.resize(n + 2, 0);
    DFS(1);
    fout << maxi;
 
    fin.close();
    fout.close();
    return 0;
}