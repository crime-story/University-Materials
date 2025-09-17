#include <fstream>
#include <climits>
#include <cstdlib>
#include <time.h>
using namespace std;
ifstream fin("abce.in");
ofstream fout("abce.out");
class Nod {
    Nod *fiuStang;
    Nod *fiuDrept;
    int val; // retine numarul pe care il introducem noi in multime
    const int priority; // pentru rechilibrare utilizand rotiri (stanga/dreapta)
public:
    int getVal() const; // pentru poo: semnaleaza compilatorului sa nu compileze daca acea functie ar modifica vreauna din datele membre
    void setVal(int val);
    int getPriority() const;
    ~Nod();
    explicit Nod(const int &val); // explicit blocheaza conversia implicita, adica nu accepta short, long long etc
    Nod(const Nod &) = delete; // blocheaza constructorul de copiere
    // regula celor 3
    // daca supraincarc constructorul de initializare trebuie sa supraincarc si pe cel de copiere si destructorul
    // dar intrucat nu am nevoie sa le supraincarc le marchez cu delete
    // daca nu as face asta compilatorul ar genera un constructor de copiere implicit
    // daca nu puneam referinta se creea o noua variabila care ocupa mai multe memorie
    // daca aveam un vector de 10.000 de elemente era o problema !
    Nod *getFiuStang() const; // returneaza un pointer de tip nod, returneaza o valoare in adresa de memorie
    // ar fi compilat si cu void*, dar nu as mai fi putut folosi geteri si seteri
    // void nu returneaza nimic
    // void* este un pointer si returneaza o adresa de memorie !
    void setFiuStang(Nod *fiuStang);
    Nod *getFiuDrept() const;
    void setFiuDrept(Nod *fiuDrept);
};
void Nod::setFiuDrept(Nod *fiuDrept) {
    Nod::fiuDrept = fiuDrept;
}
Nod *Nod::getFiuStang() const {
    return fiuStang;
}

void Nod::setFiuStang(Nod *fiuStang) {
    Nod::fiuStang = fiuStang;
}

Nod *Nod::getFiuDrept() const {
    return fiuDrept;
}
Nod::~Nod() {}
Nod::Nod(const int &val) : val(val), priority(rand()) {
    // apelez constructorul de initializare pentru cele 2 variabile, de aceea folosesc lista de initializare
    // pentru doua variabile de tip int nu prea conteaza
    // dar daca as fi avut de exemplu un vector din stl ca data membra atunci am fi dorit sa apelam constructorul lui de copiere
    // pentru a nu copia de mana toate cele 10.000 de elemente
    fiuDrept = fiuStang = nullptr;
}
int Nod::getVal() const {
    return val;
}

void Nod::setVal(int val) {
    Nod::val = val; // poo: daca puneam ca parametru val1 in loc de val nu mai era necesar
}// operatorul de rezolutie de scop Nod::val = val;
int Nod::getPriority() const {
    return priority;
}

class Treap {
    Nod *radacina; // nodul radacina
    // intotdeauna pornesc cu parcurgeri din radacina cand inserez, sterg, caut etc
    void balance(Nod *nodBunic, Nod *nodTata) { // balance e apelat de inserare
        if (nodTata->getFiuStang() != nullptr && nodTata->getFiuStang()->getPriority() > nodTata->getPriority())
            // daca exista fiu stang care are o prioritate mai mare ca mine
            // nodTata->getPriority() e prioritatea nodului curent
            rotDreapta(nodBunic, nodTata, nodTata->getFiuStang()); // ne rotim la dreapta
        else if (nodTata->getFiuDrept() != nullptr && nodTata->getFiuDrept()->getPriority() > nodTata->getPriority())
            rotStanga(nodBunic, nodTata, nodTata->getFiuDrept());
    }
    void inserare(Nod *nodDeInserat, Nod *nodTata, Nod *nodBunic) { // e apelata doar cand exista cel putin un nod
        if (nodDeInserat->getVal() == nodTata->getVal())//daca am duplicat, nu il mai inserez
            return;
        else if (nodDeInserat->getVal() < nodTata->getVal()) {///daca nodul meu e mai mic, il inseram in stanga
            if (nodTata->getFiuStang() != nullptr)///daca mai copii, cobor
                inserare(nodDeInserat, nodTata->getFiuStang(), nodTata);
            else//altfel, il leg
                nodTata->setFiuStang(nodDeInserat);
        } else {//altfel il inseram in dreapta
            if (nodTata->getFiuDrept() != nullptr)///daca mai am copiii cobor
                inserare(nodDeInserat, nodTata->getFiuDrept(), nodTata);
            else//altfel il leg
                nodTata->setFiuDrept(nodDeInserat);
        }//rebalansez in fct de prioritati ca sa respecte proprietatea de heap
        balance(nodBunic, nodTata); // daca inserarea se face de sus in jos, balansarea se va face de jos in sus, fiind apelata la fiecare nivel pana inapoi in radacina (de h ori)
    }
    Nod *cautare(const int nrCautat, Nod *nodCurent) {
        // are complexitate O(h)
        // pt ca merg ori la stanga ori la dreapta
        if (nodCurent == nullptr || nodCurent->getVal() == nrCautat)
            // daca am iesit in afara arborelui sau am gasit valoare cautata
            // returnez nodul curent ori null or nodul valoare
            return nodCurent;
        else if (nrCautat < nodCurent->getVal())
            // compar cu valoarea
            return cautare(nrCautat, nodCurent->getFiuStang()); // daca este mai mica ma duc la stanga
            // recursia se termina la return nodCurent
        else
            return cautare(nrCautat, nodCurent->getFiuDrept()); // daca este mai mare ma duc la dreapta
    }
    void rotDreapta(Nod *nodBunic, Nod *nodTata, Nod *nodCurent) {
        // Rotirile presupun 3 miscari bunicul sa-si recunoasca nodul curent drept fiu
        // apoi nodul tata sa-si recunoasca nepotul si apoi nodul curent sa-si recunoasca tatal drept fiu
        // in else if se face prima miscare
        // ultimele doua miscari in ultimele doua linii din functie
        // la inceput e caz particular daca tatal e radacina
        if (nodTata == radacina) // daca eu sunt radacina
            radacina = nodCurent; // atunci radacina este nodul curent, adica fiul
        else if (nodBunic != nullptr) { // daca nodul bunic exista, il va recunoaste drept fiu pe nod curent
            if (nodBunic->getFiuStang() == nodTata) // daca nodul tata era fiu stang al nodului bunic
                nodBunic->setFiuStang(nodCurent); // nodul bunic va pointa in stanga la nodul curent
            else
                nodBunic->setFiuDrept(nodCurent); // altfel va pointa in dreapta
        }
        // se refac legaturile fiul luand locul tatalui
        nodTata->setFiuStang(nodCurent->getFiuDrept()); // tatal isi va recunoaste nepotii drept fii
        nodCurent->setFiuDrept(nodTata); // nodul curent isi va recunoaste tatal drept fiu
    }
    void rotStanga(Nod *nodBunic, Nod *nodTata, Nod *nodCurent) {
        if (nodTata == radacina)
            radacina = nodCurent;
        else if (nodBunic != nullptr) {
            if (nodBunic->getFiuStang() == nodTata)
                nodBunic->setFiuStang(nodCurent);
            else
                nodBunic->setFiuDrept(nodCurent);
        }
        nodTata->setFiuDrept(nodCurent->getFiuStang()); // tatal isi va recunoaste nepotii drept fii
        nodCurent->setFiuStang(nodTata); // nodul curent isi va recunoaste tatal drept fiu
    }
    void sRD(std::ofstream &fout, const int st, const int dr, Nod *nodCurent) { // parcurgerea
        if (nodCurent != nullptr) { // nodul la care sunt != null, adica nu am iesit in afara arborelui, eu ma duc in stanga si in dreapta pana cand ies
            sRD(fout, st, dr, nodCurent->getFiuStang()); // ma duc in stanga
            if (nodCurent->getVal() >= st && nodCurent->getVal() <= dr) // ma asigur ca afisez doar nodurile din intervalul dorit
                fout << nodCurent->getVal() << ' '; // afisez nodul curent
            sRD(fout, st, dr, nodCurent->getFiuDrept()); // merg in dreapta
        }
    }
public:
    Treap() {
        radacina = nullptr;
    }
    void inserare(const int &val) {
        Nod *nodNou = new Nod(val);
        if (radacina == nullptr) // daca nu exista niciun nod
            radacina = nodNou; // pointez catre noul nod
        else
            inserare(nodNou, radacina, nullptr);
        // e nullptr la bunic pentru ca incep de la radacina care nu are niciun tata
    }

    int cautare(const int nrCautat) {
        Nod *nod = cautare(nrCautat, radacina); // apelez functia de cautare care imi va returna nodul ce contine valoarea cautata sau nullptr daca nu exista acea valoare in multime
        if (nod == nullptr) // daca nu exista afisam 0
            return 0;
        else // altfel afisam 1
            return 1;
    }

    void stergere(const int val) {
        // sunt 3 cazuri
        Nod *nodCurent = radacina;
        ///gasim nodul si tatal lui
        Nod *nodTata = nullptr;
        while (nodCurent != nullptr && nodCurent->getVal() != val) { // gasesc nodul care are valoarea cautata de mine sau nullptr daca nu exista in multime
            nodTata = nodCurent; // retinem si care era tatal nodului cautat
            // acelasi cod ca la cautare doar ca iterativ (nu recursiv)
            if (val < nodCurent->getVal())
                nodCurent = nodCurent->getFiuStang();
            else
                nodCurent = nodCurent->getFiuDrept();
        }
        if (nodCurent != nullptr) { // daca am gasit nodul care trebuie sters
            ///cazul 1-nu are niciun fiu
            if (nodCurent->getFiuStang() == nullptr && nodCurent->getFiuDrept() == nullptr) { // se sterge direct nodul
                if (nodCurent == radacina) // caz particular cand nodul sters este chiar radacina
                    radacina = nullptr;
                else if (nodTata->getFiuStang() == nodCurent) // altfel spun tatalui sa pointeze la null
                    nodTata->setFiuStang(nullptr);
                else
                    nodTata->setFiuDrept(nullptr);
            } else if (nodCurent->getFiuStang() != nullptr && nodCurent->getFiuDrept() == nullptr) {
                // sterg nodul curent si apoi leg tatal de fiul meu
                ///cazul 2A - doar fiu stang
                if (nodCurent == radacina)
                    radacina = nodCurent->getFiuStang();
                else if (nodTata->getFiuStang() == nodCurent)
                    nodTata->setFiuStang(nodCurent->getFiuStang());
                else
                    nodTata->setFiuDrept(nodCurent->getFiuStang());
            } else if (nodCurent->getFiuStang() == nullptr && nodCurent->getFiuDrept() != nullptr) {
                ///cazul 2B - doar fiu drept
                if (nodCurent == radacina)
                    radacina = nodCurent->getFiuDrept();
                else if (nodTata->getFiuStang() == nodCurent)
                    nodTata->setFiuStang(nodCurent->getFiuDrept());
                else
                    nodTata->setFiuDrept(nodCurent->getFiuDrept());
            } else if (nodCurent->getFiuStang() != nullptr && nodCurent->getFiuDrept() != nullptr) { // iau succesorul
                ///cazul 3 - are ambii fii
                Nod *nodAux = nodCurent->getFiuDrept(); // ma duc o data la dreapta
                Nod *tataNodAux = nodCurent; // retine tatal
                while (nodAux->getFiuStang() != nullptr) {
                    // ma duc la stanga cat timp pot
                    tataNodAux = nodAux; // retin tatal
                    nodAux = nodAux->getFiuStang(); // si ma duc la stanga pt ca inca pot
                }
                nodCurent->setVal(nodAux->getVal());///mut succesorul pe pozitia nodului pe care il sterg,
                /// pastrand insa prioritatea nodului sters
                // am folosit setVal nu pastrez valoarea, dar pastrez prioritatea
                // de exemplu daca voiam sa il sterg pe 2 (avea prioritatea 5)
                // si vine 7 in locul lui dupa stergere, 7 va avea prioritatea 5 pe care o avea 2
                // urmeaza doua cazuri diferite
                if (tataNodAux != nodCurent) // primul if este daca fiul drept al nodului curent pe care voiam sa il sterg avea fiu stang
                    tataNodAux->setFiuStang(nodAux->getFiuDrept()); // inseamna ca dupa ce m-am dus in dreapta m-am tot dus la stanga
                else
                    tataNodAux->setFiuDrept(nodAux->getFiuDrept()); // inseamna ca sunt fiul drept direct al nodului curent
                nodCurent = nodAux; // marchez nodul in care se afla succesorul pentru stergere pentru ca dupa voi face delete de nodCurent
            }
            delete nodCurent;
        }
    }

    int predecesor(const int &val) {
        // predecesorul este mai mic sau egal
        // se porneste din radacina
        Nod *nodAux = radacina;
        int maxi = INT_MIN; // am initializat cu ce a fost posibil pe int, valoarea minima pentru int
        while (nodAux != nullptr) { // cat timp nu am iesit din arbore
            if (nodAux->getVal() > maxi && nodAux->getVal() <= val) // daca am gasit un numar mai mare care era mai <= val
                maxi = nodAux->getVal(); // il retin
            if (nodAux->getVal() > val) // daca nodul curent are valoarea mai mare decat val care era numarul al carui predecesor voiam sa il gasim
                nodAux = nodAux->getFiuStang(); // ma duc la stanga, pt ca la stanga este mai mic
            else if (nodAux->getVal() == val) // daca valoarea curente este egala cu val ma opresc pentru ca inseamna ca voi returna val
                break;
            else // nodul curent e mai mic de val, deci toate nodurile din stanga vor fi mai mici de val
                nodAux = nodAux->getFiuDrept(); // deci merge in dreapta
        }
        return maxi;
    }

    int succesor(const int &val) {
        Nod *nodAux = radacina;
        int mini = INT_MAX;
        while (nodAux != nullptr) {
            if (nodAux->getVal() < mini && nodAux->getVal() >= val)
                mini = nodAux->getVal();
            if (nodAux->getVal() < val)
                nodAux = nodAux->getFiuDrept();
            else if (nodAux->getVal() == val)
                break;
            else
                nodAux = nodAux->getFiuStang();
        }
        return mini;
    }

    void sRD(std::ofstream &fout, const int st, const int dr) { // apelez functia de afisare a intervalului care O(n) pentru ca merge si stanga si dreapta
        sRD(fout, st, dr, radacina);
    }
    void clearTreap(Nod *nod) { // dezaloca memoria folosita fiind o parcurgere stanga dreapta radacina SDR (postordine)
        if (nod != nullptr) {
            clearTreap(nod->getFiuStang());
            clearTreap(nod->getFiuDrept());
            delete nod;
        }
    }
    ~Treap() {
        clearTreap(radacina);
    }
};

int main() {
    srand(time(NULL));
    Treap t;
    int n;
    fin >> n;
    for (int i = 0; i < n; i++) {
        int a, b;
        fin >> a >> b;
        if (a == 1) {
            t.inserare(b);
        } else if (a == 2) {
            t.stergere(b);
        } else if (a == 3) {
            fout << t.cautare(b) << '\n';
        } else if (a == 4) {
            fout << t.predecesor(b) << '\n';
        } else if (a == 5) {
            fout << t.succesor(b) << '\n';
        } else if (a == 6) {
            int c;
            fin >> c;
            t.sRD(fout, b, c);
            fout << '\n';
        }
    }
    return 0;
}