#include <iostream>
#include <cstdlib>
#include <algorithm>
#include <iomanip>
#include <cstring>
#include <sys/time.h>
#include <cstdio>
#include <chrono>


using namespace std;

int baza[1000],nrbaze;
// returneaza timpul in microsecunde (o milionime de secunda)
inline double getTime()
{
    struct timeval tv;

    gettimeofday(&tv, NULL);
    return (tv.tv_sec * 1000000LL + tv.tv_usec)/1000.0;
}

clock_t t_start, t_stop, t_stop10;
double seconds1, seconds2, seconds3, seconds4[100], seconds5, seconds6, seconds7,seconds8,seconds10;
int ok1,ok2,ok3,ok4;

// Sursa: https://stackoverflow.com/questions/28115724/getting-big-random-numbers-in-c-c
unsigned long random()
{
    unsigned long long r = 0;

    for (int i = 0; i < 5; ++i)
    {
        r = (unsigned long)(r << 15) | (unsigned long)(rand() & 0x7FFF);
    }

    return (unsigned long)r & 0xFFFFFFFFFFFFFFFFULL;
}

unsigned long long v[10000000], b[10000000], fr[10000000],w[10000000],nrmaxi;
unsigned long long bb[100000001], maxi,bux,aux,piv;
int i, n, j, ok, nrtest, k, r,nrcif;

void BubbleSort()
{
    for(i=0; i<n-1; i++) // numarul pasi = lungimea vectorului de sortat - 1, deci avem n-1 pasi intotdeauna
    {
        ok = 0; // initializam un ok cu 0, presupunem ca vectorul este sortat
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat BubbleSort\n";
            break;
        }
        for(j=0; j<n-i-1; j++) // avem n-i-1 comparatii pentru fiecare pas, folosind aceasta regula putem
            // sa evitam comparatiile care nu sunt necesare cum ar fi acelea cand array-ul este deja sortat
            // dar programul inca executa pasi de comparatie
        {
            if(v[j] > v[j+1]) // daca elementul de pe pozitia j este mai mare decat cel de pe pozitia urmatoare
            {
                swap(v[j],v[j+1]); // le interschibam, i.e. folosim functia swap
                ok = 1; // daca am ajuns aici, inseamna ca am facut o interschimbare, deci vectorul inca nu este sortat
            }
        }
        if(ok == 0) // daca vectorul este sortat pentru a ca nu a mai facut nicio interschimbare
            break;  // am terminat programul si iesim fortat
    }
}

void CountingSort()
{
    memset(fr,0,sizeof(fr)); // se reseteaza vectorul de frecventa fr
    for(i=0; i<n; i++) // initializez un vector de frecventa
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat CountingSort\n";
            break;
        }
        ++fr[v[i]]; // iterez pentru fiecare element numarul aparatiilor sale, crescand frecventa
    }
    for(i=1; i<=maxi; i++) //  de la al doilea element pana la maximul din vectorul de sortat
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat CountingSort\n";
            break;
        }
        fr[i] += fr[i-1]; // actualizam vectorul de frecventa fr, incepand de la i = 1 pana la maxi
        // adunand valoarea de pe pozitia anterioara la cea care urmeaza
        // de ex daca in vectorul intial de frecventa aveam fr[1] = 3 si fr[0] = 5
        // fr[1] = fr[1] + fr[0] = 3 + 5 = 8
    }
    for(i=n-1; i>=0; i--) // plecam de la coada pentru a mentinte stabilitatea algoritmului
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat CountingSort\n";
            break;
        }
        b[--fr[v[i]]] = v[i];  // decrementam cu 1 numarul de aparitii ale fiecarui element
        // si retinem in vectorul b pe acea pozitie elementul v[i]
    }
    for(i=0; i<n; i++)
        v[i]=b[i]; // creez un vector de copiere pentru b
}

void interclas(int p,int poz,int u)///apel functie de interclasare
{
    if(ok3==0)
    {

        int i=p; // retin prima pozitie a primei jumatati
        int j=poz+1; // retin prima pozitie a celei de a 2 a jumatati
        int k=0; // index vector sortat
        while(i<=poz && j<=u) // cat timp nu s-a terminat de parcurs nici o jumatate
        {
            t_stop10 = clock();
            seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
            if(seconds10 > 10)
            {
                cout << "Ignorat MergeSort\n";
                ok3=1;
                break;
            }
            if (v[i]<v[j]) // daca un element din prima jumatate<un element din a 2a jumatate
                b[k++]=v[i++]; // il retin intr-un alt vector
            else // daca un element din a 2 a jumatate<un element din prima jumatate
                b[k++]=v[j++]; // il retin intr-un alt vector
        }


        while (i<=poz) // daca au mai ramas elemente in prima jumatate
            b[k++]=v[i++]; // le adaug in acelasi vector

        while (j<=u) // daca au mai ramas elemente in a doua jumatate
            b[k++]=v[j++]; // le adaug in acelasi vector


        int t=p;
        for (k=0; k<(u-p)+1; k++)
            v[t++]=b[k]; // vectorul initial devine vectorul sortat
    }
}

void divimp(int p,int u) // apel functie divide et impera
{
    if(ok3==0)
        if (p<u)
        {
            int poz=(p+u)/2; // retin pozitia de mijloc a vectorului
            divimp(p,poz); // apelez de la inceput pana la mijloc
            divimp(poz+1,u);///apelez de la mijloc pana la sfarsit
            interclas(p,poz,u); // ordoneaza  cele 2 jumatati
        }
}

void MergeSort()
{
    divimp(0,n-1);
}

int partitie1(int p, int u)
{
    // scopul acestei functii este sa returneze pozitia pivotului
    // in vectorul final sortat
    int i,j;
    piv = v[u]; // retin ca pivot ultimul element

    i = p - 1; // incep de la inceputul array-ului

    for (j = p; j <=u- 1; j++) // parcurg vectorul pana la sfarsit, cu exceptia ultimului
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat QuickSort\n";
            ok1=1;
            break;
        }
        if (v[j] <= piv) // comparam fiecare element cu pivotul
        {
            i++;
            aux=v[i]; // interschimbam fiecare element de pe pozitia i cu cel de pe pozitia j
            v[i]=v[j];
            v[j]=aux;
        }
    }
    i++;
    aux=v[i]; // interschimbam fiecare element de pe pozitia i cu pivotul
    v[i]=v[u];
    v[u]=aux;
    return i; // returnez pozitia pivotului in array
}

int partitie2(int p, int u)
{
    int i,j;
    ///--------------------------

    int mij = p + (u - p) / 2; // determin pozitia din mijloc
    // ordonez crescator v[p], v[mij], v[u] pentru a stabili pivotul
    // valoarea din mijloc a celor 3 adica v[mij]
    if (v[p] > v[mij])
        swap(v[p], v[mij]);
    if (v[p] > v[u])
        swap(v[p], v[u]);
    if (v[mij] > v[u])
        swap(v[mij], v[u]);

    piv = v[mij]; // pivotul este mediana dintre v[p], v[mij], v[u]

    while(1<2)
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat QuickSort\n";
            ok2=1;
            break;
        }
        while (v[p] < piv)++p; // plec din stanga si caut primul element mai mare sau egal cu pivotul
        while (v[u] > piv)--u; // plec din dreapta si caut primul element mai mic sau egal cu pivotul
        if (p >= u)
            return u;

        swap(v[p], v[u]); // interschimb primul si ultimul element
        p++; // avansez spre stanga
        u--; // avansez spre dreapta
    }
}

void quickSort1(int p, int u)
{
    if(ok1==0)
        if (p < u)
        {
            int poz = partitie1(p, u);
            quickSort1(p, poz - 1); // apelez pentru jumatatea din stanga
            quickSort1(poz + 1,u); // apalez pentru jumatatea din dreapta
        }
}

void quickSort2(int p, int u)
{
    if(ok2==0)
        if (p < u)
        {
            int poz = partitie2(p, u);
            if(poz-p+1<u-poz)
            {
                quickSort2(p, poz); // apelez pentru jumatatea din stanga
                quickSort2(poz + 1,u); // apelez pentru jumatatea din dreapta
            }
            else
            {
                quickSort2(poz+1, u); // apelez pentru jumatatea din dreapta
                quickSort2(p,poz); // apelez pentru jumatatea din stanga
            }
        }
}

void radixSort1(int r, int baza, unsigned long long v[], int n)
{
    int fr[baza]; // declar vectorul de frecventa a cifrelor
    // care are dimensiunea maxima numarul bazei

    memset(fr,0,sizeof(fr)); // initializez in memorie
    // un vector de frecventa a cifrelor

    for (int i = 0; i < n; i++) // parcurg vectorul
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat RadixSort cu baze\n";
            break;
        }
        int cifra_curenta = (v[i] / r) % baza; // extrag cifra curenta din vector
        fr[cifra_curenta+1]++; // cresc frecventa de aparitie a cifrei curente
    }

    for (int i = 1; i < baza; i++)
    {
        fr[i] = fr[i]+ fr[i-1]; // actualizam vectorul de frecventa fr, incepand de la i = 1 pana la baza
        // adunand valoarea de pe pozitia anterioara la cea care urmeaza
        // de ex daca in vectorul intial de frecventa aveam fr[1] = 3 si fr[0] = 5
        // fr[1] = fr[1] + fr[0] = 3 + 5 = 8
        // aceste sume partiale le facem pentru a afla la ce indice trebuie sa plasez elementele cu o anumita cifra
        // pastrand ordinea relativa
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat RadixSort cu baze\n";
            break;
        }
    }

    for (int i = 0; i < n; i++) // parcurg vectorul
    {
        int cifra_curenta = (v[i] / r) % baza; // extrag cifra curenta din vector
        b[fr[cifra_curenta]] = v[i]; // plasez intr-un vector auxiliar b elementul v[i] pe pozitia fr[cifra_curenta]
        // pe care am stabilit-o mai sus
        fr[cifra_curenta]++; // incrementez indicele pentru a stabili pozitia pe care va fi plasat urmatorul element
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat RadixSort cu baze\n";
            break;
        }
    }

    for (int i = 0; i < n; i++)
        v[i] = b[i]; // copiez vectorul auxiliar in vectorul initial
}

void RadixSort_baze(int baza)
{

    unsigned long long r = 1; // r = baza la puterea i, unde i ia valori de la 0
    // la cate cifre are numarul
    // i reprezinta rangul cifrei in numar
    // unde 0 este rangul cifrei unitatilor
    // 1 este rangul cifrei zecilor
    // 2 este rangul cifrei sutelor
    // etc.

    while (r <= maxi) /// cat timp baza la putera i este mai mica
        /// decat maximul din vectorul de sortat
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat RadixSort cu baze\n";
            break;
        }
        radixSort1(r, baza, v, n); // la primul apel ordonam crescator
        // dupa cifra unitatilor
        // La al doilea apel, ordonam crescator dupa cifra zecilor
        // etc.
        r *= baza; // marim rangul cifrei dupa care facem sortarea
    }
}

void radix_sort_biti(unsigned long long v[], int n, int nr)   // lungimea este p, unde 10^p este lungimea array-ului.
{

    ///nr= pow(2, nr);
    int shift = 0;
    unsigned long long masca;

    for (int i = 0; i < nr; i++) // nr reprezinta numarul maxim de biti
    {
        t_stop10 = clock();
        seconds10 = (double)(t_stop10-t_start)/CLOCKS_PER_SEC;
        if(seconds10 > 10)
        {
            cout << "Ignorat RadixSort pe biti\n";
            break;
        }
        int p = 0; // initializez
        masca = 1 << shift; // masca pentru a determina bitul de pe pozitia curenta

        for (int j = 0; j < n; j++) // parcurg array-ul
            if ((v[j]&masca)== 0)	/// daca bitul curent este 0
            {
                bb[p] = v[j];  // adaug acel element intr-un vector auxiliar
                p+=1; // incrementez dimensiunea vectorului auxiliar bb
            }

        for (int j = 0; j < n; j++) // parcurg array-ul din nou
            if ((v[j]&masca) != 0)	// daca bitul curent este 1
            {
                bb[p] = v[j]; // adaug acel element intr-un vector auxiliar
                p+=1;
            }

        for (int j = 0; j < n; j++) // copiem array-ul sortat dupa bitul curent
            v[j] = bb[j];

        shift++; // trecem la urmatorul bit
    }
}

int main()
{
    int T,tip;

    cout<<"Introduceti numarul de teste ";
    cin >> T;
    nrtest = 1; // initializam numarul testului cu 0
    while(nrtest<=T) // cat timp numarul nrtest este mai mic decat numarul de teste
    {
        ok1=ok2=ok3=ok4=0;
        cout<<"\nTestul "<<nrtest<<endl;
        cout << "Introduceti numarul de elemente din array: ";
        cin >> n; // citim numarul de teste
        cout<<"Introduceti tipul de test"<<endl;
        cout<<"Apasati tasta 1 - vector aproape sortat crescator"<<endl;
        cout<<"Apasati tasta 2 - vector aproape sortat descrescator"<<endl;
        cout<<"Apasati tasta 3 - vector constant"<<endl;
        cout<<"Apasati tasta 4 - vector complet random"<<endl;
        cin>>tip;
        cout<<"\nIntroduceti numarul de baze in care doriti sa faceti sortarea pentru Radix sort=";
        cin>>nrbaze;
        int index_baze=1;
        while(index_baze<=nrbaze)
        {
            cout << "\nIntroduceti baza "<<index_baze<<" in care doriti sa faceti sortarea pentru Radix sort=";
            cin >> baza[index_baze];
            index_baze++;
        }

        cout << "Introduceti maximul din array: ";
        cin >> nrmaxi; // citim elementul maxim din array
        if(tip==1)
        {
            unsigned long long x=random()%nrmaxi; // generez primul termen din vector random
            v[n-1]=x;
            for(i=n-2; i>=0; i--)
                if(v[i+1]-1==0)
                    v[i]=v[i+1];
                else
                    v[i]=v[i+1]-1;
        }
        else if(tip==2)
        {
            unsigned long long x=random()%nrmaxi;
            v[0]=x;
            for(i=1; i<n; i++)
            {
                if(v[i-1]-1==0)
                    v[i]=v[i-1];
                else
                    v[i]=v[i-1]-1;

            }
        }
        else if(tip==3)
        {
            v[0]=random()%nrmaxi;
            for(i=0; i<n; i++)
                v[i] = v[0];
        }
        else if(tip==4)
        {
            for(i=0; i<n; i++)
                v[i] = random()%nrmaxi;
        }


        for(i=0; i<n; i++)
        {
            if(v[i]>maxi)
                maxi = v[i]; // maximul la counting sort
        }
        bux=maxi;
        while(bux)
        {
            nrcif++;
            bux/=2;
        }


        nrtest += 1; // avansam in teste

///-------------------------------------------------------
        cout<<"\nMetoda de sortare Bubble Sort"<<endl;

        memcpy(w,v,sizeof(w));
        cout << "Array-ul nesortat pentru Bubble sort este \n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;
        t_start = clock();
        BubbleSort();
        t_stop = clock();
        seconds1 = (double)(t_stop-t_start)/CLOCKS_PER_SEC;
        cout <<"\nArray-ul sortat pentru Bubble sort este:\n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;
        cout<<"\nTimpul de executie al programului pentru metoda Bubble Sort este " <<setprecision(8)<<seconds1<<"\n";
///-------------------------------------------------------

        cout<<"\nMetoda de sortare Counting Sort"<<endl;

        memcpy(v,w,sizeof(w));
        cout << "Array-ul nesortat pentru Counting sort este \n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;
        t_start = clock();
        CountingSort();
        t_stop = clock();
        seconds2 = (double)(t_stop-t_start)/CLOCKS_PER_SEC;

        cout <<"\nArray-ul sortat pentru Counting sort:\n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<"\nTimpul de executie al programului pentru metoda Counting Sort este " <<setprecision(8)<<seconds2<< "s\n";

///-------------------------------------------------------

        cout<<"\nMetoda de sortare Merge Sort"<<endl;

        memcpy(v,w,sizeof(w));
        cout << "Array-ul nesortat pentru Mergesort este \n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;
        t_start = clock();
        MergeSort();
        t_stop = clock();
        seconds3 = (double)(t_stop-t_start)/CLOCKS_PER_SEC;

        cout <<"\nArray-ul sortat pentru Mergesort:\n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<"\nTimpul de executie al programului pentru metoda Merge Sort este " <<setprecision(8)<<seconds3<< "s\n";

///-----------------------------------------------------

        index_baze=1;
        while(index_baze<=nrbaze)
        {
            cout<<"\nMetoda de sortare Radix Sort cu baza "<<baza[index_baze]<<endl;

            memcpy(v,w,sizeof(w));
            cout << "Array-ul nesortat pentru Radix Sort cu baza "<<baza[index_baze]<<" este \n";
            for(i=0; i<n; i++)
                cout << v[i] << " ";
            cout<<endl;
            t_start = clock();
            RadixSort_baze(baza[index_baze]);
            t_stop = clock();
            seconds4[index_baze] = (double)(t_stop-t_start)/CLOCKS_PER_SEC;

            cout << "Array-ul nesortat pentru Radix Sort cu baza "<<baza[index_baze]<<" este \n";
            for(i=0; i<n; i++)
                cout << v[i] << " ";

            cout<<"\nTimpul de executie al programului pentru metoda Radix Sort baza "<<baza[index_baze]<<" este "<<setprecision(8)<< seconds4[index_baze]<< "s\n";
            index_baze++;

        }
///-----------------------------------------------------

        cout<<"\nMetoda de sortare Quick Sort stabilind ca pivot ultimul element"<<endl;

        memcpy(v,w,sizeof(w));
        cout << "Array-ul nesortat pentru Quick Sort stabilind ca pivot ultimul element este \n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;
        t_start = clock();
        quickSort1(0,n-1);
        t_stop = clock();
        seconds5 = (double)(t_stop-t_start)/CLOCKS_PER_SEC;
        cout <<"\nArray-ul sortat pentru Quick Sort stabilind ca pivot ultimul element este:\n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<"\nTimpul de executie al programului pentru metoda Quick Sort stabilind ca pivot ultimul element este " <<setprecision(20)<<seconds5<< "s\n";

///-----------------------------------------------------

        cout<<"\nMetoda de sortare Quick Sort stabilind ca pivot mediana din 3"<<endl;

        memcpy(v,w,sizeof(w));
        cout << "Array-ul nesortat pentru Quick Sort stabilind pivot mediana din 3 este \n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;

        t_start = clock();
        quickSort2(0,n-1);
        t_stop = clock();
        seconds6 = 1.0* (double)(t_stop-t_start)/CLOCKS_PER_SEC;
        cout <<"\nArray-ul sortat pentru Quick Sort stabilind ca pivot mediana din 3 este:\n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<"\nTimpul de executie al programului pentru metoda Quick Sort stabilind ca pivot mediana din 3 este " << setprecision(20)<<seconds6<< "s\n";

        memcpy(v,w,sizeof(w));
        cout << "Array-ul nesortat pentru Radix Sort pe biti stabilind pivot mediana din 3 este \n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;

        t_start = clock();
        radix_sort_biti(v,n, nrcif);
        t_stop = clock();
        seconds7= 1.0* (double)(t_stop-t_start)/CLOCKS_PER_SEC;
        cout <<"\nArray-ul sortat pentru Radix Sort pe biti este:\n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<"\nTimpul de executie al programului pentru metoda Radix Sort pe biti " << setprecision(20)<<seconds7<< "s\n";

///-----------------------------------------------------

        memcpy(v,w,sizeof(w));
        cout << "Array-ul nesortat pentru functia nativa sort \n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<endl;

        t_start = clock();
        sort(v,v+n);
        t_stop = clock();
        seconds8= 1.0* (double)(t_stop-t_start)/CLOCKS_PER_SEC;
        cout <<"\nArray-ul sortat pentru functia nativa sort:\n";
        for(i=0; i<n; i++)
            cout << v[i] << " ";
        cout<<"\nTimpul de executie al programului pentru functia nativa sort " << setprecision(20)<<seconds8<< "s\n";

        cout<<endl;

///-----------------------------------------------------
        cout<<"IN CONCLUZIE, TIMPII DE EXECUTIE PENTRU TESTUL "<<nrtest-1<<" sunt\n";
        cout<<"\nNumarul de elemente este: "<<n<<".\n";
        cout<<"\nNumarul de maxim care poate fi in array este : "<<nrmaxi<<".\n";
        if(tip==1)
            cout<<"\nTipul de test este pentru un array sortat crescator.\n";
        if(tip==2)
            cout<<"\nTipul de test este pentru un array sortat descrescator.\n";
        if(tip==3)
            cout<<"\nTipul de test este pentru un array constant.\n";
        if(tip==4)
            cout<<"\nTipul de test este pentru un array generat random.\n";
        cout<<"\nSortarea Radix a fost facuta pentru "<<nrbaze<<" baze.\n";

        cout<<"\nTimpul de executie al programului pentru metoda Bubble Sort este " <<setprecision(20)<<seconds1<<"s\n";
        cout<<"\nTimpul de executie al programului pentru metoda Counting Sort este " <<setprecision(20)<<seconds2<< "s\n";
        cout<<"\nTimpul de executie al programului pentru metoda Merge Sort este " <<setprecision(20)<<seconds3<< "s\n";
        index_baze=1;
        while(index_baze<=nrbaze)
        {
            cout<<"\nTimpul de executie al programului pentru metoda Radix Sort baza "<<baza[index_baze]<<" este "<<setprecision(8)<< seconds4[index_baze]<< "s\n";
            index_baze++;
        }
        cout<<"\nTimpul de executie al programului pentru metoda Quick Sort stabilind ca pivot ultimul element este " <<setprecision(20)<<seconds5<< "s\n";
        cout<<"\nTimpul de executie al programului pentru metoda Quick Sort stabilind ca pivot mediana din 3 este " << setprecision(20)<<seconds6<< "s\n";
        cout<<"\nTimpul de executie al programului pentru metoda Radix Sort pe biti este " <<setprecision(20)<< seconds7<< "s\n";
        cout<<"\nTimpul de executie al programului pentru functia nativa sort " << setprecision(20)<<seconds8<< "s\n";

    }

    getchar();

    return 0;
}
