// Popescu Paullo Robertto Karloss Grupa 2311
// Linkurile pentru fiecare problema se gasesc in main

#include <fstream>
#include <vector>
#include <queue>
#include <stack>
#include <set>
#include <algorithm>

#define maxi 100001

using namespace std;

ifstream fin("bfs.in");
ofstream fout("bfs.out");

stack<pair<int, int>> stivaComponenteBiconexe;
vector<set<int>> componenteBiconexe;

class Graf {
    int nrNoduri, nrMuchii;
    int x, y; // extremitate muchie stanga respectiv dreapta
    int vizitat[maxi] = {0};

    int niv_min[maxi] = {0}; // pt muchie critica
    int nivel[maxi] = {0}; // pt muchie critica

    vector<int> *adiacenta; // lista de vecini
    vector<int> *adiacenta2; // lista de vecini transpusa (i.e. in loc de x si y folosim y si x ca la grafuri neorientate)
    queue<int> coada2;


public:
    Graf();

    void citireBFS(int &nodPlecare);

    void BFS();

    void afisareCoadaBFS();

    void citireDFS();

    void DFS(int nodPlecare);

    void nrComponenteConexe();

    void citireBiconex();

    void biconex(int nodPlecare, int precedent, int k);

    static void afisareComponenteBiconexe();

    void citireComponenteTareConexe();

    void DF1(int nodPlecare, vector<int> &succesor, vector<int> &v, int &index);

    void DF2(int nodPlecare, vector<int> &predecesor, int &nrComponenteTareConexe);

    void afisareComponenteTareConexe();

    void havelHakimi();

    void havelHakimiCountingSort();

    void citireSortareTopologica();

    void DFS2(int nodPlecare, stack<int> &stiva);

    void sortareTopologica();

    void muchieCriticaDF(int nodPlecare);

    void GrafInfoarena();

    void GrafBFS(int nodPlecare, vector<int> &distanta);

    void apm();

    int reprezentant_tata(int nod, vector<int> &tata);

    void reuneste(int tataSursa, int tataDestinatie, vector<int> &tata, vector<int> &inaltime);

    void disjoint();

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
    coada2.push(nodPlecare);
    vizitat[coada2.back()] = 1;
}

void Graf::BFS() {
    if (!coada2.empty()) // daca mai sunt elemente in coada / nu am verificat pt toate nodurile
    {
        int nodPlecare = coada2.front(); // retin nodul de unde plec
        for (auto i: adiacenta[nodPlecare])
            if (vizitat[i] == -1) {
                // caut toate nodurile nevizitate care sunt adiacente cu nodul de plecare
                vizitat[i] = vizitat[nodPlecare] + 1; // il marcam vizitat
                coada2.push(i); // il adaug in coada PUSH
            }
        /*
        // Echivalent cu:
        for (int j = 0; j < adiacenta[nodPlecare].size(); j++)
            if (vizitat[adiacenta[nodPlecare][j]] == -1) {
                vizitat[adiacenta[nodPlecare][j]] = vizitat[nodPlecare] + 1;
                coada.push(adiacenta[nodPlecare][j]);
            }
        */
        coada2.pop();
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

void Graf::biconex(int nodPlecare, int precedent, int k) {
    vizitat[nodPlecare] = k;
    niv_min[nodPlecare] = k;
    for (auto i: adiacenta[nodPlecare]) {
        int vecin = i;
        if (vecin != precedent) { // pentru optimizare (iese din timp altfel, uneori),
            // daca vecinul curent nu s-a executat la pasul anterior
            if (!vizitat[vecin]) { // daca vecinul nu a fost vizitat
                stivaComponenteBiconexe.push(make_pair(nodPlecare, vecin));
                biconex(vecin, nodPlecare, k + 1); // reapelez DF din nodul in care am ajuns
                if (niv_min[nodPlecare] > niv_min[vecin]) // daca face parte din ciclu
                    niv_min[nodPlecare] = niv_min[vecin]; // actualizez nivelul minim
                if (niv_min[vecin] >= vizitat[nodPlecare]) {
                    // daca un vecin are nivelul minim mai mare sau egal decat nivelul tatalui sau
                    // (vizitat este pe pos de nivel din muchia critica, i.e. nivelul din arborele DF),
                    // inseamna ca nu face parte dintr-un ciclu, deci am gasit o componenta biconexa
                    set<int> aux;
                    int aux1, aux2;
                    do {
                        aux1 = stivaComponenteBiconexe.top().first;
                        aux2 = stivaComponenteBiconexe.top().second;
                        aux.insert(aux1);
                        aux.insert(aux2);
                        stivaComponenteBiconexe.pop();
                    } while (aux1 != nodPlecare || aux2 != vecin);
                    componenteBiconexe.push_back(aux);
                }
            } else if (niv_min[nodPlecare] > vizitat[vecin]) { // daca nodul curent a fost deja vizitat
                // si daca exista o muchie de intoarcere de la nodPlecare la nodul curent, exista legatura cu un stramos
                // (muchie de intoarcere de la nodPlecare la nodul curent)
                niv_min[nodPlecare] = vizitat[vecin]; // nivelul nodului de Plecare
                // va fi nivelul stramosului sau (nodul deja vizitat)
            }
        }
    }
}

void Graf::afisareComponenteBiconexe() {
    set<int>::iterator it;
    fout << componenteBiconexe.size() << "\n";

    for (auto &i: componenteBiconexe) {
        for (it = i.begin(); it != i.end(); it++) {
            fout << *it << " ";
        }
        fout << "\n";
    }
}

void Graf::citireComponenteTareConexe() {
    fin >> nrNoduri >> nrMuchii;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> x >> y;
        adiacenta[x].push_back(y); // retin lista succesorilor lui x
        adiacenta2[y].push_back(x); // retin lista predecesorilor lui x
    }
}

void Graf::DF1(int nodPlecare, vector<int> &succesor, vector<int> &v, int &index) {
    succesor[nodPlecare] = 1; // marchez nodul succesor nodului curent ca fiind vizitat
    for (auto i: adiacenta[nodPlecare]) // parcurg toti vecinii nodului
        if (!succesor[i]) // daca succesorul nu a fost vizitat
            DF1(i, succesor, v, index); // continui parcurgerea
    /*
    // Echivalent cu:
    for (int i = 0; i < adiacenta[nodPlecare].size(); i++)
        if (!succesor[adiacenta[nodPlecare][i]])
            DF1(adiacenta[nodPlecare][i]);
    */
    v[++index] = nodPlecare; // retin succesorii intr-un array
}

void Graf::DF2(int nodPlecare, vector<int> &predecesor, int &nrComponenteTareConexe) {
    predecesor[nodPlecare] = nrComponenteTareConexe; // marchez nodul predecesor nodului curent ca fiind vizitat
    for (auto i: adiacenta2[nodPlecare])
        if (!predecesor[i])
            DF2(i, predecesor, nrComponenteTareConexe);
    /*
    // Echivalent cu:
    for (int i = 0; i < adiacenta2[nodPlecare].size(); i++)
        if (!predecesor[adiacenta2[nodPlecare][i]])
            DF2(adiacenta2[nodPlecare][i]);
    */
}

// Am folosit algoritmul lui Kosaraju pentru a afla numarul de Componente Tare Conexe intr-un graf orientat
void Graf::afisareComponenteTareConexe() {
    vector<int> succesor(nrNoduri + 1, 0);
    vector<int> predecesor(nrNoduri + 1, 0);
    vector<int> v(nrNoduri + 1, 0);
    pair<int, int> p[nrNoduri + 1]; // pereche (predecesor, nod)
    int nrComponenteTareConexe = 1, index = 0;
    for (int i = 1; i <= nrNoduri; i++)
        if (succesor[i] == 0) // daca nodul i nu a fost vizitat
            DF1(i, succesor, v, index); // parcurg in adancime marcand succesorii
    for (int i = nrNoduri; i >= 1; i--)
        if (predecesor[v[i]] == 0) // daca predecesorul lui i nu a fost vizitat
        {
            DF2(v[i], predecesor, nrComponenteTareConexe); // parcurg in adancime marcand predecesorii
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
    vector<int> gradeNoduri;
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

void countingSort(vector<int> &gradeNoduri) {
    vector<int> nrAparitii(gradeNoduri.size() * gradeNoduri.size(), 0);
    int maxim = 0;
    for (int i: gradeNoduri) {
        nrAparitii[i]++;
        if (i > maxim)
            maxim = i;
    }
    int capat = 0;
    for (int i = maxim; i >= 0; i--)
        while (nrAparitii[i]) {
            nrAparitii[i]--;
            gradeNoduri[capat++] = i;
        }
}

void Graf::havelHakimiCountingSort() {
    int gradCurent, sumaGrade = 0;
    vector<int> gradeNoduri;
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
            countingSort(gradeNoduri);
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

void Graf::citireSortareTopologica() {
    fin >> nrNoduri >> nrMuchii;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> x >> y;
        adiacenta[x].push_back(y);
    }
}

void Graf::DFS2(int nodPlecare, stack<int> &stiva) {
    vizitat[nodPlecare] = 1;
    for (auto i: adiacenta[nodPlecare])
        if (!vizitat[i])
            DFS2(i, stiva);
    stiva.push(nodPlecare);
}

void Graf::sortareTopologica() {
    stack<int> stiva;
    for (int i = 1; i <= nrNoduri; ++i)
        if (!vizitat[i])
            DFS2(i, stiva);

    while (!stiva.empty()) {
        fout << stiva.top() << " ";
        stiva.pop();
    }
}

void Graf::muchieCriticaDF(int nodPlecare) {
    vizitat[nodPlecare] = 1;
    niv_min[nodPlecare] = nivel[nodPlecare]; // initializez nivelul minim cu nivelul nodului, nivel[1] = 1 la inceput
    for (auto i: adiacenta[nodPlecare])
        if (!vizitat[i]) {
            nivel[i] = nivel[nodPlecare] + 1; // actualizez nivelul nodului in care am ajuns
            muchieCriticaDF(i); // reapelez DF din nodul in care am ajuns
            niv_min[nodPlecare] = min(niv_min[nodPlecare], niv_min[i]); // cand se intoarce recursiv
            // modifica nivelul minim al nodului de plecare
            // de exemplu face DF din nodul 5..
            // de la nodul 5 exista o muchie de intoarcere la un nod care deja a fost vizitat (un stramos al sau),
            // deci o muchie de intoarcere (niv_min[5] = 3), atunci nivelul minim al nodului de plecare va fi minimul
            // dintre el si cel al nodului 5

            // daca nivelul minim al lui 5 nu este mai mic decat nivelul minim al nodului de plecare,
            // nivelul minim al nodului de plecare ramane acelasi
            if (niv_min[i] > nivel[nodPlecare]) // daca un nod are nivelul minim mai mare decat nivelul tatalui sau,
                // inseamna ca nu face parte dintr-un ciclu, deci am gasit o muchie critica
                fout << nodPlecare << " " << i << "\n";
        } else if (nivel[i] < nivel[nodPlecare] - 1)   // daca exista muchie de intoarcere
            // la un nod care fost deja vizitat (stramos)
            niv_min[nodPlecare] = min(niv_min[nodPlecare], nivel[i]); // se reactualizeaza nivelul minim al acelui nod,
    // ca fiind minimul dintre nivelul lui si nivelul stramosului/desecendentului lui
}

void Graf::GrafInfoarena() {
    int nodStart, nodEnd;
    fin >> nrNoduri >> nrMuchii >> nodStart >> nodEnd;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> x >> y;
        adiacenta[x].push_back(y);
        adiacenta[y].push_back(x);
    }
    vector<int> distantaStart(nrNoduri + 1, 0);
    vector<int> distantaEnd(nrNoduri + 1, 0);
    vector<int> frecventa(nrNoduri + 1, 0);
    vector<int> etichete;
    GrafBFS(nodStart, distantaStart);
    GrafBFS(nodEnd, distantaEnd);
    int lungimeLant = distantaStart[nodEnd];
    for (int i = 1; i <= nrNoduri; i++)
        if (distantaStart[i] + distantaEnd[i] - 1 == lungimeLant)
            frecventa[distantaStart[i]]++;
    for (int i = 1; i <= nrNoduri; i++)
        if (distantaStart[i] + distantaEnd[i] - 1 == lungimeLant && frecventa[distantaStart[i]] == 1)
            etichete.push_back(i);
    fout << etichete.size() << "\n";
    for (auto i: etichete)
        fout << i << " ";
}

void Graf::GrafBFS(int nodPlecare, vector<int> &distanta) {
    queue<int> coada;
    coada.push(nodPlecare);
    distanta[coada.back()] = 1;
    while (!coada.empty()) { // daca mai sunt elemente in coada / nu am verificat pt toate nodurile
        nodPlecare = coada.front(); // retin nodul de unde plec
        coada.pop();
        for (auto i: adiacenta[nodPlecare])
            if (!distanta[i]) {
                // caut toate nodurile nevizitate care sunt adiacente cu nodul de plecare
                distanta[i] = distanta[nodPlecare] + 1; // il marcam vizitat
                coada.push(i); // il adaug in coada PUSH
            }
    }
}

// Tema 2

struct muchie {
    int sursa, destinatie, cost;
} u[400001];

struct muchie2 {
    int tipOperatie, sursa, destinatie;
} u2[100001];

bool comp(muchie a, muchie b) {
    return a.cost < b.cost;
}

// complexitatea pentru a gasii reprezentantul este O(reprezentant)
int Graf::reprezentant_tata(int nod, vector<int> &tata) { // caut radacina nodului curent in subarbore
    while (tata[nod] != nod)
        nod = tata[nod];
    return nod;
}

// regula: intotdeauna vom unii arborele de inaltime mai mica de arborele cu inaltime mai mare
void Graf::reuneste(int tataSursa, int tataDestinatie, vector<int> &tata, vector<int> &inaltime) {
    if (inaltime[tataSursa] > inaltime[tataDestinatie])
        // daca subarborele tataSursa are inaltimea mai mare decat tataDestinatie
        tata[tataDestinatie] = tataSursa;
        // leg subarborele de inaltime mai mica (in cazul nostru tataDestinatie)
        // de subarborele de inaltime mai mare (in cazul nostru tataSursa)
    else if (inaltime[tataSursa] < inaltime[tataDestinatie])
        tata[tataSursa] = tataDestinatie;
    else if (inaltime[tataSursa] == inaltime[tataDestinatie]) {
        // daca au aceeasi inaltime
        tata[tataSursa] = tataDestinatie; // leg subarborele tataSursa de subarborele tataDestinatie
        inaltime[tataDestinatie]++; // dupa ce le-am legat inaltimea arborelui meu creste
        // de exemplu am 2 subarbori de inaltime 2, arborele meu, dupa ce i-am unit va avea inaltimea 3.
        // pot unii fie primul subarbore de al doilea sau invers
        // dupa ce i-am unit arborele meu va avea inaltimea unui subarbore + 1
        // de ce? atunci cand unesc doi subarbori, eu adaug ca fiu al radacinii subarborelui meu, celalalt subarbore
    }
}

// Pentru gasirea APM-ului am folosit algoritmul lui Kruskal
void Graf::apm() {
    vector<pair<int, int>> apm;
    fin >> nrNoduri >> nrMuchii;
    vector<int> tata(nrNoduri + 1);
    vector<int> inaltime(nrNoduri + 1);
    int costAPM = 0;
    for (int i = 1; i <= nrMuchii; i++)
        fin >> u[i].sursa >> u[i].destinatie >> u[i].cost;
    sort(u + 1, u + nrMuchii + 1, comp); // sortez crescator muchiile dupa cost
    for (int i = 1; i <= nrNoduri; i++) { // initializare complexitate O(n)
        tata[i] = i;
        inaltime[i] = 0;
    }
    for (int i = 1; i <= nrMuchii; i++) {
        int tataSursa = reprezentant_tata(u[i].sursa, tata); // caut reprezentantul nodului sursa curent
        int tataDestinatie = reprezentant_tata(u[i].destinatie,
                                               tata); // caut reprezentantantul nodului destinatie curent
        if (tataSursa != tataDestinatie) {
            // daca nu am acelasi tata inseamna ca pot sa reunesc
            reuneste(tataSursa, tataDestinatie, tata, inaltime);
            apm.push_back(make_pair(u[i].sursa, u[i].destinatie));
            costAPM += u[i].cost;
        }
    }
    nrMuchii = nrNoduri - 1; // stim ca un APM are intotdeuna nrNoduri - 1 muchii
    fout << costAPM << "\n" << nrMuchii << "\n";
    for (auto i: apm)
        fout << i.first << " " << i.second << "\n";
}

void Graf::disjoint() {
    fin >> nrNoduri >> nrMuchii;
    vector<int> tata(nrNoduri + 1);
    vector<int> inaltime(nrNoduri + 1, 0);
    for (int i = 1; i <= nrMuchii; i++)
        fin >> u2[i].tipOperatie >> u2[i].sursa >> u2[i].destinatie; // tip operatie
    for (int i = 1; i <= nrNoduri; i++) // initializare complexitate O(n)
        tata[i] = i;
    for (int i = 1; i <= nrMuchii; i++) {
        int tataSursa = reprezentant_tata(u2[i].sursa, tata);
        // caut reprezentantul nodului sursa curent
        int tataDestinatie = reprezentant_tata(u2[i].destinatie,tata);
        // caut reprezentantantul nodului destinatie curent
        if (u2[i].tipOperatie == 1)
            reuneste(tataSursa, tataDestinatie, tata, inaltime);
        else if (tataSursa == tataDestinatie)
            // daca am acelasi tata inseamna ca fac parte din aceeasi multime
            fout << "DA\n";
        else
            fout << "NU\n";
    }
}


int main() {
    // Tema 1:
    /*
    // Problema BFS (100p)
    // Link: https://infoarena.ro/problema/bfs
    // Sursa: https://infoarena.ro/job_detail/2797664?action=view-source
    int nodPlecare;
    Graf g1;
    g1.citireBFS(nodPlecare);
    g1.BFS();
    g1.afisareCoadaBFS();
    */

    /*
    // Problema DFS (100p)
    // Link: https://infoarena.ro/problema/dfs
    // Sursa: https://infoarena.ro/job_detail/2797669?action=view-source
    Graf g1;
    g1.citireDFS();
    g1.nrComponenteConexe();
    */

    /*
    // Problema Componente Biconexe (100p)
    // Link: https://infoarena.ro/problema/biconex
    // Sursa: https://infoarena.ro/job_detail/2797675?action=view-source
    Graf g1;
    g1.citireDFS();
    g1.biconex(1, 0, 1);
    g1.afisareComponenteBiconexe();
    */

    /*
    // Problema CTC (Componente Tare Conexe) (100p)
    // Link: https://infoarena.ro/problema/ctc
    // Sursa: https://infoarena.ro/job_detail/2797676?action=view-source
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
    // Problema Havel Hakimi cu Counting Sort pentru sortarea gradelor nodurilor
    Graf g1;
    g1.havelHakimiCountingSort();
    */

    /*
    // Problema Sortare topologica (100p)
    // Link: https://infoarena.ro/problema/sortaret
    // Sursa: https://infoarena.ro/job_detail/2797552?action=view-source
    Graf g1;
    g1.citireSortareTopologica();
    g1.sortareTopologica();
    */

    /*
    // Problema muchie critica (success)
    // Link: https://leetcode.com/problems/critical-connections-in-a-network/
    // Sursa: https://leetcode.com/submissions/detail/583031282/
    Graf g1;
    g1.citireDFS();
    g1.muchieCriticaDF(1);
    */

    /*
    // Problema Graf (100p)
    // Link: https://www.infoarena.ro/problema/graf
    // Sursa: https://www.infoarena.ro/job_detail/2800679?action=view-source
    Graf g1;
    g1.GrafInfoarena();
    */

    // Tema 2:
    /*
    // Problema apm (100p)
    // Link: https://infoarena.ro/problema/apm
    // Sursa: https://infoarena.ro/job_detail/2805610?action=view-source
    Graf g1;
    g1.apm();
    */

    /*
    // Problema Paduri de multimi disjuncte (100p)
    // Link: https://infoarena.ro/problema/disjoint
    // Sursa: https://infoarena.ro/job_detail/2805636?action=view-source
    Graf g1;
    g1.disjoint();
    */

    fin.close();
    fout.close();
    return 0;
}

Graf::Graf() {
    nrNoduri = nrMuchii = x = y = 0;
    adiacenta = new vector<int>[maxi];
    adiacenta2 = new vector<int>[maxi];
}

Graf::~Graf() {
    delete[] adiacenta;
    delete[] adiacenta2;
}