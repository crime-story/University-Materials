#include <fstream>

using namespace std;
ifstream f("alibaba.in");
ofstream g("alibaba.out");

int n, k, i, poz, poz1, v[10010], j, maxi,aux, aux2;
char c;

int main()
{
    f>>n>>k;
    for (i = 1; i <= n; ++i)
    {
        f>>c,v[i] = (int)(c - 48);
    }
    poz = 1;
    aux = n - k;
    for (i = 1; i <= aux; i++)
    {
        maxi = 0;
        aux2 = poz + k;

        for (j = poz; j <= aux2 && j <= n; ++j)
        {
            if (v[j] > maxi)
            {
                maxi = v[j];
                poz1 = j;
            }
        }
        g<<maxi;
        k = k- (poz1 - poz);
        poz = poz1 + 1;
    }
    return 0;
}