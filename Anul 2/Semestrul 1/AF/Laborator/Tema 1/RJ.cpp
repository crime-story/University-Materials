// Link: https://www.infoarena.ro/job_detail/2797659?action=view-source

#include <fstream>

using namespace std;

ifstream f("rj.in");
ofstream g("rj.out");

int n, m, xr, yr, xj, yj;
int dl[8] = {0, 1, 0, -1, -1, 1, -1, 1};
int dc[8] = {1, 0, -1, 0, -1, 1, 1, -1};
char l[102][102];
int r[102][102];
int j[102][102];

struct Punct {
    int l, c;
} C[102 * 102], p;


int main() {
    int i, k;
    char c;

    f >> n >> m;
    for (i = 0; i <= n + 1; i++)
        l[i][0] = l[i][m + 1] = 'X';
    for (i = 0; i <= m + 1; i++)
        l[0][i] = l[n + 1][i] = 'X';

    f.get();
    for (i = 1; i <= n; i++) {
        for (k = 1; k <= m; k++) {
            f.get(c);
            l[i][k] = c;
            if (l[i][k] == 'R') {
                xr = i;
                yr = k;
                l[i][k] = ' ';
            }
            if (l[i][k] == 'J') {
                xj = i;
                yj = k;
                l[i][k] = ' ';
            }
        }
        f.get();
    }


    int inc = 0, sf = 0;
    for (i = 0; i <= n + 1; i++)
        for (k = 0; k <= m + 1; k++)
            r[i][k] = 0;

    C[0].l = xr;
    C[0].c = yr;

    r[xr][yr] = 1;

    while (inc <= sf) {
        p = C[inc++];
        for (i = 0; i < 8; i++)
            if (l[p.l + dl[i]][p.c + dc[i]] == ' ' && r[p.l + dl[i]][p.c + dc[i]] == 0) {
                r[p.l + dl[i]][p.c + dc[i]] = 1 + r[p.l][p.c];
                C[++sf].l = p.l + dl[i];
                C[sf].c = p.c + dc[i];
            }
    }

    inc = 0, sf = 0;
    for (i = 0; i <= n + 1; i++)
        for (k = 0; k <= m + 1; k++)
            j[i][k] = 0;

    C[0].l = xj;
    C[0].c = yj;
    j[xj][yj] = 1;

    while (inc <= sf) {
        p = C[inc++];
        for (i = 0; i < 8; i++)
            if (l[p.l + dl[i]][p.c + dc[i]] == ' ' && j[p.l + dl[i]][p.c + dc[i]] == 0) {
                j[p.l + dl[i]][p.c + dc[i]] = 1 + j[p.l][p.c];
                C[++sf].l = p.l + dl[i];
                C[sf].c = p.c + dc[i];
            }
    }

    int tmin = 102 * 102;
    int xmin = -1;
    int ymin = -1;

    for (i = 1; i <= n; i++)
        for (k = 1; k <= m; k++)
            if (r[i][k] == j[i][k])
                if (r[i][k] < tmin && r[i][k] != 0) {
                    tmin = r[i][k];
                    xmin = i;
                    ymin = k;
                }
    g << tmin << " " << xmin << " " << ymin << endl;
    return 0;
}