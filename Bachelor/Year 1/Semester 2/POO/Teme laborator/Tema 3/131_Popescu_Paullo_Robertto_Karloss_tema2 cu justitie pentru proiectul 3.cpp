#include <iostream>
#include <cstdio>
#include <cstdlib>
#include<list>
#include <vector>

using namespace std;

list<float> l;

const int k = 30;

class proces {
protected:
    int nrProces;
    string reclamant;
    string reclamat;
    static int co;

public:
    proces(); // constructor de initializare fara parametrii implicit
    proces(string, string, int); // constructor de initializare cu parametrii
    proces(const proces &); // constructor de copiere
    virtual ~proces(); // destructor

    /*proces(int x, string y, string z)
    { // AICI asa ar fi aratat daca am fi vrut ca constructorul parametrizat sa fie membru al clasei
        nrProces = x;
        reclamant = y;
        reclamat = z;
    }*/

    proces &operator=(proces &);

    friend istream &operator>>(istream &is, proces &proces); // supraincarcare operator citire " >> " clasa proces
    friend ostream &operator<<(ostream &os, proces &proces); // supraincarcare operator afisare " << " clasa proces
    virtual void citire(); // functie de citire
    virtual void afisare(); // functie afisare

    static int getAutoIncrement();
};

int proces::co = 0;

proces::proces() {}

proces::proces(string reclamant, string reclamat, int nrProces = ++co) {
    this->nrProces = nrProces;
    this->reclamant = reclamant;
    this->reclamat = reclamat;
}

proces::proces(const proces &ob) {
    nrProces = ob.nrProces;
    reclamant = ob.reclamant;
    reclamant = ob.reclamat;
}

proces::~proces() {}

proces &proces::operator=(proces &ob) {
    if (this != &ob) {
        nrProces = ob.nrProces;
        reclamant = ob.reclamant;
        reclamat = ob.reclamat;
    }
    return *this;
}

istream &operator>>(istream &is, proces &proces) {
    cout << "Introduceti numarul procesului sau -1 pt increment automat: ";
    is >> proces.nrProces;
    if (proces.nrProces == -1)
        proces.nrProces = proces.getAutoIncrement();
    cout << "Introduceti numele reclamantului: ";
    is >> proces.reclamant;
    cout << "Introduceti numele reclamatului: ";
    is >> proces.reclamat;
    return is;
}

ostream &operator<<(ostream &os, proces &proces) {
    os << "Numarul procesului este " << proces.nrProces << endl;
    os << "Numele reclamantului este " << proces.reclamant << endl;
    os << "Numele reclamatului este " << proces.reclamat << endl;
    return os;
}

void proces::citire() {
    cout << "Introduceti numarul procesului: ";
    cin >> nrProces;
    cout << "Introduceti numele reclamantului: ";
    cin >> reclamant;
    cout << "Introduceti numele reclamatului: ";
    cin >> reclamat;
}

void proces::afisare() {
    cout << "Numarul procesului este " << nrProces << endl;
    cout << "Numele reclamantului este " << reclamant << endl;
    cout << "Numele reclamatului este " << reclamat << endl;
}

int proces::getAutoIncrement() {
    return ++co;
}

class proces_civil : public proces {
protected:
    double dauneMorale, dauneMateriale, taxa_timbru;
    int nrMartori;
    bool stadiu;
public:
    proces_civil(); // constructor de intializare fara parametrii
    proces_civil(string, string, double, double, double, int, int); // constructor de initializare cu parametrii
    proces_civil(const proces_civil &); // constructor de copiere
    ~proces_civil(); // destructor
    proces_civil &operator=(proces_civil &);

    /*proces_civil(int x, string y, string z, double a, double b, int c):proces(x,y,z)
    {
        dauneMorale = a;
        dauneMateriale = b;
        nrMartori = c;
        if (nrMartori > 5)
            stadiu = 1;
        else
            stadiu = 0;
        taxa_timbru = 0.1*(dauneMorale + dauneMateriale);
    }*/
    friend istream &
    operator>>(istream &is, proces_civil &proces_civil); // supraincarcare operator citire " >> " clasa proces_civil
    friend ostream &
    operator<<(ostream &os, proces_civil &proces_civil); // supraincarcare operator afisare " << " clasa proces_civil
    proces_civil &modific_stadiu_proces_civil(int martori, int nr, proces_civil &p);

    void proces_civil_timbru(proces_civil c) const;

    void proces_civil_stocare_timbru(proces_civil c);

    void marire_civil_timbru(proces_civil &c);

    void proces_civil_timbru_maxim(proces_civil c, double &maxim, int &nrp);

    void citire(); // functie de citire
    void afisare(); // functie de afisare
};

proces_civil::proces_civil() {}

proces_civil::proces_civil(string reclamant, string reclamat, double dauneMorale, double dauneMateriale,
                           double taxa_timbru, int nrMartori, int nrProces = ++co) : proces(reclamant, reclamat,
                                                                                            nrProces) {
    this->dauneMorale = dauneMorale;
    this->dauneMateriale = dauneMateriale;
    this->taxa_timbru = taxa_timbru;
    this->nrMartori = nrMartori;
    if (nrMartori > 5)
        stadiu = 1;
    else
        stadiu = 0;
    taxa_timbru = 0.1 * (dauneMorale + dauneMateriale);
}

proces_civil::proces_civil(const proces_civil &ob) : proces(ob) {
    dauneMorale = ob.dauneMorale;
    dauneMateriale = ob.dauneMateriale;
    taxa_timbru = ob.taxa_timbru;
    nrMartori = ob.taxa_timbru;
}

proces_civil::~proces_civil() {}

void proces_civil::proces_civil_timbru(proces_civil c) const {
    cout << "Taxa de timbru a procesului civil " << c.nrProces << " este " << c.taxa_timbru << endl;
}

void proces_civil::marire_civil_timbru(proces_civil &c) {
    c.taxa_timbru += k;
}

void proces_civil::proces_civil_stocare_timbru(proces_civil c) {
    l.push_back(c.taxa_timbru);
}

void proces_civil::proces_civil_timbru_maxim(proces_civil c, double &maxim, int &nrp) {
    if (c.taxa_timbru > maxim) {
        maxim = c.taxa_timbru;
        nrp = c.nrProces;
    }
}

proces_civil &proces_civil::modific_stadiu_proces_civil(int martori, int nr, proces_civil &p) {
    p.nrMartori = martori;
    if (martori > 5)
        p.stadiu = 1;
    else
        p.stadiu = 0;
    return *this;
}


proces_civil &proces_civil::operator=(proces_civil &ob) {
    if (this != &ob) {
        proces::operator=(ob);
        dauneMorale = ob.dauneMorale;
        dauneMateriale = ob.dauneMateriale;
        nrMartori = ob.nrMartori;
        stadiu = ob.stadiu;
        taxa_timbru = ob.taxa_timbru;
    }
    return *this;
}

istream &operator>>(istream &is, proces_civil &proces_civil) {
    cout << "APELEZ TEMPLATE PENTRU PROCES CIVIL" << endl;
    is >> (proces &) (proces_civil);///downcast
    cout << "Introduceti valoarea daunelor morale ";
    is >> proces_civil.dauneMorale;
    cout << "Introduceti valoarea daunelor materiale ";
    is >> proces_civil.dauneMateriale;
    cout << "Introduceti numarul de martori: ";
    is >> proces_civil.nrMartori;
    if (proces_civil.nrMartori > 5)
        proces_civil.stadiu = 1;
    else
        proces_civil.stadiu = 0;
    proces_civil.taxa_timbru = 0.1 * (proces_civil.dauneMorale + proces_civil.dauneMateriale);
    return is;
}

ostream &operator<<(ostream &os, proces_civil &proces_civil) {
    os << "\nAcum afisam date despre procesul civil\n";
    os << (proces &) (proces_civil);
    os << "Valoarea daunelor morale din procesul civil este " << proces_civil.dauneMorale << endl;
    os << "Valoarea daunelor materiale din procesul civil este " << proces_civil.dauneMateriale << endl;
    os << "Numarul de martori din procesul civil este " << proces_civil.nrMartori << endl;
    os << "Stadiul procesului civil este " << proces_civil.stadiu << endl;
    os << "Taxa de timbru a procesului civil este " << proces_civil.taxa_timbru << endl;
    return os;
}

void proces_civil::citire() {
    proces::citire();
    cout << "Introduceti valoarea daunelor morale ";
    cin >> dauneMorale;
    cout << "Introduceti valoarea daunelor materiale ";
    cin >> dauneMateriale;
    cout << "Introduceti numarul de martori: ";
    cin >> nrMartori;
    if (nrMartori > 5)
        stadiu = 1;
    else
        stadiu = 0;
    taxa_timbru = 0.1 * (dauneMorale + dauneMateriale);
}

void proces_civil::afisare() {
    cout << "\nAcum afisam date despre procesul civil\n";
    proces::afisare();
    cout << "Valoarea daunelor morale din procesul civil este " << dauneMorale << endl;
    cout << "Valoarea daunelor materiale din procesul civil este " << dauneMateriale << endl;
    cout << "Numarul de martori din procesul civil este " << nrMartori << endl;
    cout << "Stadiul procesului civil este " << stadiu << endl;
    cout << "Taxa de timbru a procesului civil este " << taxa_timbru << endl;
}

class proces_penal : public proces {
protected:
    int dovezi;
    bool stadiu;
public:
    proces_penal(); // constructor de intializare fara parametrii pentru clasa proces_penal
    proces_penal(string, string, int, int); // constructor de initializare cu parametrii
    proces_penal(const proces_penal &); // constructor de copiere
    ~proces_penal(); // destructor
    proces_penal &operator=(proces_penal &);

    /*proces_penal(int x, string y, string z, int c):proces(x,y,z)
    {
        dovezi = c;
        if(dovezi > 25)
            stadiu = 1;
        else
            stadiu = 0;
    }*/
    friend istream &
    operator>>(istream &is, proces_penal &proces_penal); // supraincarcare operator citire " >> " clasa proces_penal
    friend ostream &
    operator<<(ostream &os, proces_penal &proces_penal); // supraincarcare operator afisare " << " clasa proces_penal
    proces_penal &modific_stadiu_proces_penal(int dovezi, int nr, proces_penal &p);

    void citire();

    void afisare();
};

proces_penal::proces_penal() {}

proces_penal::proces_penal(string reclamant, string reclamat, int dovezi, int nrProces = ++co) : proces(reclamant,
                                                                                                        reclamat,
                                                                                                        nrProces) {
    this->dovezi = dovezi;
    if (dovezi > 25)
        stadiu = 1;
    else
        stadiu = 0;
}

proces_penal::proces_penal(const proces_penal &ob) : proces(ob) {
    dovezi = ob.dovezi;
}

proces_penal::~proces_penal() {}

proces_penal &proces_penal::modific_stadiu_proces_penal(int nrdovezi, int nr, proces_penal &p) {
    p.dovezi = nrdovezi;
    if (p.dovezi > 25)
        p.stadiu = 1;
    else
        p.stadiu = 0;
    return *this;
}


istream &operator>>(istream &is, proces_penal &proces_penal) {
    cout << "APELEZ TEMPLATE PENTRU PROCES PENAL" << endl;

    is >> (proces &) (proces_penal);
    cout << "Introduceti numarul dovezilor ";
    is >> proces_penal.dovezi;

    if (proces_penal.dovezi > 5)
        proces_penal.stadiu = 1;
    else
        proces_penal.stadiu = 0;

    return is;
}

ostream &operator<<(ostream &os, proces_penal &proces_penal) {
    os << "\nAcum afisam date despre procesul penal\n";
    os << (proces &) (proces_penal);
    os << "Numarul de dovezi din procesul penal este " << proces_penal.dovezi << endl;
    os << "Stadiul procesului penal este " << proces_penal.stadiu << endl;
    return os;
}

proces_penal &proces_penal::operator=(proces_penal &ob) {
    if (this != &ob) {
        proces::operator=(ob);
        dovezi = ob.dovezi;
        stadiu = ob.stadiu;
    }
    return *this;
}

void proces_penal::citire() {
    proces::citire();
    cout << "Introduceti numarul dovezilor ";
    cin >> dovezi;

    if (dovezi > 5)
        stadiu = 1;
    else
        stadiu = 0;
}

void proces_penal::afisare() {
    cout << "\nAcum afisam date despre procesul penal\n";
    proces::afisare();
    cout << "Numarul de dovezi din procesul penal este " << dovezi << endl;
    cout << "Stadiul procesului penal este " << stadiu << endl;
}

class justitie {
protected:
    int nrprocese;
    vector<int> tip;
    vector<proces_civil> v;
    vector<proces_penal> w;


public:
    justitie() // constructor de initializare fara parametrii
    {
        nrprocese = 0;
    }

    justitie(int a) // constructor de initializare cu parametrii
    {
        nrprocese = a;
    }

    //justitie(const justitie&); // constructor de copiere
    ~justitie() // destructor
    {
        cout << "apelez destructor" << endl;
        v.clear();
        w.clear();
        tip.clear();
    }

    friend istream &operator>>(istream &in, justitie &justitie); // supraincarcare operator citire " >> " clasa justitie
    friend ostream &
    operator<<(ostream &out, justitie &justitie); // supraincarcare operator citire " >> " clasa justitie
    justitie &operator=(justitie &);

    justitie &modific_stadiu_proces_civil1(int martori, int nr, justitie &j);

    justitie &modific_stadiu_proces_penal1(int nrdovezi, int nr, justitie &j);

    void afisare_timbru(justitie &j);

    void afisare1(justitie &j);

    void marire_timbru(justitie &j);

    void afisare_timbru_maxim(justitie &j);

    void stocare_timbru(justitie &j);

};

/*justitie::justitie(const justitie& ob)
{
    nrprocese = ob.nrprocese;
    v = ob.v;
    w = ob.w;
}*/

justitie &justitie::modific_stadiu_proces_civil1(int martori, int nr, justitie &j) {
    j.v[nr].modific_stadiu_proces_civil(martori, nr, j.v[nr]);
    return *this;
}

void justitie::afisare_timbru(justitie &j) {
    for (int i = 0; i < j.nrprocese; i++) {
        if (j.tip[i] == 1)
            j.v[i].proces_civil_timbru(j.v[i]);
    }
}

void justitie::marire_timbru(justitie &j) {
    for (int i = 0; i < j.nrprocese; i++) {
        if (j.tip[i] == 1)
            j.v[i].marire_civil_timbru(j.v[i]);
    }
}

void justitie::afisare1(justitie &j) {
    cout << "AICI AFISEZ TAXELE DE TIMBRU PROCESE CIVILE ORDONATE CRESCATOR DUPA VALOARE" << endl;
    l.sort(); //crescator
    list<float>::iterator p;

    for (p = l.begin(); p != l.end(); p++) cout << *p << " ";

}

void justitie::stocare_timbru(justitie &j) {
    for (int i = 0; i < j.nrprocese; i++) {
        if (j.tip[i] == 1)
            j.v[i].proces_civil_stocare_timbru(j.v[i]);
    }
}

void justitie::afisare_timbru_maxim(justitie &j) {
    double maxim = 0.0;
    int nrp = 0;
    for (int i = 0; i < j.nrprocese; i++) {
        if (j.tip[i] == 1)
            j.v[i].proces_civil_timbru_maxim(j.v[i], maxim, nrp);
    }
    cout << "Acum afisam procesul civil cu taxa de timbru maxima\n";
    if (j.nrprocese != 0)
        cout << j.v[nrp - 1];
}

justitie &justitie::modific_stadiu_proces_penal1(int nrdovezi, int nr, justitie &j) {
    j.w[nr].modific_stadiu_proces_penal(nrdovezi, nr, j.w[nr]);
    return *this;
}


istream &operator>>(istream &in, justitie &justitie) {

    cout << "APELEZ TEMPLATE PENTRU JUSTITIE" << endl;

    cout << "Introduceti numarul de procese ";
    in >> justitie.nrprocese;

    if (justitie.nrprocese != 0) {
        cout << "\nIntroduceti datele despre procese:";
        for (int i = 0; i < justitie.nrprocese; i++) {
            try {
                cout << "\nProcesul " << i + 1 << ":\n";

                cout << "Introduceti tipul procesului(1-CIVIL/2-PENAL)\n";
                justitie.tip.emplace_back();
                cin >> justitie.tip.back();

                if (justitie.tip[i] == 1) {
                    justitie.v.emplace_back();
                    in >> justitie.v.back();
                } else if (justitie.tip[i] == 2) {
                    justitie.w.emplace_back();
                    in >> justitie.w.back();
                } else {
                    throw std::runtime_error("EROARE. TREBUIE SA INTRODUCETI 1 SAU 2");
                }
            }
            catch (std::runtime_error &e) {
                cout << e.what();
            }
        }
    }
    return in;
}

ostream &operator<<(ostream &out, justitie &justitie) {
    out << "Acum afisam date despre procese" << endl;
    for (int i = 0; i < justitie.nrprocese; i++) {
        out << "\nAfisam date despre procesul " << i + 1 << ":\n";
        if (justitie.tip[i] == 1)
            out << justitie.v[i] << endl;
        else
            out << justitie.w[i] << endl;
    }
    return out;
}

justitie &justitie::operator=(justitie &ob) {
    if (this != &ob) {
        nrprocese = ob.nrprocese;
        v.clear();
        w.clear();
        tip.clear();
        for (int i = 0; i < nrprocese; i++) {
            w.emplace_back(ob.w[i]);
            v.emplace_back(ob.v[i]);
            tip.emplace_back(ob.tip[i]);
        }
    }
    return *this;
}

template<class Ttype>
void afisare_template(Ttype& j) {
    cout << j << " ";
}

template<class Ttype>
void citire_template(Ttype &j) {
    cin >> j;
}

template<class Ttype>
void citire_template(Ttype *j) {
    cin >> *j;
}


int main() {

    justitie j;
    proces *x;
    int martori, nr, nrdovezi;

    int optiune;
    do {
        cout << "\n\nIntroduceti o tasta (1/2/3/4/5/6/7/8/9/10/11 astfel\n";
        cout << "1- Introducere date clasa justitie cu template\n";
        cout << "11- Introducere date clasa proces civil cu template\n";
        cout << "12- Introducere date clasa proces penal cu template\n";
        cout << "13- Marire taxe de timbru cu o constanta\n";
        cout << "2- Afisare date procese\n";
        cout << "3- Modificare stadiu proces civil\n";
        cout << "4- Modificare stadiu proces penal\n";
        cout << "5- Atribuire date intre 2 procese civile\n";
        cout << "6- Atribuire date intre 2 procese penale\n";
        cout << "7- Atribuie date intre doua obiecte de tip justitie\n";
        cout << "8- Afisarea taxa de timbru procese civile cu STL\n";
        cout << "9- Terminare\n";

        cin >> optiune;
        switch (optiune) {
            case (1): {
                citire_template(j);
                break;
            }

            case (11): {
                citire_template(dynamic_cast<proces_civil *>(x));
                break;
            }
            case (12): {
                citire_template(dynamic_cast<proces_penal *>(x));
                break;
            }
            case (13): {
                j.marire_timbru(j);

                //j.stocare_timbru(j);
                //j.afisare1(j);///cu stl

                cout << j;
                break;
            }

            case (2): {
                afisare_template(j);
                //cout << j;
                break;
            }
            case (3): {
                cout << "Introduceti numarul de martori=";
                cin >> martori;
                cout << "Introduceti numarul procesului civil pe care vreti sa il modificati=";
                cin >> nr;

                j.modific_stadiu_proces_civil1(martori, nr - 1, j);
                cout << j;
                break;
            }
            case (4): {
                cout << "Introduceti numarul de dovezi=";
                cin >> nrdovezi;
                cout << "Introduceti numarul procesului penal pe care vreti sa il modificati=";
                cin >> nr;

                j.modific_stadiu_proces_penal1(nrdovezi, nr - 1, j);
                cout << j;
                break;
            }
            case (5): {
                proces *c[2];///am declarat un vector de pointer de tip clasa de baza
                c[0] = new proces_civil();///acest vector este incarcat cu obiecte de tip clasa derivata
                c[1] = new proces_civil();/// upcasting

                cin
                        >> *(proces_civil *) c[0];///downcast pentru ca c[0] este de tip clasa de baza si il tratam ca un obiect al clasei derivate
                cin >> *(proces_civil *) c[1];///downcast

                c[0]->afisare();
                c[1]->afisare();

                c[1] = c[0];
                cout << "Acum afisam al primul civil\n";
                c[0]->afisare();
                cout << "Acum afisam al doilea proces civil care este modificat luand valoarea primului\n";
                c[1]->afisare();
                break;
            }
            case (6): {
                proces *c[2];///am declarat un vector de pointer de tip clasa de baza
                c[0] = new proces_penal();///acest vector este incarcat cu obiecte de tip clasa derivata
                c[1] = new proces_penal();/// upcasting

                c[0]->citire();
                c[1]->citire();

                c[0]->afisare();
                c[1]->afisare();

                c[1] = c[0];
                cout << "Acum afisam al primul penal\n";
                c[0]->afisare();
                cout << "Acum afisam al doilea proces penal care este modificat luand valoarea primului\n";
                c[1]->afisare();
                break;
            }
            case (7): {
                justitie j1;
                j1 = j;
                cout << "Acum afisam datele noului obiect de tip justitie\n";
                cout << j1;
                break;
            }
            case (8): {
                j.stocare_timbru(j);
                j.afisare1(j);///cu stl
                cout << endl;
                j.afisare_timbru(j);///fara stl
                break;
            }
            default:
                exit(EXIT_SUCCESS);
        }
    } while (optiune != 9);
    return 0;
}