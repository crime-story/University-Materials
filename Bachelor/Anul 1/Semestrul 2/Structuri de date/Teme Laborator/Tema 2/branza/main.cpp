#include <fstream>

using namespace std;

//#define Nmax 100100
//#define v(r) ((long long)c[r] + (n-r)*s)
ifstream f("branza.in");
ofstream g("branza.out");

int c[100100], p[100100], w[100100];
int st,dr;
long long rez=0;
int n,s,t;
long long  functie(int r)
{
    return c[r] + (n-r)*s;
}

int main()
{
    f>>n>>s>>t;

    for (int i=1; i<=n; i++)
        f>>c[i]>>p[i];

    st=dr=1;
    c[0] = 2114567890;


    for (int i=1; i<=n; ++i)
    {
        while(functie(w[dr]) > functie(i) && st<=dr)
            dr--;
        w[++dr] = i;
        rez += (functie(w[st]) - (n-i)*s)*p[i];
        if(w[st] == i-t) ++st;
    }

    g<<rez;
    return 0;
}