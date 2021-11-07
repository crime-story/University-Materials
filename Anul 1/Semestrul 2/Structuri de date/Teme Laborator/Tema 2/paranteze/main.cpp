#include <iostream>
#include <stack>
#include <fstream>
#include <string>
using namespace std;
ifstream in("paranteze.in");
ofstream out("paranteze.out");
stack <pair<int,char>> st;
int n;
string s;

int paranteze(char c)
{
    if(st.empty()!=0)
        return 0;
    if((c==')' && st.top().second=='('))
        return 1;
    if((c==']' && st.top().second=='['))
        return 1;
    if((c=='}' && st.top().second=='{'))
        return 1;
    return 0;

}
int main()
{
    st.push({-1, 'x'});

    in>>n>>s;
    int maxi=0;
    for(int i=0; i<n; i++)
        if(paranteze(s[i])==0)
            st.push({i,s[i]});
        else
        {
            st.pop();
            if(i-st.top().first>maxi)
                maxi=i-st.top().first;
        }

    out<<maxi;

    return 0;
}
