# Laborator 6 (07.11.2022)

### Notite Tabla Laborator:
![WhatsApp Image 2022-11-22 at 00 46 20](https://user-images.githubusercontent.com/73616883/203173570-de947745-bdcc-4a55-bdce-f41625803a58.jpg)
![WhatsApp Image 2022-11-22 at 00 46 20](https://user-images.githubusercontent.com/73616883/203173583-ddd3640a-522b-444f-8bfd-20f8164d951d.jpg)
![WhatsApp Image 2022-11-22 at 00 46 21](https://user-images.githubusercontent.com/73616883/203173597-136c80cd-68bd-456b-9cd9-6e9dff7f2537.jpg)

### Exercitiul 1

```cpp
#include <bits/stdc++.h>
using namespace std;

bool CompareStates(deque<int> c1, deque<int>c2){
    for(int i = 0; i <= c1.size(); i++){
        if(c1[i] != c2[i]){
            return true;
        }
    }
    return false;
}

int main()
{
    int n;
    cout << "Introdu numarul de biti: ";
    cin >> n;
    deque <int> c(n), s(n);
    cout << "Biti:\n";
    for (int i = 0; i < n; i++) {
        cout << "bit no"<< i + 1 << " = ";
        cin >> c[i];
    }

    cout << "Initial state:\n";
    for (int i = 0; i < n; i++) {
        cout << "bit no" << i  << "-";
        cin >> s[i];
    }
    int xorNum;
    cout << "Numarul de coeficienti pentru xor: ";
    cin >> xorNum;
    vector<int> xorCoef;
    for(int i = 0; i < xorNum; i++){
        cout << "Coef no" << i+1 << ": ";
        int x;
        cin >>  x;
        xorCoef.push_back(x);
    }
    while(CompareStates(c,s)){
        int x = c[0];
        for(int i = 0 ; i < xorCoef.size(); i++){
            x = x ^ c[xorCoef[i]];
        }
        cout << x << " ";
        c.pop_front();
        c.push_back(x);
    }
    cout << "\nFinal result: ";
    for(int i = 0; i < s.size(); i++){
        cout << s[i] << " ";
    }
}
```

## ❗ Exercitiile 2 si 3 sunt in jupyter notebook ❗
