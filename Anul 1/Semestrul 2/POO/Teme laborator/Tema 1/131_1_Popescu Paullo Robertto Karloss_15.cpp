#include <iostream>
using namespace std;

int m,n,x,n1,m1,x1,ok,n2,m2,n3,m3,n_22,m_22,n22,m22,n33,m33,n_33,m_33,N,M,val;
int N1,M1,N2,M2,N3,M3,N4,M4,N5, M5;
///--------------------------


class matrice
{
public:
    int l,c,v[100][100];
    float w[100][100];
    matrice(int, int);///CONSTRUCTOR PENTRU INITIALIZARE
    matrice(const matrice&);///constructor pentru initializarea cu un numar dat pe toate componentele (primeste ca parametru numarul respectiv si numarul de linii si de coloane);
    matrice(double, int, int);///CONSTRUCTOR DE COPIERE

    ~matrice(); // dealocă zona alocată valorilor matricei
    matrice& operator = (const matrice&); // matrice A = B + C;
    matrice& transpusa(const matrice&);///transpusa unei matrice

    void citire(matrice&);///METODA PUBLICA PENTU CITIRE
    void afisare(matrice);
    void reactualizare(double x, int linii, int coloane);///metoda publica pentru reactualizarea numarului de linii si coloane si initializarea componentelor cu un numar dat (primeste ca parametru numarul respectiv, numarul liniilor si al coloanelor);


    friend matrice operator + (const matrice&, const matrice&); /// matrice A=B+C;
    friend matrice operator * (const matrice&, const matrice&); /// matrice A=B*C;
    friend matrice operator - (const matrice&, const matrice&); /// matrice A=B-C;
    friend matrice operator * (const matrice&, int valoare); /// matrice A=B*valoare;
    friend bool operator == (const matrice&, const matrice&); /// OPERATOR PENTRU TESTAREA EGALITATII A DOUA MATRICI
    friend int nrlinii(const matrice&);
    friend int nrcoloane(const matrice&);
    friend int nrelemente(const matrice&);


private:
    int rows, cols;///membri privati pentru matricea propriuzisa, numarul de linii si numarul de coloane
    double** cont; ///indica tabloul pointerilor liniilor matricei

    inline void alloc()   /// constituie zona valorilor matricei
    {
        cont = new double* [rows];  /// tabloul pointerilor de linii
        for(int i=0; i < rows; i++)
            cont[i] = new double [cols]; /// tabloul valorilor din linie
    }
};

///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii
///clasei rows si cols si se aloca dinamic memorie pentru matrice
matrice::matrice(int n=0, int m=0)
{
    if(n && !m) m = n;
    rows = n;///se preiau m si n de catre membrii clasei rows si cols
    cols = m;
    alloc();///se aloca dinamic memorie pentru n linii si m coloane
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            cont[i][j] = 0;///se initializeaza elementele matricei cu 0
}

matrice::matrice(const matrice& M)
{
    rows = M.rows;
    cols = M.cols;
    alloc();
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            cont[i][j] = M.cont[i][j];
}

///constructor pentru initializarea cu un numar dat pe toate componentele (primeste ca parametru numarul respectiv si numarul de linii si de coloane);
matrice::matrice(double x, int linii, int coloane)
{
    int i = 0, j = 0;
    rows = linii;
    cols = coloane;
    alloc();
    for(i=0; i<rows; i++)
        for(j=0; j<cols; j++)
            cont[i][j] = x;
}
///metoda-operator public de atribuire =;
matrice& matrice::operator = (const matrice& M)
{
    if(this == &M) return *this; /// evita auto-atribuirea (matrice M = M)
    if(rows != M.rows || cols != M.cols)
    {
        this -> ~matrice(); /// isi sterge din HEAP tabloul alocat
        rows = M.rows;
        cols = M.cols;
        alloc(); /// preia datele lui M
    }
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            cont[i][j] = M.cont[i][j]; /// copiaza valorile din M
    return *this;
}

///transpusa
matrice& matrice::transpusa(const matrice& M)
{
    this->~matrice();
    rows = M.cols;
    cols = M.rows;
    alloc(); /// preia datele lui M

    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            cont[i][j] = M.cont[j][i]; /// copiaza valorile din M
    return *this;
}

matrice::~matrice()   /// cere SO să elibereze zona HEAP alocată
{
    for(int i=0; i < rows; i++)
        delete [] cont[i];
    delete [] cont;
}

void matrice::afisare(matrice M)
{
    cout<<"\nElementele matricei sunt\n";

    for(int i = 0, n=M.rows; i < n; i++)
    {
        for(int j = 0, m=M.cols; j < m; j++)
            cout << M.cont[i][j]<<" ";
        cout << '\n';
    }
    cout << '\n';
}

void matrice::citire(matrice& M)
{
    cout<<"\nIntroduceti elementele matricei\n";
    int rows = M.rows, cols = M.cols;
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            cin >> M.cont[i][j];
}

///metoda publica pentru reactualizarea numarului de linii si coloane si initializarea componentelor cu un numar dat (primeste ca parametru numarul respectiv, numarul liniilor si al coloanelor);
void matrice::reactualizare(double x, int linii, int coloane)
{
    this -> ~matrice(); /// isi sterge din HEAP tabloul alocat
    rows = linii;
    cols = coloane;
    alloc(); /// preia datele lui M

    int i, j;
    for(i=0; i<linii; i++) /// parcurg liniile matricei cunoscute
        for(j=0; j<coloane; j++) /// parcurg coloanele matricei cunoscute
            cont[i][j] = x;
}
matrice operator + (const matrice& A, const matrice& B)
{
    if(A.rows != B.rows || A.cols != B.cols) /// daca n-au acelaşi tip,
        return matrice();            /// atunci returnează matricea vida
    int rows = A.rows, cols = A.cols;
    matrice C(rows, cols);
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            C.cont[i][j] = A.cont[i][j] + B.cont[i][j];
    return C;
}

matrice operator - (const matrice& A, const matrice& B)
{
    if(A.rows != B.rows || A.cols != B.cols) /// daca n-au acelaşi tip,
        return matrice();            /// atunci returnează matricea vida
    int rows = A.rows, cols = A.cols;
    matrice C(rows, cols);
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            C.cont[i][j] = A.cont[i][j] - B.cont[i][j];
    return C;
}

matrice operator * (const matrice& A, const matrice& B)
{
    if(A.cols != B.rows) // dacă nu-s inlanţuite,
    {cout<<"\nNu se poate efectua inmultirea celor oua matrici, deoarece nu sunt inlantuite\n";
        return matrice(); // atunci returnează matricea vidă
    }
    int rows=A.rows, cols=B.cols;
    matrice C(rows, cols);
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            for(int k=0; k < A.cols; k++)
                C.cont[i][j] += A.cont[i][k] * B.cont[k][j];
    return C;
}

matrice operator * (const matrice& A, int valoare)
{
    int rows=A.rows, cols=A.cols;
    matrice C(rows, cols);
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            C.cont[i][j] = A.cont[i][j]*valoare;
    return C;
}

bool operator == (const matrice& A, const matrice& B)
{
    if(A.rows != B.rows || A.cols != B.cols) /// daca n-au acelaşi tip,
        return false;            /// atunci returnează matricea vida
    int rows = A.rows, cols = A.cols;
    for(int i=0; i < rows; i++)
        for(int j=0; j < cols; j++)
            if(A.cont[i][j]!=B.cont[i][j])
                return false;
    return true;
}

int nrlinii(const matrice& A)
{
    return A.rows;
}

int nrcoloane(const matrice& A)
{
    return A.cols;
}

int nrelemente(const matrice& A)
{
    return A.rows*A.cols;
}


int main()
{
    int optiune;
    cout<<"Introduceti numarul de linii ale matricei=";
    cin>>n;
    cout<<"Introduceti numarul de coloane ale matricei=";
    cin>>m;
    matrice a(n,m);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
    a.citire(a);///se apeleaza metoda publica de citire
    a.afisare(a);///se apeleaza metoda publica de afisare

    do
    {

        cout<<"\n\nIntroduceti o tasta (1/2/3/4/5/6/7/8/9/10/11 astfel\n";
        cout<<"1- Initializarea cu un numar dat pe toate componentele matricei\n";
        cout<<"2- Atribuirea matricei initiale unei alte matrice\n";
        cout<<"3- Reactualizare numar de linii,coloane si valorile din matrice\n";
        cout<<"4- Suma a doua matrice\n";
        cout<<"5- Diferenta a doua matrice\n";
        cout<<"6- Produsul a doua matrice\n";
        cout<<"7- Inmultirea matricei cu o valoare data\n";
        cout<<"8- Testarea egalitatii doua matrice\n";
        cout<<"9- Afisarea numarului de linii/coloane/numar de elemete a unei matrice\n";
        cout<<"10- Rezolvarea ecuatiei AX+B=0\n";
        cout<<"11- Terminare\n";

        cin>>optiune;
        if(optiune>10)
            break;

        if(optiune==1)
        {
            cout<<"\nIntroduceti un numar cu care initializati toate elementele matricei=";
            cin>>x;
            matrice b(x,n,m); ///constructor pentru initializarea cu un numar dat pe toate componentele (primeste ca parametru numarul respectiv si numarul de linii si de coloane);
            cout<<"\nAcum afisam matricea obtinuta din matricea initiala prin inlocuirea fiecarui element cu valoarea citita anterior\n";
            b.afisare(b);///se apeleaza metoda publica de afisare
        }
        else if(optiune==2)
        {
            matrice c(n,m);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            c=a;///metoda-operator public de atribuire =;
            cout<<"\nAcum afisam matricea obtinuta din matricea initiala prin atribuirea fiecarui element\n";
            c.afisare(c);///se apeleaza metoda publica de afisare
        }
        else if(optiune==3)
        {
            cout<<"Acum urmeaza sa reactualizam numarul de linii, coloane si valorile din matrice cu valori citite de utilizator pentru matrice initiala de mai jos\n";
            a.afisare(a);///se apeleaza metoda publica de afisare

            cout<<"Introduceti numarul de linii ale  matricei reactualizate=";
            cin>>n1;
            cout<<"Introduceti numarul de coloane ale matricei reactualizate=";
            cin>>m1;
            cout<<"\nIntroduceti un numar cu care initializati toate elementele matricei reactualizate=";
            cin>>x1;
            a.reactualizare(x1, n1, m1);
            a.afisare(a);///se apeleaza metoda publica de afisare
            cout<<"\nAcum matricea este reactualizata\n";
        }
        else if(optiune==4)
        {
            cout<<"\nAdunarea a doua matrice citite de la tastatura\n";

            do
            {
                cout<<"Introduceti numarul de linii ale primei matricei=";
                cin>>n2;
                cout<<"Introduceti numarul de coloane ale primei matricei=";
                cin>>m2;
                cout<<"Introduceti numarul de linii ale matricei a doua=";
                cin>>n3;
                cout<<"Introduceti numarul de coloane ale matricei a doua=";
                cin>>m3;
                if(n2!=n3 ||m2!=m3)
                    cout<<"Numarul de linii/coloane al celor 2 matrice trebuie sa coincida, mai incercati\n";
            }
            while(n2!=n3||m2!=m3);


            matrice d(n2,m2);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            matrice e(n3,m3);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            cout<<"Urmeaza sa citim elementele primei matrice din suma\n";
            d.citire(d);///se apeleaza metoda publica de citire

            cout<<"Urmeaza sa citim elementele celei de-a doua matrice din suma\n";
            e.citire(e);///se apeleaza metoda publica de citire

            cout<<"\nSuma celor doua matrice citite de la tastatura este:\n";

            matrice f(n2,m2);
            f=d+e;///operator friend + (suma)
            f.afisare(f);
        }

        else if(optiune==5)
        {
            cout<<"\nDiferenta a doua matrice citite de la tastatura\n";

            do
            {
                cout<<"Introduceti numarul de linii ale primei matricei=";
                cin>>n22;
                cout<<"Introduceti numarul de coloane ale primei matricei=";
                cin>>m22;
                cout<<"Introduceti numarul de linii ale matricei a doua=";
                cin>>n33;
                cout<<"Introduceti numarul de coloane ale matricei a doua=";
                cin>>m33;
                if(n22!=n33 ||m22!=m33)
                    cout<<"Numarul de linii/coloane al celor 2 matrice trebuie sa coincida, mai incercati\n";
            }
            while(n22!=n33||m22!=m33);


            matrice dd(n22,m22);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            matrice ee(n33,m33);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            cout<<"Urmeaza sa citim elementele primei matrice din diferenta\n";
            dd.citire(dd);///se apeleaza metoda publica de citire

            cout<<"Urmeaza sa citim elementele celei de-a doua matrice din diferenta\n";
            ee.citire(ee);///se apeleaza metoda publica de citire

            cout<<"\nDiferenta celor doua matrice citite de la tastatura este:\n";

            matrice ff(n22,m22);
            ff=dd-ee;///operator friend -
            ff.afisare(ff);


        }
        else if(optiune==6)
        {
            cout<<"\nProdusul a doua matrice citite de la tastatura\n";

            do
            {
                cout<<"Introduceti numarul de linii ale primei matricei=";
                cin>>n_22;
                cout<<"Introduceti numarul de coloane ale primei matricei=";
                cin>>m_22;
                cout<<"Introduceti numarul de linii ale matricei a doua=";
                cin>>n_33;
                cout<<"Introduceti numarul de coloane ale matricei a doua=";
                cin>>m_33;
                if(m_22!=n_33)
                    cout<<"Numarul de coloane al primei matrice trebuie sa coincida cu numarul de linii al celei de-a doua matrice, mai incercati\n";
            }
            while(m_22!=n_33);


            matrice d_22(n_22,m_22);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            matrice e_22(n_33,m_33);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            cout<<"Urmeaza sa citim elementele primei matrice din suma\n";
            d_22.citire(d_22);///se apeleaza metoda publica de citire

            cout<<"Urmeaza sa citim elementele celei de-a doua matrice din suma\n";
            e_22.citire(e_22);///se apeleaza metoda publica de citire

            cout<<"\nProdusul celor doua matrice citite de la tastatura este:\n";

            matrice f_22(n_22,m_33);
            f_22=d_22*e_22;///operator friend * (produsul)
            f_22.afisare(f_22);

        }
        else if(optiune==7)
        {
            cout<<"\nProdusul dintre o matrice si valoare data\n";

            cout<<"Introduceti numarul de linii ale matricei=";
            cin>>N;
            cout<<"Introduceti numarul de coloane ale matricei=";
            cin>>M;


            matrice A(N,M);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            cout<<"Urmeaza sa citim elementele matricei\n";
            A.citire(A);///se apeleaza metoda publica de citire
            cout<<"Introduceti valoarea cu care doriti sa inmultiti matricea=";
            cin>>val;


            cout<<"\nProdusul dintre o matrice si valoare data este:\n";

            matrice B(N,M);
            B=A*val;///operator friend * (produsul)
            B.afisare(B);

        }
        else if(optiune==8)
        {
            cout<<"\nTestarea egalitatii a doua matrice citite de la tastatura\n";


            cout<<"Introduceti numarul de linii ale primei matricei=";
            cin>>N1;
            cout<<"Introduceti numarul de coloane ale primei matricei=";
            cin>>M1;
            cout<<"Introduceti numarul de linii ale matricei a doua=";
            cin>>N2;
            cout<<"Introduceti numarul de coloane ale matricei a doua=";
            cin>>M2;
            if(n22!=n33 ||m22!=m33)
                cout<<"Numarul de linii/coloane al celor 2 matrice trebuie sa coincida, mai incercati\n";


            matrice C(N1,M1);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            matrice D(N2,M2);///se apeleaza constructorul de initializare prin care se preiau m si n de catre membrii clasei rows si cols si se aloca dinamic memorie pentru matrice
            cout<<"Urmeaza sa citim elementele primei matrice\n";
            C.citire(C);///se apeleaza metoda publica de citire

            cout<<"Urmeaza sa citim elementele celei de-a doua matrice\n";
            D.citire(D);///se apeleaza metoda publica de citire
            if(C==D)
                cout<<"Cele doua matrici sunt egale intre ele\n";
            else
                cout<<"Cele doua matrici nu sunt egale intre ele\n";

        }

        else if(optiune==9)
        {
            cout<<"Afisarea numarului de linii, coloane, numar elemente dintr-o matrice data\n";

            cout<<"Introduceti numarul de linii ale matricei=";
            cin>>N3;
            cout<<"Introduceti numarul de coloane ale matricei=";
            cin>>M3;
            cout<<"Urmeaza sa citim elementele matrice\n";
            matrice E(N3,M3);
            E.citire(E);///se apeleaza metoda publica de citire
            cout<<"Numarul de linii ale matricei citite este=";
            cout<<nrlinii(E)<<endl;
            cout<<"Numarul de coloane ale matricei citite este=";
            cout<<nrcoloane(E)<<endl;
            cout<<"Numarul de elemente ale matricei citite este=";
            cout<<nrelemente(E)<<endl;

        }

        else if(optiune==10)
        {
            cout<<"Introduceti numarul de linii ale matricei A=";
            cin>>N4;
            cout<<"Introduceti numarul de coloane ale matricei A=";
            cin>>M4;
            cout<<"Acum introducem elementele matricei A\n";
            matrice F(N4,M4);
            F.citire(F);
            matrice H(N4,M4);
            H=H.transpusa(F);
            cout<<"Transpusa matricei A este\n";
            H.afisare(H);

            cout<<"Introduceti numarul de linii ale matricei B=";
            cin>>N5;
            cout<<"Introduceti numarul de coloane ale matricei B=";
            cin>>M5;
            cout<<"Acum introducem elementele matricei B\n";
            matrice G(N5,M5);
            G.citire(G);
            int val=-1;
            matrice I(N5,M5);
            I=G*val;
            cout<<"Matricea B *(-1) este \n";
            I.afisare(I);
            matrice X(N4,M5);
            X=I*H;
            cout<<"AX+B=0\n";
            X.afisare(X);

        }
        else if(optiune>10)
            return 0;
    }
    while(optiune<=10);
}
