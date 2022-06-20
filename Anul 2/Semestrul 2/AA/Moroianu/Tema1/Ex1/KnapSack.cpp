// Please Check PDF file for details.

#include <bits/stdc++.h>
using namespace std;

int SumaMax(vector<int> w, int K)
{
    vector<int> viz(K + 1);
    viz[0] = 1;
    for (auto i : w)
        for (int j = K; j >= i; j--)
            viz[j] |= viz[j - i];
    int ans = K;
    while (!viz[ans])
        ans--;
    return ans;
}

int main()
{
    int N;
    cout << "Please enter number of elements: ";
    cin >> N;
    
    vector <int> v(N);
    for (int i = 0; i < N; i++) {
        cout << "Please enter element #" << i << ": ";
        cin >> v[i];
    }

    int W;
    cout << "Please enter weight of the knapsack: ";
    cin >> W;

    cout << "Maximal load: " << SumaMax(v, W) << '\n';
}