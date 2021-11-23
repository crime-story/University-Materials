// Popescu Paullo Robertto Karloss Grupa 2311
// Linkurile pentru fiecare problema se gasesc in main

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

class Graf {
private:
    int nrNoduri;
    bool eOrientat, arePonderi;
    vector<int> *adiacenta; // lista de vecini
    vector<pair<int, pair<int, int>>> muchii; // {cost, {sursa, destinatie}};

public:
    explicit Graf(int nrNoduri);

    Graf(int nrNoduri, const bool eOrientat, const bool arePonderi);

    void citire(const int nrMuchii);

    void rezolvaBFS(int &nodPlecare);

    void rezolvaDFS();

    void rezolvaBiconex();

    void rezolvaComponenteTareConexe(const int &nrMuchii);

    void havelHakimi(const bool sortareCountingSort);

    void rezolvaSortareTopologica();

    void rezolvaMuchieCritica();

    void rezolvaGrafInfoarena(int &nodStart, int &nodEnd);

    void rezolvaAPM(int &nrMuchii);

    void rezolvaDisjoint(int &nrMuchii);

    void rezolvaDijkstra(int &nrMuchii);

    void rezolvaBellmanFord(int &nrMuchii);

    ~Graf();

private:
    void adaugaMuchie(const int sursa, const int destinatie);

    void adaugaMuchiePonderat(const int sursa, const int destinatie, const int cost);

    void BFSrecursiv(queue<int> &coada, vector<int> &vizitat);

    void DFSrecursiv(int &nodPlecare, vector<int> &vizitat);

    void biconex(int nodPlecare, int precedent, int k, stack<pair<int, int>> &stivaComponenteBiconexe,
                 vector<set<int>> &componenteBiconexe, vector<int> &vizitat, vector<int> &niv_min);

    void DF1(int nodPlecare, vector<int> &succesor, vector<int> &v, int &index);

    void DF2(int nodPlecare, vector<int> &predecesor, int &nrComponenteTareConexe, vector<int> *adiacenta2);

    void DFSsortareTopologica(int nodPlecare, stack<int> &stiva, vector<int> &vizitat);

    void BFSgrafInfoarena(int nodPlecare, vector<int> &distanta);

    int reprezentant_tata(int nod, vector<int> &tata);

    void reuneste(int tataSursa, int tataDestinatie, vector<int> &tata, vector<int> &inaltime);

    void DFmuchieCritica(int nodPlecare, vector<int> &vizitat, vector<int> &niv_min, vector<int> &nivel);

};

void Graf::BFSrecursiv(queue<int> &coada, vector<int> &vizitat) {
    if (!coada.empty()) // daca mai sunt elemente in coada / nu am verificat pt toate nodurile
    {
        int nodPlecare = coada.front(); // retin nodul de unde plec
        for (auto &i: adiacenta[nodPlecare])
            if (vizitat[i] == -1) {
                // caut toate nodurile nevizitate care sunt adiacente cu nodul de plecare
                vizitat[i] = vizitat[nodPlecare] + 1; // il marcam vizitat
                coada.push(i); // il adaug in coada PUSH
            }
        coada.pop();
        BFSrecursiv(coada, vizitat);
    }
}

void Graf::rezolvaBFS(int &nodPlecare) {
    vector<int> vizitat(nrNoduri + 1, -1);
    queue<int> coada;
    coada.push(nodPlecare);
    vizitat[coada.back()] = 1;
    BFSrecursiv(coada, vizitat);
    for (int i = 1; i <= nrNoduri; i++) {
        if (vizitat[i] == -1)
            fout << -1 << " ";
        else
            fout << vizitat[i] - 1 << " ";
    }
}

void Graf::DFSrecursiv(int &nodPlecare, vector<int> &vizitat) {
    vizitat[nodPlecare] = 1;
    for (auto i: adiacenta[nodPlecare])
        if (!vizitat[i])
            DFSrecursiv(i, vizitat);
}

void Graf::rezolvaDFS() {
    vector<int> nivel(nrNoduri + 1, 0);
    vector<int> vizitat(nrNoduri + 1, 0);
    nivel[1] = 0;
    int numarComponenteConexe = 0;
    for (int i = 1; i <= nrNoduri; i++)
        if (vizitat[i] == 0) {
            numarComponenteConexe++;
            DFSrecursiv(i, vizitat);
        }
    fout << numarComponenteConexe;
}

void Graf::biconex(int nodPlecare, int precedent, int k, stack<pair<int, int>> &stivaComponenteBiconexe,
                   vector<set<int>> &componenteBiconexe, vector<int> &vizitat, vector<int> &niv_min) {
    vizitat[nodPlecare] = k;
    niv_min[nodPlecare] = k;
    for (auto &i: adiacenta[nodPlecare]) {
        int vecin = i;
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

void Graf::rezolvaBiconex() {
    stack<pair<int, int>> stivaComponenteBiconexe;
    vector<set<int>> componenteBiconexe;

    vector<int> vizitat(nrNoduri + 1, 0);
    vector<int> niv_min(nrNoduri + 1, 0);

    biconex(1, 0, 1, stivaComponenteBiconexe, componenteBiconexe, vizitat, niv_min);
    set<int>::iterator it;
    fout << componenteBiconexe.size() << "\n";
    for (auto &i: componenteBiconexe) {
        for (it = i.begin(); it != i.end(); it++) {
            fout << *it << " ";
        }
        fout << "\n";
    }
}

void Graf::DF1(int nodPlecare, vector<int> &succesor, vector<int> &v, int &index) {
    succesor[nodPlecare] = 1; // marchez nodul succesor nodului curent ca fiind vizitat
    for (auto &i: adiacenta[nodPlecare]) // parcurg toti vecinii nodului
        if (!succesor[i]) // daca succesorul nu a fost vizitat
            DF1(i, succesor, v, index); // continui parcurgerea
    v[++index] = nodPlecare; // retin succesorii intr-un array
}

void Graf::DF2(int nodPlecare, vector<int> &predecesor, int &nrComponenteTareConexe, vector<int> *adiacenta2) {
    predecesor[nodPlecare] = nrComponenteTareConexe; // marchez nodul predecesor nodului curent ca fiind vizitat
    for (auto &i: adiacenta2[nodPlecare])
        if (!predecesor[i])
            DF2(i, predecesor, nrComponenteTareConexe, adiacenta2);
}

// Am folosit algoritmul lui Kosaraju pentru a afla numarul de Componente Tare Conexe intr-un graf orientat
void Graf::rezolvaComponenteTareConexe(const int &nrMuchii) {
    adiacenta = new vector<int>[nrNoduri + 1];
    vector<int> adiacenta2[nrNoduri + 1]; // lista de vecini transpusa
    // (i.e. in loc de x si y folosim y si x ca la grafuri neorientate)
    int sursa, destinatie;
    for (int i = 1; i <= nrMuchii; i++) {
        fin >> sursa >> destinatie;
        adiacenta[sursa].push_back(destinatie); // retin lista succesorilor lui x
        adiacenta2[destinatie].push_back(sursa); // retin lista predecesorilor lui x
    }

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
            DF2(v[i], predecesor, nrComponenteTareConexe, adiacenta2); // parcurg in adancime marcand predecesorii
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

void Graf::havelHakimi(const bool sortareCountingSort) {
    int gradCurent, sumaGrade = 0;
    vector<int> gradeNoduri;
    bool ok = true; // presupun ca suma gradelor este para si ca gradul oricarui nod nu este >= decat nrNoduri sau negativ
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
            if (sortareCountingSort)
                countingSort(gradeNoduri);
            else
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

void Graf::DFSsortareTopologica(int nodPlecare, stack<int> &stiva, vector<int> &vizitat) {
    vizitat[nodPlecare] = 1;
    for (auto i: adiacenta[nodPlecare])
        if (!vizitat[i])
            DFSsortareTopologica(i, stiva, vizitat);
    stiva.push(nodPlecare);
}

void Graf::rezolvaSortareTopologica() {
    vector<int> vizitat(nrNoduri + 1, 0);
    stack<int> stiva;
    for (int i = 1; i <= nrNoduri; ++i)
        if (!vizitat[i])
            DFSsortareTopologica(i, stiva, vizitat);
    while (!stiva.empty()) {
        fout << stiva.top() << " ";
        stiva.pop();
    }
}

void Graf::rezolvaMuchieCritica() {
    vector<int> vizitat(nrNoduri + 1, 0);
    vector<int> niv_min(nrNoduri + 1, 0);
    vector<int> nivel(nrNoduri + 1, 0);
    vizitat[1] = 0;
    DFmuchieCritica(1, vizitat, niv_min, nivel);
}

void Graf::DFmuchieCritica(int nodPlecare, vector<int> &vizitat, vector<int> &niv_min, vector<int> &nivel){
    vizitat[nodPlecare] = 1;
    niv_min[nodPlecare] = nivel[nodPlecare]; // initializez nivelul minim cu nivelul nodului, nivel[1] = 1 la inceput
    for (auto &i: adiacenta[nodPlecare])
        if (!vizitat[i]) {
            nivel[i] = nivel[nodPlecare] + 1; // actualizez nivelul nodului in care am ajuns
            DFmuchieCritica(i, vizitat, niv_min, nivel); // reapelez DF din nodul in care am ajuns
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

void Graf::BFSgrafInfoarena(int nodPlecare, vector<int> &distanta) {
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

void Graf::rezolvaGrafInfoarena(int &nodStart, int &nodEnd) {
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
    fout << etichete.size() << "\n";
    for (auto i: etichete)
        fout << i << " ";
}

int Graf::reprezentant_tata(int nod, vector<int> &tata) { // caut radacina nodului curent in subarbore
    while (tata[nod] != nod)
        nod = tata[nod];
    return nod;
}

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
void Graf::rezolvaAPM(int &nrMuchii) {
    vector<pair<int, int>> apm;
    vector<int> tata(nrNoduri + 1);
    vector<int> inaltime(nrNoduri + 1);
    int costAPM = 0;
    sort(muchii.begin() + 1, muchii.end()); // sortez crescator muchiile dupa cost
    for (int i = 1; i <= nrNoduri; i++) { // initializare complexitate O(n)
        tata[i] = i;
        inaltime[i] = 0;
    }
    for (int i = 1; i <= nrMuchii; i++) {
        int tataSursa = reprezentant_tata(muchii[i].second.first, tata); // caut reprezentantul nodului sursa curent
        int tataDestinatie = reprezentant_tata(muchii[i].second.second,
                                               tata); // caut reprezentantantul nodului destinatie curent
        if (tataSursa != tataDestinatie) {
            // daca nu am acelasi tata inseamna ca pot sa reunesc
            reuneste(tataSursa, tataDestinatie, tata, inaltime);
            apm.push_back({muchii[i].second.first, muchii[i].second.second});
            costAPM += muchii[i].first;
        }
    }
    nrMuchii = nrNoduri - 1; // stim ca un APM are intotdeuna nrNoduri - 1 muchii
    fout << costAPM << "\n" << nrMuchii << "\n";
    for (auto &i: apm)
        fout << i.first << " " << i.second << "\n";
}

void Graf::rezolvaDisjoint(int &nrMuchii) {
    // in citire avem asa:
    // muchie[i] = {distanta, {tipOperatie, sursa}};

    // Deci in cazul asta:
    // muchie[i].first = distanta
    // muchie[i].second.first = tipul Operatiei
    // muchie[i].second.second = sursa

    vector<int> tata(nrNoduri + 1);
    vector<int> inaltime(nrNoduri + 1, 0);
    for (int i = 1; i <= nrNoduri; i++) // initializare complexitate O(n)
        tata[i] = i;
    for (int i = 1; i <= nrMuchii; i++) {
        int tataSursa = reprezentant_tata(muchii[i].second.second, tata);
        // caut reprezentantul nodului sursa curent
        int tataDestinatie = reprezentant_tata(muchii[i].first, tata);
        // caut reprezentantantul nodului destinatie curent
        if (muchii[i].second.first == 1)
            reuneste(tataSursa, tataDestinatie, tata, inaltime);
        else if (tataSursa == tataDestinatie)
            // daca am acelasi tata inseamna ca fac parte din aceeasi multime
            fout << "DA\n";
        else
            fout << "NU\n";
    }
}

void Graf::rezolvaDijkstra(int &nrMuchii) {
    vector<vector<pair<int, int>>> adiacentaCost(nrNoduri + 1, vector<pair<int, int>>(1, {-1, -1}));
    for (int i = 1; i <= nrMuchii; ++i) {
        adiacentaCost[muchii[i].second.first].push_back(make_pair(muchii[i].second.second, muchii[i].first));
    }
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap; // {cost, nodDestinatie}
    vector<int> distanta(nrNoduri + 1, INFINIT); // initial toate distantele sunt infinit
    vector<int> vizitat(nrNoduri + 1, 0); // toate nodurile nu sunt vizitate
    minHeap.push(make_pair(0, 1)); // introduc in minHeap prima data costul de la nodStart, care este 0 si nodulStart
    distanta[1] = 0; // distanta pana la nodulStart este 0

    while (!minHeap.empty()) {
        int nod = minHeap.top().second; // iau nodul cel mai apropiat din minHeap
        minHeap.pop(); // il scot din minHeap
        if (!vizitat[nod]) { // daca nu am verificat inca nodul
            vizitat[nod] = 1; // il marchez ca fiind verificat
            for (int i = 1; i < adiacentaCost[nod].size(); i++) // iau toate nodurile adiacente cu el
                if (distanta[nod] + adiacentaCost[nod][i].second < distanta[adiacentaCost[nod][i].first]) {
                    // daca distanta pana la nodul in care sunt + costul pana la nodul adiacent cu el
                    // este mai mic decat distanta pana nodul adiacent cu el
                    // atunci actualizez distanta ca fiind:
                    // distanta pana nodul meu + costul pana nodul adiacent cu el
                    distanta[adiacentaCost[nod][i].first] = distanta[nod] + adiacentaCost[nod][i].second;
                    minHeap.push(make_pair(distanta[adiacentaCost[nod][i].first], adiacentaCost[nod][i].first));
                    // introduc in minHeap distanta actualizata pana la nodul adiacent cu nodul meu,
                    // si nodul adiacent cu mine
                }
        }
    }
    for (int i = 2; i <= nrNoduri; i++)
        if (distanta[i] != INFINIT)
            fout << distanta[i] << " ";
        else
            fout << 0 << " "; // pun 0 ca asa cere infoarena. Normal se pune infinit
}

void Graf::rezolvaBellmanFord(int &nrMuchii) {
    vector<vector<pair<int, int>>> adiacentaCost(nrNoduri + 1, vector<pair<int, int>>(1, {-1, -1}));
    for (int i = 1; i <= nrMuchii; i++) {
        adiacentaCost[muchii[i].second.first].push_back(make_pair(muchii[i].second.second, muchii[i].first));
    }
    vector<int> distanta(nrNoduri + 1, INFINIT);
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
    distanta[1] = 0; // distanta pana la nodulStart este 0

    while (!coada2.empty() && !cicluNegativ) {
        int nod = coada2.front();
        coada2.pop();
        apartCoada[nod] = 0;

        for (int i = 1; i < adiacentaCost[nod].size(); i++)
            if (distanta[nod] + adiacentaCost[nod][i].second < distanta[adiacentaCost[nod][i].first]) {
                distanta[adiacentaCost[nod][i].first] = distanta[nod] + adiacentaCost[nod][i].second;
                vizitat[adiacentaCost[nod][i].first]++;

                if (vizitat[adiacentaCost[nod][i].first] >= nrNoduri) {
                    // daca am vizitat un nod adiacent cu mine de mai multe ori decat numarul de noduri
                    // inseamna ca avem un ciclu negativ
                    cicluNegativ = true;
                    break;
                }

                if (!apartCoada[adiacentaCost[nod][i].first]) {
                    coada2.push(adiacentaCost[nod][i].first);
                    apartCoada[adiacentaCost[nod][i].first] = 1;
                }
            }
    }
    if (cicluNegativ) {
        fout << "Ciclu negativ!";
    } else {
        for (int i = 2; i <= nrNoduri; i++)
            fout << distanta[i] << " ";
    }

}

int main() {
    // Tema 1:
    /*
    // Problema BFS (100p)
    // Link: https://infoarena.ro/problema/bfs
    // Sursa: https://infoarena.ro/job_detail/2797664?action=view-source
    int nrNoduri, nrMuchii, nodPlecare;
    fin >> nrNoduri >> nrMuchii >> nodPlecare;
    Graf g1(nrNoduri, true, false);
    g1.citire(nrMuchii);
    g1.rezolvaBFS(nodPlecare);
    */

    /*
    // Problema DFS (100p)
    // Link: https://infoarena.ro/problema/dfs
    // Sursa: https://infoarena.ro/job_detail/2797669?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, false, false);
    g1.citire(nrMuchii);
    g1.rezolvaDFS();
    */

    /*
    // Problema Componente Biconexe (100p)
    // Link: https://infoarena.ro/problema/biconex
    // Sursa: https://infoarena.ro/job_detail/2797675?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, false, false);
    g1.citire(nrMuchii);
    g1.rezolvaBiconex();
    */

    /*
    // Problema CTC (Componente Tare Conexe) (100p)
    // Link: https://infoarena.ro/problema/ctc
    // Sursa: https://infoarena.ro/job_detail/2797676?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri);
    g1.rezolvaComponenteTareConexe(nrMuchii);
    */

    /*
    // Problema Havel Hakimi cu/fara Counting Sort pentru sortarea gradelor nodurilor
    int nrNoduri;
    fin >> nrNoduri;
    Graf g1(nrNoduri);
    g1.havelHakimi(true);
    */

    /*
    // Problema Sortare topologica (100p)
    // Link: https://infoarena.ro/problema/sortaret
    // Sursa: https://infoarena.ro/job_detail/2797552?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, true, false);
    g1.citire(nrMuchii);
    g1.rezolvaSortareTopologica();
    */

    /*
    // Problema muchie critica (success)
    // Link: https://leetcode.com/problems/critical-connections-in-a-network/
    // Sursa: https://leetcode.com/submissions/detail/583031282/
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, false, false);
    g1.citire(nrMuchii);
    g1.rezolvaMuchieCritica();
    */

    /*
    // Problema Graf (100p)
    // Link: https://www.infoarena.ro/problema/graf
    // Sursa: https://www.infoarena.ro/job_detail/2800679?action=view-source
    int nrNoduri, nrMuchii, nodStart, nodEnd;
    fin >> nrNoduri >> nrMuchii >> nodStart >> nodEnd;
    Graf g1(nrNoduri, false, false);
    g1.citire(nrMuchii);
    g1.rezolvaGrafInfoarena(nodStart, nodEnd);
    */

    // Tema 2:
    /*
    // Problema apm (100p)
    // Link: https://infoarena.ro/problema/apm
    // Sursa: https://infoarena.ro/job_detail/2807085?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, false, true);
    g1.citire(nrMuchii);
    g1.rezolvaAPM(nrMuchii);
    */

    /*
    // Problema Paduri de multimi disjuncte (100p)
    // Link: https://infoarena.ro/problema/disjoint
    // Sursa: https://infoarena.ro/job_detail/2807108?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, false, true);
    g1.citire(nrMuchii);
    g1.rezolvaDisjoint(nrMuchii);
    */

    /*
    // Problema Dijkstra (100p)
    // Link: https://infoarena.ro/problema/dijkstra
    // Sursa: https://infoarena.ro/job_detail/2807148?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, true, true);
    g1.citire(nrMuchii);
    g1.rezolvaDijkstra(nrMuchii);
    */

    /*
    // Problema Bellman-Ford (100p)
    // Link: https://infoarena.ro/problema/bellmanford
    // Sursa: https://infoarena.ro/job_detail/2807149?action=view-source
    int nrNoduri, nrMuchii;
    fin >> nrNoduri >> nrMuchii;
    Graf g1(nrNoduri, true, true);
    g1.citire(nrMuchii);
    g1.rezolvaBellmanFord(nrMuchii);
    */

    fin.close();
    fout.close();
    return 0;
}

Graf::Graf(const int nrNoduri) {
    this->nrNoduri = nrNoduri;
}

Graf::Graf(const int nrNoduri, const bool eOrientat, const bool arePonderi) {
    this->nrNoduri = nrNoduri;
    this->eOrientat = eOrientat;
    this->arePonderi = arePonderi;
}

void Graf::adaugaMuchie(const int sursa, const int destinatie) {
    adiacenta[sursa].push_back(destinatie);
}

void Graf::adaugaMuchiePonderat(const int sursa, const int destinatie, const int cost) {
//    adiacentaCost[sursa].push_back(muchieCost(destinatie, cost));
}

void Graf::citire(const int nrMuchii) {
    int sursa, destinatie; // extremitate muchie stanga respectiv dreapta
    adiacenta = new vector<int>[nrNoduri + 1];
    if (!arePonderi) {
        if (eOrientat)
            for (int i = 1; i <= nrMuchii; i++) {
                fin >> sursa >> destinatie;
                adaugaMuchie(sursa, destinatie);
            }
        else
            for (int i = 1; i <= nrMuchii; i++) {
                fin >> sursa >> destinatie;
                adaugaMuchie(sursa, destinatie);
                adaugaMuchie(destinatie, sursa);
            }
    } else {
        int cost; // costul muchiei
        muchii.resize(nrMuchii + 1);
        if (eOrientat)
            for (int i = 1; i <= nrMuchii; i++) {
                fin >> sursa >> destinatie >> cost;
                muchii[i] = {cost, {sursa, destinatie}};
            }
        else
            for (int i = 1; i <= nrMuchii; i++) {
                fin >> sursa >> destinatie >> cost;
                muchii[i] = {cost, {sursa, destinatie}};
            }
    }
}

Graf::~Graf() {
    delete[] adiacenta;
}