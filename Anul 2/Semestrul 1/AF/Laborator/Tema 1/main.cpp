// Popescu Paullo Robertto Karloss Grupa 2311
// Fiecare functie apelata in main contine link-ul catre problema rezolvata

#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <stack>
#include <set>
#include <algorithm>
#include <climits>

#define INFINIT INT_MAX

using namespace std;

ifstream fin("bfs.in");
ofstream fout("bfs.out");

struct muchie {
    int sursa, destinatie, cost, capacitate;

    muchie(int destinatie, int cost, int capacitate) : destinatie(destinatie), cost(cost), capacitate(capacitate) {}

    muchie(int sursa, int destinatie, int cost, int capacitate) : sursa(sursa), destinatie(destinatie), cost(cost),
                                                                  capacitate(capacitate) {}

    bool operator<(const muchie &ob) const {
        return cost < ob.cost;
    }
};

class Graf {
private:
    int nrNoduri, nrMuchii;
    bool eOrientat, arePonderi, areCapacitati, trebuieListaDeMuchii;
    vector<vector<muchie>> listaDeAdiacenta; // lista de vecini
    vector<muchie> listaDeMuchii; // lista de muchii

public:
    // creaza graful cu nrNoduri, nrMuchii, eOrientat, arePonderi, areCapacitati, trebuieListaDeMuchii
    Graf(const int nrNoduri, const int nrMuchii, const bool eOrientat, const bool arePonderi, const bool areCapacitati,
         const bool trebuieListaDeMuchii);

    // construieste lista de adiacenta a grafului
    void citire();

    // returneaza un vector cu distantele minime de la un nod dat ca parametru la toate nodurile din graf
    vector<int> numarMinimArce(int &nodPlecare);

    // returneaza numarul de componente conexe dintr-un graf neorientat
    int numarComponenteConexe();

    // returneaza un vector format din componetele biconexe intr-un graf neorientat
    // pentru a afisa numarul de componente biconexe vom printa size-ul acestui vector
    vector<set<int>> componenteBiconexe();

    // returneaza o pereche formata din numarul de componente tare conexe
    // si un vector format din componentele tare conexe daca graful este orientat
    pair<int, vector<vector<int>>> componenteTareConexe();

    // primeste ca parametru true daca vrem sa folosim counting sort pentru sortarea gradelor
    // sau false daca vrem sa folosim sortarea nativa pentru sortarea gradelor nodurilor
    // verifica daca se poate construi un graf avand dat gradele pentru fiecare nod,
    // returneaza true daca se poate construi un graf, sau false daca nu se poate construi
    bool havelHakimi(const bool sortareCountingSort) const;

    // returneaza o stiva in care tinem nodurile grafului dupa ce am efectuat o sortare topologica
    // sortarea topologica functioneaza doar pe grafuri orientate!
    stack<int> sortareTopologica();

    // returneaza un vector format din muchiile critice, pereche sursa, destinatie
    vector<pair<int, int>> muchiiCritice();

    // primeste ca parametru nodul de plecare si nodul de oprire si
    // returneaza un vector format din nodurile comune lanturilor de lungime minima de la nodul de plecare la nodul de oprire
    // intr-un graf neorientat
    // pentru a afisa numarul de noduri comune se va printa pe size-ul vectorului returnat
    vector<int> grafInfoarena(int &nodStart, int &nodEnd);

    // returneaza o pereche formata din costul total al arborelui partial de cost minim
    // si un vector format din muchiile care alcatuisc arborele partial de cost minim
    // vectorul este format din perechi de tipul {sursa, destinatie}
    // stim ca numarul de muchii care alcatuiesc arborele partial de cost minim este egal cu numarul de noduri - 1
    pair<int, vector<pair<int, int>>> apm();

    // pentru operatia de tip 1 se reunesc multimile din care fac parte nodurile respective
    // pentru operatia de tip 2 se afiseaza in fisier daca cele 2 noduri se afla in aceeasi multime
    void disjoint();

    // returneaza un vector format din distantele minime de la nodul 1 la oricare alt nod intr-un graf orientat
    // folosind algoritmul lui Dijsktra, care functioneaza doar daca avem muchii cu cost pozitiv, si nu avem cicluri negative!
    vector<int> dijkstra();

    // returneaza un vector format din distantele minime de la nodul 1 la oricare alt nod intr-un graf orientat
    // folosind algoritmul lui Bellman-Ford, care functioneaza cu muchii negative, dar nu functioneaza cu cicluri negative!
    vector<int> bellmanFord();

    // primeste ca parametru matrice costurilor si
    // returneaza distantele minime de la oricare nod la oricare alt nod
    vector<vector<int>> royFloyd(vector<vector<int>> &matrice);

    // primeste ca parametru sursa si destinatia
    // returneaza fluxul maxim care poate fi transmis din sursa la destinatie
    // stim ca fluxul maxim = taietura minima
    int maxFlow(int sursa, int destinatie);

    // returneaza lungimea celui mai lung lant dintr-un arbore
    int diametruArbore();

    // afiseaza lista de adiacenta
    // de exemplu
    // -> Nodul 2 este adiacent cu:
    // nodul 1 cu costul 5 si capacitatea 0
    // nodul 3 cu costul 7 si capacitatea 0
    // etc.
    void afiseazaListaDeAdiacenta();

    // destructor unde curatam lista de adiacenta
    ~Graf();

private:
    void BFSrecursiv(queue<int> &coada, vector<int> &vizitat);

    void DFSrecursiv(int &nodPlecare, vector<int> &vizitat);

    void biconex(int nodPlecare, int precedent, int k, stack<pair<int, int>> &stivaComponenteBiconexe,
                 vector<set<int>> &componenteBiconexe, vector<int> &vizitat, vector<int> &niv_min);

    vector<vector<muchie>> grafTranspus();

    void DFSgrafTranspus(int nodPlecare, vector<vector<int>> &componente, int &nr, vector<int> &vizitat,
                         vector<vector<muchie>> &listaDeAdiacentaTranspusa);

    void DFSsortareTopologica(int nodPlecare, stack<int> &stiva, vector<int> &vizitat);

    void DFmuchieCritica(int nodPlecare, vector<int> &vizitat, vector<int> &niv_min, vector<int> &nivel,
                         vector<pair<int, int>> &muchii);

    void BFSgrafInfoarena(int nodPlecare, vector<int> &distanta);

    static int reprezentant_tata(int nod, vector<int> &tata);

    static void reuneste(int tataSursa, int tataDestinatie, vector<int> &tata, vector<int> &inaltime);

    static int BFSmaxFlow(vector<vector<int>> &capacitati, int sursa, int destinatie, vector<int> &tati,
                          vector<vector<int>> &flux, vector<bool> &vizitat, vector<vector<int>> &grafRezidual);

    vector<int> rezolvaBFS2(int nodPlecare);
};

void Graf::BFSrecursiv(queue<int> &coada, vector<int> &vizitat) {
    if (!coada.empty()) // daca mai sunt elemente in coada / nu am verificat pt toate nodurile
    {
        int nodPlecare = coada.front(); // retin nodul de unde plec
        for (auto &i: listaDeAdiacenta[nodPlecare])
            if (vizitat[i.destinatie] == -1) {
                // caut toate nodurile nevizitate care sunt adiacente cu nodul de plecare
                vizitat[i.destinatie] = vizitat[nodPlecare] + 1; // il marcam vizitat
                coada.push(i.destinatie); // il adaug in coada PUSH
            }
        coada.pop();
        BFSrecursiv(coada, vizitat);
    }
}

vector<int> Graf::numarMinimArce(int &nodPlecare) {
    vector<int> vizitat(nrNoduri + 1, -1);
    queue<int> coada;
    coada.push(nodPlecare);
    vizitat[coada.back()] = 1;
    BFSrecursiv(coada, vizitat);
    for (int i = 1; i <= nrNoduri; i++) {
        if (vizitat[i] != -1)
            vizitat[i] -= 1;
    }
    return vizitat;
}

void Graf::DFSrecursiv(int &nodPlecare, vector<int> &vizitat) {
    vizitat[nodPlecare] = 1;
    for (auto &i: listaDeAdiacenta[nodPlecare])
        if (!vizitat[i.destinatie])
            DFSrecursiv(i.destinatie, vizitat);
}

int Graf::numarComponenteConexe() {
    vector<int> nivel(nrNoduri + 1, 0);
    vector<int> vizitat(nrNoduri + 1, 0);
    nivel[1] = 0;
    int nr = 0;
    for (int i = 1; i <= nrNoduri; i++)
        if (vizitat[i] == 0) {
            nr++;
            DFSrecursiv(i, vizitat);
        }
    return nr;
}

void Graf::biconex(int nodPlecare, int precedent, int k, stack<pair<int, int>> &stivaComponenteBiconexe,
                   vector<set<int>> &componenteBiconexe, vector<int> &vizitat, vector<int> &niv_min) {
    vizitat[nodPlecare] = k;
    niv_min[nodPlecare] = k;
    for (auto &i: listaDeAdiacenta[nodPlecare]) {
        int vecin = i.destinatie;
        if (vecin != precedent) { // pentru optimizare (iese din timp altfel, uneori),
            // daca vecinul curent nu s-a executat la pasul anterior
            if (!vizitat[vecin]) { // daca vecinul nu a fost vizitat
                stivaComponenteBiconexe.push(make_pair(nodPlecare, vecin));

                biconex(vecin, nodPlecare, k + 1, stivaComponenteBiconexe, componenteBiconexe, vizitat,
                        niv_min); // reapelez DF din nodul in care am ajuns
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

vector<set<int>> Graf::componenteBiconexe() {
    stack<pair<int, int>> stivaComponenteBiconexe;
    vector<set<int>> componenteBiconexe;

    vector<int> vizitat(nrNoduri + 1, 0);
    vector<int> niv_min(nrNoduri + 1, 0);

    biconex(1, 0, 1, stivaComponenteBiconexe, componenteBiconexe, vizitat, niv_min);
    return componenteBiconexe;
}

vector<vector<muchie>> Graf::grafTranspus() {
    vector<vector<muchie>> listaDeAdiacentaTranspusa(nrNoduri + 1, vector<muchie>());
    for (int i = 1; i <= nrNoduri; i++) {
        for (auto &j: listaDeAdiacenta[i])
            listaDeAdiacentaTranspusa[j.destinatie].push_back(muchie(i, j.cost, j.capacitate));
    }
    return listaDeAdiacentaTranspusa;
}

void Graf::DFSsortareTopologica(int nodPlecare, stack<int> &stiva, vector<int> &vizitat) {
    vizitat[nodPlecare] = 1;
    for (auto &i: listaDeAdiacenta[nodPlecare])
        if (!vizitat[i.destinatie])
            DFSsortareTopologica(i.destinatie, stiva, vizitat);
    stiva.push(nodPlecare);
}

void Graf::DFSgrafTranspus(int nodPlecare, vector<vector<int>> &componente, int &nr, vector<int> &vizitat,
                           vector<vector<muchie>> &listaDeAdiacentaTranspusa) {
    vizitat[nodPlecare] = 2;
    componente[nr].push_back(nodPlecare);
    for (auto &i: listaDeAdiacentaTranspusa[nodPlecare])
        if (vizitat[i.destinatie] == 1)
            DFSgrafTranspus(i.destinatie, componente, nr, vizitat, listaDeAdiacentaTranspusa);
}

// Am folosit algoritmul lui Kosaraju pentru a afla numarul de Componente Tare Conexe intr-un graf orientat
pair<int, vector<vector<int>>> Graf::componenteTareConexe() {
//    vector<vector<muchie>> listaDeAdiacentaTranspusa = grafTranspus();
    vector<vector<muchie>> listaDeAdiacentaTranspusa(nrNoduri + 1, vector<muchie>());
    for (int i = 1; i <= nrNoduri; i++)
        for (auto &j: listaDeAdiacenta[i])
            listaDeAdiacentaTranspusa[j.destinatie].push_back(muchie(i, j.cost, j.capacitate));
    // lista de vecini transpusa
    // (i.e. in loc de sursa si destinatie folosim destinatie si sursa ca la grafuri neorientate)

    vector<int> vizitat(nrNoduri + 1, 0);
    vector<vector<int>> componente(nrNoduri + 1, vector<int>());
    stack<int> stiva;
    for (int i = 1; i <= nrNoduri; i++)
        if (!vizitat[i])
            DFSsortareTopologica(i, stiva, vizitat);

    int nr = 0;
    while (!stiva.empty()) {
        int nodCurent = stiva.top();
        if (vizitat[nodCurent] == 1) {
            nr++;
            DFSgrafTranspus(nodCurent, componente, nr, vizitat, listaDeAdiacentaTranspusa);
        }
        stiva.pop();
    }
    pair<int, vector<vector<int>>> rezultat;
    return make_pair(nr, componente);
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

bool Graf::havelHakimi(const bool sortareCountingSort) const {
    int gradCurent, sumaGrade = 0;
    vector<int> gradeNoduri;
    for (int i = 1; i <= nrNoduri; i++) {
        fin >> gradCurent;
        sumaGrade += gradCurent;
        if (gradCurent > nrNoduri - 1 || gradCurent < 0) {
            return false;
        } else
            gradeNoduri.push_back(gradCurent);
    }
    if (sumaGrade % 2) // daca suma gradelor nu e para
        return false;
    else // daca suma gradelor e para
        while (true) {
            if (sortareCountingSort)
                countingSort(gradeNoduri);
            else
                sort(gradeNoduri.begin(), gradeNoduri.end(), greater<>());
            if (gradeNoduri[0] == 0)
                return true;
            gradeNoduri.erase(gradeNoduri.begin());
            for (int i = 0; i < gradeNoduri[0]; i++) {
                gradeNoduri[i]--;
                if (gradeNoduri[i] < 0)
                    return false;
            }
        }
}

stack<int> Graf::sortareTopologica() {
    vector<int> vizitat(nrNoduri + 1, 0);
    stack<int> stiva;
    for (int i = 1; i <= nrNoduri; ++i)
        if (!vizitat[i])
            DFSsortareTopologica(i, stiva, vizitat);
    return stiva;
}

void Graf::DFmuchieCritica(int nodPlecare, vector<int> &vizitat, vector<int> &niv_min, vector<int> &nivel,
                           vector<pair<int, int>> &muchii) {
    vizitat[nodPlecare] = 1;
    niv_min[nodPlecare] = nivel[nodPlecare]; // initializez nivelul minim cu nivelul nodului, nivel[1] = 1 la inceput
    for (auto &i: listaDeAdiacenta[nodPlecare])
        if (!vizitat[i.destinatie]) {
            nivel[i.destinatie] = nivel[nodPlecare] + 1; // actualizez nivelul nodului in care am ajuns
            DFmuchieCritica(i.destinatie, vizitat, niv_min, nivel, muchii); // reapelez DF din nodul in care am ajuns
            niv_min[nodPlecare] = min(niv_min[nodPlecare], niv_min[i.destinatie]); // cand se intoarce recursiv
            // modifica nivelul minim al nodului de plecare
            // de exemplu face DF din nodul 5..
            // de la nodul 5 exista o muchie de intoarcere la un nod care deja a fost vizitat (un stramos al sau),
            // deci o muchie de intoarcere (niv_min[5] = 3), atunci nivelul minim al nodului de plecare va fi minimul
            // dintre el si cel al nodului 5

            // daca nivelul minim al lui 5 nu este mai mic decat nivelul minim al nodului de plecare,
            // nivelul minim al nodului de plecare ramane acelasi
            if (niv_min[i.destinatie] >
                nivel[nodPlecare]) // daca un nod are nivelul minim mai mare decat nivelul tatalui sau,
                // inseamna ca nu face parte dintr-un ciclu, deci am gasit o muchie critica
                muchii.push_back(make_pair(nodPlecare, i.destinatie));
        } else if (nivel[i.destinatie] < nivel[nodPlecare] - 1)   // daca exista muchie de intoarcere
            // la un nod care fost deja vizitat (stramos)
            niv_min[nodPlecare] = min(niv_min[nodPlecare],
                                      nivel[i.destinatie]); // se reactualizeaza nivelul minim al acelui nod,
    // ca fiind minimul dintre nivelul lui si nivelul stramosului/desecendentului lui
}

vector<pair<int, int>> Graf::muchiiCritice() {
    vector<int> vizitat(nrNoduri + 1, 0);
    vector<int> niv_min(nrNoduri + 1, 0);
    vector<int> nivel(nrNoduri + 1, 0);
    vector<pair<int, int>> rez;
    vizitat[1] = 0;
    DFmuchieCritica(1, vizitat, niv_min, nivel, rez);

    return rez;
}

void Graf::BFSgrafInfoarena(int nodPlecare, vector<int> &distanta) {
    queue<int> coada;
    coada.push(nodPlecare);
    distanta[coada.back()] = 1;
    while (!coada.empty()) { // daca mai sunt elemente in coada / nu am verificat pt toate nodurile
        nodPlecare = coada.front(); // retin nodul de unde plec
        coada.pop();
        for (auto &i: listaDeAdiacenta[nodPlecare])
            if (!distanta[i.destinatie]) {
                // caut toate nodurile nevizitate care sunt adiacente cu nodul de plecare
                distanta[i.destinatie] = distanta[nodPlecare] + 1; // il marcam vizitat
                coada.push(i.destinatie); // il adaug in coada PUSH
            }
    }
}

vector<int> Graf::grafInfoarena(int &nodStart, int &nodEnd) {
    vector<int> distantaStart(nrNoduri + 1, 0);
    vector<int> distantaEnd(nrNoduri + 1, 0);
    vector<int> frecventa(nrNoduri + 1, 0);
    vector<int> etichete;
    BFSgrafInfoarena(nodStart, distantaStart);
    BFSgrafInfoarena(nodEnd, distantaEnd);
    int lungimeLant = distantaStart[nodEnd];
    for (int i = 1; i <= nrNoduri; i++)
        if (distantaStart[i] + distantaEnd[i] - 1 == lungimeLant)
            frecventa[distantaStart[i]]++;
    for (int i = 1; i <= nrNoduri; i++)
        if (distantaStart[i] + distantaEnd[i] - 1 == lungimeLant && frecventa[distantaStart[i]] == 1)
            etichete.push_back(i);

    return etichete;
}

// <summary>
// gaseste reprezentantul unui nod
// </summary>
int Graf::reprezentant_tata(int nod, vector<int> &tata) {
    while (tata[nod] != nod)
        nod = tata[nod];
    return nod;
}

// <summary>
// reuneste doua multimi disjuncte
// </summary>
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

pair<int, vector<pair<int, int>>> Graf::apm() {
    vector<pair<int, int>> apm;
    vector<int> tata(nrNoduri + 1);
    vector<int> inaltime(nrNoduri + 1);
    int costAPM = 0;
    sort(listaDeMuchii.begin(), listaDeMuchii.end()); // sortez crescator muchiile dupa cost
    for (int i = 1; i <= nrNoduri; i++) { // initializare complexitate O(n)
        tata[i] = i;
        inaltime[i] = 0;
    }
    for (auto &i: listaDeMuchii) {
        int tataSursa = reprezentant_tata(i.sursa, tata); // caut reprezentantul nodului sursa curent
        int tataDestinatie = reprezentant_tata(i.destinatie, tata); // caut reprezentantantul nodului destinatie curent
        if (tataSursa != tataDestinatie) {
            // daca nu am acelasi tata inseamna ca pot sa reunesc
            reuneste(tataSursa, tataDestinatie, tata, inaltime);
            apm.push_back({i.sursa, i.destinatie});
            costAPM += i.cost;
        }
    }

    return make_pair(costAPM, apm);
}

void Graf::disjoint() {
    // in citire avem asa:
    // listaDemuchii[i] = {tipOperatie, sursa, destinatie};

    // Deci in cazul asta:
    // listaDemuchii[i].sursa = tipOperatie
    // listaDemuchii[i].destinatie = sursa
    // listaDemuchii[i].cost = destinatie
    vector<int> tata(nrNoduri + 1);
    vector<int> inaltime(nrNoduri + 1, 0);
    vector<bool> rez;
    for (int i = 1; i <= nrNoduri; i++) // initializare complexitate O(n)
        tata[i] = i;
    for (auto &i: listaDeMuchii) {
        int tataSursa = reprezentant_tata(i.destinatie, tata);
        // caut reprezentantul nodului sursa curent
        int tataDestinatie = reprezentant_tata(i.cost, tata);
        // caut reprezentantantul nodului destinatie curent
        if (i.sursa == 1)
            reuneste(tataSursa, tataDestinatie, tata, inaltime);
        else if (tataSursa == tataDestinatie)
            // daca am acelasi tata inseamna ca fac parte din aceeasi multime
            fout << "DA\n";
        else
            fout << "NU\n";
    }
}

vector<int> Graf::dijkstra() {
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap; // {cost, nodDestinatie}
    vector<int> distante(nrNoduri + 1, INFINIT); // initial toate distantele sunt infinit
    vector<int> vizitat(nrNoduri + 1, 0); // toate nodurile nu sunt vizitate
    minHeap.push(make_pair(0, 1)); // introduc in minHeap prima data costul de la nodStart, care este 0 si nodulStart
    distante[1] = 0; // distanta pana la nodulStart este 0

    while (!minHeap.empty()) {
        int nod = minHeap.top().second; // iau nodul cel mai apropiat din minHeap
        minHeap.pop(); // il scot din minHeap
        if (!vizitat[nod]) { // daca nu am verificat inca nodul
            vizitat[nod] = 1; // il marchez ca fiind verificat
            for (auto &i: listaDeAdiacenta[nod]) // iau toate nodurile adiacente cu el
                if (distante[nod] + i.cost < distante[i.destinatie]) {
                    // daca distanta pana la nodul in care sunt + costul pana la nodul adiacent cu el
                    // este mai mic decat distanta pana nodul adiacent cu el
                    // atunci actualizez distanta ca fiind:
                    // distanta pana nodul meu + costul pana nodul adiacent cu el
                    distante[i.destinatie] = distante[nod] + i.cost;
                    minHeap.push(make_pair(distante[i.destinatie], i.destinatie));
                    // introduc in minHeap distanta actualizata pana la nodul adiacent cu nodul meu,
                    // si nodul adiacent cu mine
                }
        }
    }
    return distante;
}

vector<int> Graf::bellmanFord() {
    vector<int> distante(nrNoduri + 1, INFINIT);
    vector<int> vizitat(nrNoduri + 1, 0);
    vector<int> apartCoada(nrNoduri + 1, 0); // verfica daca un nod se gaseste in coada
    // de ce? pentru ca nu vreau sa il pun de mai multe ori in coada
    // daca l-as pune de mai multe ori in coada, ar verifica pentru aceasi imbunatatire de mai multe ori (acelasi drum)
    // daca verific de mai multe ori acelasi drum, se incrementeaza counter-ul de mai multe ori la acelasi pas,
    // ceea ce imi va strica verificarea de ciclu negativ
    bool cicluNegativ = false;
    queue<int> coada2;
    coada2.push(1); // introduc in coada nodulStart
    apartCoada[1] = 1; //
    distante[1] = 0; // distanta pana la nodulStart este 0

    while (!coada2.empty() && !cicluNegativ) {
        int nod = coada2.front();
        coada2.pop();
        apartCoada[nod] = 0;

        for (auto &i: listaDeAdiacenta[nod])
            if (distante[nod] + i.cost < distante[i.destinatie]) {
                distante[i.destinatie] = distante[nod] + i.cost;
                vizitat[i.destinatie]++;

                if (vizitat[i.destinatie] >= nrNoduri) {
                    // daca am vizitat un nod adiacent cu mine de mai multe ori decat numarul de noduri
                    // inseamna ca avem un ciclu negativ
                    cicluNegativ = true;
                    break;
                }

                if (!apartCoada[i.destinatie]) {
                    coada2.push(i.destinatie);
                    apartCoada[i.destinatie] = 1;
                }
            }
    }
    if (cicluNegativ)
        distante.clear();
    return distante;
}

vector<vector<int>> Graf::royFloyd(vector<vector<int>> &matrice) {
    vector<vector<int>> distante = matrice;
    for (int i = 1; i <= nrNoduri; i++)
        for (int j = 1; j <= nrNoduri; j++)
            if (matrice[i][j] == 0 && i != j)
                distante[i][j] = 1005;
    for (int k = 1; k <= nrNoduri; k++)
        for (int i = 1; i <= nrNoduri; i++)
            for (int j = 1; j <= nrNoduri; j++)
                if (distante[i][j] > distante[i][k] + distante[k][j]) {
                    distante[i][j] = distante[i][k] + distante[k][j];
                }
    return distante;
}

int Graf::BFSmaxFlow(vector<vector<int>> &capacitati, int sursa, int destinatie, vector<int> &tati,
                     vector<vector<int>> &flux, vector<bool> &vizitat, vector<vector<int>> &grafRezidual) {
    tati.assign(capacitati.size(), 0);
    queue<int> coada;
    coada.push(sursa);

    tati[coada.back()] = -1;

    vizitat.clear();
    vizitat.resize(capacitati.size(), false);
    vizitat[sursa] = true;

    while (!coada.empty() && tati[destinatie] == 0) {
        int nodPlecare = coada.front();
        coada.pop();
        for (auto &i: grafRezidual[nodPlecare]) { // daca nodul nu a fost vizitat si muchia este nesaturata
            if (!vizitat[i] && capacitati[nodPlecare][i] > flux[nodPlecare][i]) {
                coada.push(i);
                tati[i] = nodPlecare;
                vizitat[i] = true;
            }
        }
    }
    return tati[destinatie];
}

int Graf::maxFlow(int sursa, int destinatie) {
    vector<vector<int>> capacitati(nrNoduri + 1, vector<int>(nrNoduri + 1, 0));
    vector<vector<int>> flux(nrNoduri + 1, vector<int>(nrNoduri + 1, 0));
    vector<int> tati(nrNoduri + 1, 0);
    vector<bool> vizitat(nrNoduri + 1, false);
    vector<vector<int>> grafRezidual(nrNoduri + 1);

    for (int i = 0; i < listaDeAdiacenta.size(); i++) {
        for (int j = 0; j < listaDeAdiacenta[i].size(); j++) {
            capacitati[i][listaDeAdiacenta[i][j].destinatie] = listaDeAdiacenta[i][j].capacitate;
            grafRezidual[i].push_back(listaDeAdiacenta[i][j].destinatie);
            grafRezidual[listaDeAdiacenta[i][j].destinatie].push_back(i);
        }
    }

    int fluxMaxim = 0;
    while (BFSmaxFlow(capacitati, sursa, destinatie, tati, flux, vizitat, grafRezidual)) {
        for (auto &i: grafRezidual[destinatie])
            if (vizitat[i] && flux[i][destinatie] < capacitati[i][destinatie]) {
                tati[destinatie] = i;
                int fluxNou = INFINIT;
                for (int j = destinatie; j != sursa; j = tati[j]) {
                    int x = tati[j];
                    fluxNou = min(fluxNou, capacitati[x][j] - flux[x][j]);
                }
                if (fluxNou > 0) { // urcam in arborele BFS pentru a actualiza capacitatiile
                    for (int j = destinatie; j != sursa; j = tati[j]) {
                        int x = tati[j];
                        flux[x][j] += fluxNou;
                        flux[j][x] -= fluxNou;
                    }
                    fluxMaxim += fluxNou;
                }
            }
    }
    return fluxMaxim;
}

vector<int> Graf::rezolvaBFS2(int nodPlecare) {
    vector<int> vizitat(nrNoduri + 1, -1);
    queue<int> coada;
    coada.push(nodPlecare);
    vizitat[coada.back()] = 1;
    BFSrecursiv(coada, vizitat);
    return vizitat;
}

int Graf::diametruArbore() {
    vector<int> distante = rezolvaBFS2(1);

    int diametruMaxim = -1, ultimulNod;
    for (int i = 1; i <= nrNoduri; i++)
        if (distante[i] > diametruMaxim) {
            diametruMaxim = distante[i];
            ultimulNod = i;
        }
    distante = rezolvaBFS2(ultimulNod);
    diametruMaxim = -1;
    for (int i = 1; i <= nrNoduri; i++)
        if (distante[i] > diametruMaxim)
            diametruMaxim = distante[i];
    return diametruMaxim;
}


// --- Functii apelate din main() pentru a rezolva fiecare problema -- //
void rezolvaBFS() {
    // Problema BFS (100p)
    // Link: https://infoarena.ro/problema/bfs
    // Sursa: https://infoarena.ro/job_detail/2797664?action=view-source
    int nrNoduri, nrMuchii, nodPlecare;
    fin >> nrNoduri >> nrMuchii >> nodPlecare;
    Graf g1(nrNoduri, nrMuchii, true, false, false, false);
    g1.citire();
    vector<int> distante = g1.numarMinimArce(nodPlecare);
    for (int i = 1; i < distante.size(); i++)
        fout << distante[i] << " ";
    fin.close();
    fout.close();
}

void rezolvaDFS() {
    // Problema DFS (100p)
    // Link: https://infoarena.ro/problema/dfs
    // Sursa: https://infoarena.ro/job_detail/2797669?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, false, false, false, false);
    g1.citire();
    int numarComponenteConexe = g1.numarComponenteConexe();
    fout << numarComponenteConexe;
    fin.close();
    fout.close();
}

void rezolvaComponenteBiconexe() {
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, false, false, false, false);
    g1.citire();
    vector<set<int>> componente = g1.componenteBiconexe();
    set<int>::iterator it;
    fout << componente.size() << "\n";
    for (auto &i: componente) {
        for (it = i.begin(); it != i.end(); it++) {
            fout << *it << " ";
        }
        fout << "\n";
    }
}

void rezolvaComponenteTareConexe() {
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, true, false, false, false);
    g1.citire();
    pair<int, vector<vector<int>>> rezultat = g1.componenteTareConexe();
    fout << rezultat.first << "\n";
    for (int i = 1; i <= rezultat.first; i++) {
        for (auto &j: rezultat.second[i])
            fout << j << " ";
        fout << "\n";
    }
    fin.close();
    fout.close();
}

void rezolvaHavelHakimi() {
    // Problema Havel Hakimi cu/fara Counting Sort pentru sortarea gradelor nodurilor
    int nrNoduri;
    fin >> nrNoduri;
    Graf g1(nrNoduri, 0, false, false, false, false);
    // ne intereseaza doar numarul de noduri
    bool sePoateConstruiGraful = g1.havelHakimi(true);
    if (sePoateConstruiGraful)
        fout << "DA";
    else
        fout << "NU";
    fin.close();
    fout.close();
}

void rezolvaSortareTopologica() {
    // Problema Sortare topologica (100p)
    // Link: https://infoarena.ro/problema/sortaret
    // Sursa: https://infoarena.ro/job_detail/2797552?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, true, false, false, false);
    g1.citire();
    stack<int> stiva = g1.sortareTopologica();
    while (!stiva.empty()) {
        fout << stiva.top() << " ";
        stiva.pop();
    }
    fin.close();
    fout.close();
}

void rezolvareMuchieCritica() {
    // Problema muchie critica (success)
    // Link: https://leetcode.com/problems/critical-connections-in-a-network/
    // Sursa: https://leetcode.com/submissions/detail/583031282/
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, false, false, false, false);
    g1.citire();
    vector<pair<int, int>> rez = g1.muchiiCritice();
    for (auto &i: rez)
        fout << i.first << " " << i.second << "\n";
    fin.close();
    fout.close();
}

void rezolvaGrafInfoarena() {
    // Problema Graf (100p)
    // Link: https://www.infoarena.ro/problema/graf
    // Sursa: https://www.infoarena.ro/job_detail/2800679?action=view-source
    int nrNoduri, nrMuchii, nodStart, nodEnd;
    fin >> nrNoduri >> nrMuchii >> nodStart >> nodEnd;
    Graf g1(nrNoduri, nrMuchii, false, false, false, false);
    g1.citire();
    vector<int> etichete = g1.grafInfoarena(nodStart, nodEnd);
    fout << etichete.size() << "\n";
    for (auto &i: etichete)
        fout << i << " ";

    fin.close();
    fout.close();
}

void rezolvaAPM() {
    // Problema apm (100p)
    // Link: https://infoarena.ro/problema/apm
    // Sursa: https://infoarena.ro/job_detail/2807085?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, false, true, false, true);
    g1.citire();
    pair<int, vector<pair<int, int>>> rez = g1.apm();
    nrMuchii = nrNoduri - 1; // stim ca un APM are intotdeuna nrNoduri - 1 muchii
    fout << rez.first << "\n" << nrMuchii << "\n";
    for (auto &i: rez.second)
        fout << i.first << " " << i.second << "\n";

    fin.close();
    fout.close();
}

void rezolvaDisjoint() {
    // Problema Paduri de multimi disjuncte (100p)
    // Link: https://infoarena.ro/problema/disjoint
    // Sursa: https://infoarena.ro/job_detail/2807108?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, false, true, false, true);
    g1.citire();
    g1.disjoint();

    fin.close();
    fout.close();
}

void rezolvaDijkstra() {
    // Problema Dijkstra (100p)
    // Link: https://infoarena.ro/problema/dijkstra
    // Sursa: https://infoarena.ro/job_detail/2807148?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, true, true, false, false);
    g1.citire();
    vector<int> distante = g1.dijkstra();
    for (int i = 2; i <= nrNoduri; i++)
        if (distante[i] != INFINIT)
            fout << distante[i] << " ";
        else
            fout << 0 << " "; // pun 0 ca asa cere infoarena. Normal se pune infinit

    fin.close();
    fout.close();
}

void rezolvaBellmanFord() {
    // Problema Bellman-Ford (100p)
    // Link: https://infoarena.ro/problema/bellmanford
    // Sursa: https://infoarena.ro/job_detail/2807149?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, true, true, false, false);
    g1.citire();
    vector<int> distante = g1.bellmanFord();
    if (distante.size())
        for (int i = 2; i <= nrNoduri; i++)
            fout << distante[i] << " ";
    else
        fout << "Ciclu negativ!";

    fin.close();
    fout.close();
}

void rezolvaRoyFloyd() {
    // Problema Roy-Floyd (100p)
    // Link: https://infoarena.ro/problema/royfloyd
    // Sursa: https://infoarena.ro/job_detail/2814187?action=view-source
    int n;
    fin >> n;
    vector<vector<int>> matriceCosturi;
    matriceCosturi.resize(n + 1);
    for (int i = 1; i <= n; i++) {
        matriceCosturi[i].resize(n + 1);
        for (int j = 1; j <= n; j++) {
            int cost;
            fin >> cost;
            matriceCosturi[i][j] = cost;
        }
    }
    Graf g1(n, 0, false, false, false, false);
    // ne intereseaza doar dimensiunea matricei
    vector<vector<int>> distante = g1.royFloyd(matriceCosturi);
    for (int i = 1; i < distante.size(); i++) {
        for (int j = 1; j < distante.size(); j++)
            if (i != j)
                fout << distante[i][j] << " ";
            else
                fout << 0 << " ";
        fout << "\n";
    }

    fin.close();
    fout.close();
}

void rezolvaMaxFlow() {
    // Problema MaxFlow (100p)
    // Link: https://infoarena.ro/problema/maxflow
    // Sursa: https://infoarena.ro/job_detail/2814451?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, nrMuchii, true, false, true, false);
    g1.citire();
    fout << g1.maxFlow(1, nrNoduri);

    fin.close();
    fout.close();
}

void rezolvaDarb() {
    // Problema Darb
    // Link: https://infoarena.ro/problema/darb
    // Sursa: https://infoarena.ro/job_detail/2814352?action=view-source
    int nrMuchii;
    fin >> nrMuchii;
    Graf g1(0, nrMuchii, false, false, false, false);
    g1.citire();
    fout << g1.diametruArbore();

    fin.close();
    fout.close();
}

int main() {
    int optiune = 14;
    // Meniu
    // Pentru tema 1: tastele 1-8
    // Pentru tema 2: tastele 9-12
    // Pentru tema 3: tastele 12-15
    switch (optiune) {
        case 1:
            rezolvaBFS();
            break;
        case 2:
            rezolvaDFS();
            break;
        case 3:
            rezolvaComponenteBiconexe();
            break;
        case 4:
            rezolvaComponenteTareConexe();
            break;
        case 5:
            rezolvaHavelHakimi();
            break;
        case 6:
            rezolvaSortareTopologica();
            break;
        case 7:
            rezolvareMuchieCritica();
            break;
        case 8:
            rezolvaGrafInfoarena();
            break;
        case 9:
            rezolvaAPM();
            break;
        case 10:
            rezolvaDisjoint();
            break;
        case 11:
            rezolvaDijkstra();
            break;
        case 12:
            rezolvaBellmanFord();
            break;
        case 13:
            rezolvaRoyFloyd();
            break;
        case 14:
            rezolvaMaxFlow();
            break;
        case 15:
            rezolvaDarb();
            break;
    }
    return 0;
}

void Graf::afiseazaListaDeAdiacenta() {
    for (int i = 1; i <= nrNoduri; i++) {
        fout << "-> Nodul " << i << " este adiacent cu:\n";
        for (auto &j: listaDeAdiacenta[i])
            fout << "nodul " << j.destinatie << ", cu costul " << j.cost << " si cu capacitatea " << j.capacitate
                 << "\n";
    }
}

void Graf::citire() {
    int sursa, destinatie, cost, capacitate;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> sursa >> destinatie;
        if (arePonderi)
            fin >> cost;
        else
            cost = 0;
        if (areCapacitati)
            fin >> capacitate;
        else
            capacitate = 0;
        listaDeAdiacenta[sursa].push_back(muchie(destinatie, cost, capacitate));
        if (trebuieListaDeMuchii)
            listaDeMuchii.push_back(muchie(sursa, destinatie, cost, capacitate));
        if (!eOrientat)
            listaDeAdiacenta[destinatie].push_back(muchie(sursa, cost, capacitate));
    }
}

Graf::Graf(const int nrNoduri, const int nrMuchii, const bool eOrientat, const bool arePonderi,
           const bool areCapacitati, const bool trebuieListaDeMuchii) {
    this->nrNoduri = nrNoduri;
    this->nrMuchii = nrMuchii;
    this->eOrientat = eOrientat;
    this->arePonderi = arePonderi;
    this->areCapacitati = areCapacitati;
    this->trebuieListaDeMuchii = trebuieListaDeMuchii;
    listaDeAdiacenta.resize(nrNoduri + 1, vector<muchie>());
}

Graf::~Graf() {
    listaDeAdiacenta.clear();
    listaDeMuchii.clear();
}