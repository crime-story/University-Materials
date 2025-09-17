#include <fstream>

using namespace std;

ifstream f("vila2.in");
ofstream g("vila2.out");

int n, k;
int v[100001];
long long s = 0;
int coada1[100001];
int coada2[100001];
int st1 = 1, dr1 = 0;
int st2 = 1, dr2 = 0;
int maxi;
int main()
{
    f>>n>>k;
    k++;
    for (int i = 1; i <= n; i++)
        f>>v[i];


    for (int i = 1; i <= n; i++)
    {
        while (st1 <= dr1 && v[i]<=v[coada1[dr1]])
            dr1--;

        coada1[++dr1] = i;

        if (coada1[st1] == i - k)
            st1++;

        while (st2 <= dr2 && v[i]>=v[coada2[dr2]])
            dr2--;

        coada2[++dr2] = i;

        if (coada2[st2] == i - k)
            st2++;
        if(k<=i)
        {
            if(v[coada2[st2]] - v[coada1[st1]] > maxi)
                maxi=v[coada2[st2]] - v[coada1[st1]];
        }
    }

    g<<maxi;
    return 0;
}