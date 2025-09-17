#include <iostream>

using namespace std;

const double inf = 1e9;

struct Plan {
    double a, b, c;
};

int main() {
    double xmax = -inf;
    double xmin = inf;
    double ymax = inf;
    double ymin = -inf;
    int n;
    cin >> n;
    Plan plan[n];
    for (int i = 0; i < n; i++) {
        cin >> plan[i].a >> plan[i].b >> plan[i].c;
        if (plan[i].b == 0) { // if is vertical
            double aux = -plan[i].c / plan[i].a;
            if (plan[i].a > 0)
                xmin = min(xmin, aux);
            else
                xmax = max(xmax, aux);
        } else {
            double aux = -plan[i].c / plan[i].b;;
            if (plan[i].b > 0)
                ymax = min(ymax, aux);
            else
                ymin = max(ymin, aux);
        }
    }
    if (xmax > xmin || ymax < ymin)
        cout << "VOID";
    else {
        if (xmax != -inf && xmin != inf && ymax != inf && ymin != -inf)
            cout << "BOUNDED";
        else
            cout << "UNBOUNDED";
    }
    return 0;
}