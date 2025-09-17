#include <fstream>

using namespace std;

ifstream f("deque.in");
ofstream g("deque.out");

int n, k;
int v[5000001];
long long s = 0;
int coada[5000001];
int st = 1, dr = 0;

int main()
{
    f>>n>>k;
    for (int i = 1; i <= n; i++)
        f>>v[i];


    for (int i = 1; i <= n; i++)
    {
        while (st <= dr && v[i]<=v[coada[dr]])
            dr--;

        coada[++dr] = i;

        if (coada[st] == i - k)
            st++;

        if (k<=i)
            s = s+ v[coada[st]];
    }

    g<<s;
    return 0;
}