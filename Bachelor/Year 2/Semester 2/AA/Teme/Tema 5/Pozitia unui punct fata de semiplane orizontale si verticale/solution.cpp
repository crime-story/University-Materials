#include <iostream>
#include <iomanip>

using namespace std;
const double inf = 1e9;

struct Plan {
    double a, b, c;
};

bool isInPlan(double ai, double bi, double ci, double x, double y) {
    return ai * x + bi * y + ci <= 0;
}

int main() {
    int n;
    cin >> n;
    Plan plan[n];
    for (int i = 0; i < n; i++)
        cin >> plan[i].a >> plan[i].b >> plan[i].c;
    int m;
    cin >> m;
    for (int i = 0; i < m; i++) {
        double x, y;
        cin >> x >> y;
        double xmax = -inf;
        double xmin = inf;
        double ymax = inf;
        double ymin = -inf;
        for (int j = 0; j < n; j++) {
            if (isInPlan(plan[j].a, plan[j].b, plan[j].c, x, y)) {
                if (plan[j].b == 0) { // if is vertical
                    double aux = -plan[j].c / plan[j].a;
                    if (plan[j].a > 0) {
                        if (aux > x) {
                            xmin = min(xmin, aux);
                        }
                    } else {
                        if (aux < x) {
                            xmax = max(xmax, aux);
                        }
                    }
                } else {
                    double aux = -plan[j].c / plan[j].b;
                    if (plan[j].b > 0) {
                        if (aux > y) {
                            ymax = min(ymax, aux);
                        }
                    } else {
                        if (aux < y) {
                            ymin = max(ymin, aux);
                        }
                    }
                }
            }
        }
        if (xmax == -inf || xmin == inf || ymax == inf || ymin == -inf)
            cout << "NO\n";
        else
            cout << "YES\n" << fixed << setprecision(6) << (xmin - xmax) * (ymax - ymin) << '\n';
    }
    return 0;
}