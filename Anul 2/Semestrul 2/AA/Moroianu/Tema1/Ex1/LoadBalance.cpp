// Please Check PDF file for details.

#include <bits/stdc++.h>
using namespace std;

// To run the file we need the "eof" character.
// easiest way to do it is by piping the input.
// e.g.: $ echo "5 1 2 3 4" | ./loadbalance

int main()
{
    int K, ans = 0, act;
    cin >> K;
    while (cin >> act)
    {
        ans += act;
        if (ans > K)
            ans -= act;
        else if (ans < act)
            ans = act;
    }
    cout << "Answer is " << ans << '\n';
}
