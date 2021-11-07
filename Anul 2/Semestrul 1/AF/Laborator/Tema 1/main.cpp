#include <fstream>
#include <vector>
#include <queue>
#include <algorithm>

#define maxi 100001

using namespace std;

ifstream fin("bfs.in");
ofstream fout("bfs.out");

class Graf {
    int nrNoduri, nrMuchii;
    int x, y; // extremitate muchie stanga respectiv dreapta
    int vizitat[maxi] = {0};
    int succesor[maxi] = {0};
    int predecesor[maxi] = {0};
    int v[maxi] = {0};
    int niv_min[maxi] = {0};
    int nivel[100] = {0};
    pair<int, int> *p; // pereche (predecesor, nod)
    vector<int> *adiacenta; // lista de vecini
    vector<int> *adiacenta2; // lista de vecini transpusa (i.e. in loc de x si y folosim y si x ca la grafuri neorientate)
    queue<int> coada;
    int nrComponenteTareConexe, index;
    vector<int> gradeNoduri;
public:
    Graf();

    void citireBFS(int &nodPlecare);

    void BFS();

    void afisareCoadaBFS();

    void citireDFS();

    void DFS(int nodPlecare);

    void nrComponenteConexe();

    void citireComponenteTareConexe();

    void DF1(int nodPlecare);

    void DF2(int nodPlecare);

    void afisareComponenteTareConexe();

    void havelHakimi();

    void muchieCriticaDF(int nodPlecare);

    ~Graf();
};

// citire graf orientat
void Graf::citireBFS(int &nodPlecare) {
    fin >> nrNoduri >> nrMuchii >> nodPlecare;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> x >> y;
        adiacenta[x].push_back(y);
    }
    for (int i = 1; i <= maxi; i++)
        vizitat[i] = -1;
    coada.push(nodPlecare);
    vizitat[coada.back()] = 1;
}

void Graf::BFS() {
    if (!coada.empty()) // daca mai sunt elemente in coada / nu am verificat pt toate nodurile
    {
        int nodPlecare = coada.front(); // retin nodul de unde plec
        for (auto i: adiacenta[nodPlecare])
            if (vizitat[i] == -1) {
                // caut toate nodurile nevizitate care sunt adiacente cu nodul de plecare
                vizitat[i] = vizitat[nodPlecare] + 1; // il marcam vizitat
                coada.push(i); // il adaug in coada PUSH
            }
        /*
        // Echivalent cu:
        for (int j = 0; j < adiacenta[nodPlecare].size(); j++)
            if (vizitat[adiacenta[nodPlecare][j]] == -1) {
                vizitat[adiacenta[nodPlecare][j]] = vizitat[nodPlecare] + 1;
                coada.push(adiacenta[nodPlecare][j]);
            }
        */
        coada.pop();
        BFS();
    }
}

void Graf::afisareCoadaBFS() {
    for (int i = 1; i <= nrNoduri; i++) {
        if (vizitat[i] == -1)
            fout << -1 << " ";
        else
            fout << vizitat[i] - 1 << " ";
    }
}

// citire graf neorientat
void Graf::citireDFS() {
    fin >> nrNoduri >> nrMuchii;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> x >> y;
        adiacenta[x].push_back(y);
        adiacenta[y].push_back(x);
    }
    nivel[1] = 1;
}

void Graf::DFS(int nodPlecare) {
    vizitat[nodPlecare] = 1;
    for (auto i: adiacenta[nodPlecare])
        if (!vizitat[i])
            DFS(i);
    /*
    // Echivalent cu:
    for (int i = 0; i < adiacenta[nodPlecare].size(); i++)
        if (!vizitat[adiacenta[nodPlecare][i]])
            DFS(adiacenta[nodPlecare][i]);
    */
}

void Graf::nrComponenteConexe() {
    int nr = 0;
    for (int i = 1; i <= nrNoduri; i++)
        if (vizitat[i] == 0) {
            nr++;
            DFS(i);
        }
    fout << nr;
}

void Graf::citireComponenteTareConexe() {
    fin >> nrNoduri >> nrMuchii;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> x >> y;
        adiacenta[x].push_back(y); // retin lista succesorilor lui x
        adiacenta2[y].push_back(x); // retin lista predecesorilor lui x
    }
}

void Graf::DF1(int nodPlecare) {
    succesor[nodPlecare] = 1; // marchez nodul succesor nodului curent ca fiind vizitat
    for (auto i: adiacenta[nodPlecare]) // parcurg toti vecinii nodului
        if (!succesor[i]) // daca succesorul nu a fost vizitat
            DF1(i); // continui parcurgerea
    /*
    // Echivalent cu:
    for (int i = 0; i < adiacenta[nodPlecare].size(); i++)
        if (!succesor[adiacenta[nodPlecare][i]])
            DF1(adiacenta[nodPlecare][i]);
    */
    v[++index] = nodPlecare; // retin succesorii intr-un array
}

void Graf::DF2(int nodPlecare) {
    predecesor[nodPlecare] = nrComponenteTareConexe; // marchez nodul predecesor nodului curent ca fiind vizitat
    for (auto i: adiacenta2[nodPlecare])
        if (!predecesor[i])
            DF2(i);
    /*
    // Echivalent cu:
    for (int i = 0; i < adiacenta2[nodPlecare].size(); i++)
        if (!predecesor[adiacenta2[nodPlecare][i]])
            DF2(adiacenta2[nodPlecare][i]);
    */
}

// Am folosit algoritmul lui Kosaraju pentru a afla numarul de Componente Tare Conexe intr-un graf orientat
void Graf::afisareComponenteTareConexe() {
    for (int i = 1; i <= nrNoduri; i++)
        if (succesor[i] == 0) // daca nodul i nu a fost vizitat
            DF1(i); // parcurg in adancime marcand succesorii
    for (int i = nrNoduri; i >= 1; i--)
        if (predecesor[v[i]] == 0) // daca predecesorul lui i nu a fost vizitat
        {
            DF2(v[i]); // parcurg in adancime marcand predecesorii
            nrComponenteTareConexe++;
        }
    fout << nrComponenteTareConexe - 1 << '\n';
    for (int i = 1; i <= nrNoduri; i++) {
        p[i].first = predecesor[i]; // predecesorul nodului curent
        p[i].second = i; // valoarea nodului curent
    }
    sort(p + 1, p + nrNoduri + 1); // sortez crescator dupa predecesor
    for (int i = 1; i <= nrNoduri; i++) {
        if (p[i].first != p[i + 1].first) {
            fout << p[i].second << '\n';
        } else {
            fout << p[i].second << " ";
        }
    }
}

void Graf::havelHakimi() {
    int gradCurent, sumaGrade = 0;
    bool ok = true; // presupun ca suma gradelor este para si ca gradul oricarui nod nu este >= decat nrNoduri sau negativ
    fin >> nrNoduri;
    for (int i = 1; i <= nrNoduri && ok; i++) {
        fin >> gradCurent;
        sumaGrade += gradCurent;
        if (gradCurent > nrNoduri - 1 || gradCurent < 0) {
            fout << "NU";
            ok = false;
        } else
            gradeNoduri.push_back(gradCurent);
    }
    if (ok && sumaGrade % 2)
        fout << "NU";
    else if (ok && sumaGrade % 2 == 0)
        while (ok) {
            sort(gradeNoduri.begin(), gradeNoduri.end(), greater<>());
            if (gradeNoduri[0] == 0) {
                fout << "DA";
                ok = false;
            }
            gradeNoduri.erase(gradeNoduri.begin());
            for (int i = 0; i < gradeNoduri[0]; i++) {
                gradeNoduri[i]--;
                if (gradeNoduri[i] < 0) {
                    fout << "NU";
                    ok = false;
                }
            }
        }
}

void Graf::muchieCriticaDF(int nodPlecare) {
    vizitat[nodPlecare] = 1;
    for (auto i: adiacenta[nodPlecare])
        if (!vizitat[i]) {
            nivel[i] = nivel[nodPlecare] + 1;
            muchieCriticaDF(i);
            niv_min[nodPlecare] = min(niv_min[nodPlecare], niv_min[i]);
            if (niv_min[i] > nivel[nodPlecare])
                fout << nodPlecare << " " << "\n";
        } else if (nivel[i] < nivel[nodPlecare] - 1)
            niv_min[nodPlecare] = min(niv_min[nodPlecare], nivel[i]);
}

//void df(int i) {
//    viz[i] = 1;
//    niv_min[i] = nivel[i];
//    for (int j = 1; j <= n; j++)
//        if (a[i][j] == 1)   ///ij muchie de avansare
//            if (viz[j] == 0)///daca nodul j nu a fost vizitat
//            {
//                nivel[j] = nivel[i] + 1;
//                df(j);
//                ///actualizare niv_min[i] - formula B
//                niv_min[i] = min(niv_min[i], niv_min[j]);
//
//                ///test ij este muchie critica
//                if (niv_min[j] > nivel[i])
//                    g << i << " " << j << endl;
//            } else if (nivel[j] < nivel[i] - 1) ///ij muchie de intoarcere
//                ///actualizare niv_min[i] - formula A
//                niv_min[i] = min(niv_min[i], nivel[j]);
//
//}

int main() {
    /*
    // Problema BFS
    // Link: https://infoarena.ro/problema/bfs
    int nodPlecare;
    Graf g1;
    g1.citireBFS(nodPlecare);
    g1.BFS();
    g1.afisareCoadaBFS();
    */

    /*
    // Problema DFS
    // Link: https://infoarena.ro/problema/dfs
    Graf g1;
    g1.citireDFS();
    g1.nrComponenteConexe();
    */

    /*
    // Problema CTC (Componente Tare Conexe)
    // Link: https://infoarena.ro/problema/ctc
    Graf g1;
    g1.citireComponenteTareConexe();
    g1.afisareComponenteTareConexe();
    */

    /*
    // Problema Havel Hakimi
    Graf g1;
    g1.havelHakimi();
    */

    /*
    // Problema muchie critica
    // Link: https://leetcode.com/problems/critical-connections-in-a-network/
    Graf g1;
    g1.citireDFS();
    g1.muchieCriticaDF(1);
     */

    fin.close();
    fout.close();
    return 0;
}

Graf::Graf() {
    nrNoduri = nrMuchii = x = y = index = 0;
    nrComponenteTareConexe = 1;
    adiacenta = new vector<int>[maxi];
    adiacenta2 = new vector<int>[maxi];
    p = new pair<int, int>[maxi];
}

Graf::~Graf() {
    delete[] adiacenta;
    delete[] adiacenta2;
    delete[] p;
}