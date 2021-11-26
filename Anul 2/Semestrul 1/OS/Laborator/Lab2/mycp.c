#include <stdio.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>

int openFisierIntrare(char *numeFisier) {
    int fin = open(numeFisier, O_RDONLY);
    if (fin == -1) {
        printf("Eroare la deschiderea fisierului de intrare!\n");
        return -1;
    }
    return fin;
}

int openFisierIesire(char *numeFisier) {
    int fout = open(numeFisier, O_WRONLY | O_CREAT | O_TRUNC, S_IRWXU);
    if (fout == -1) {
        printf("Eroare la deschiderea fisierului de iesire!\n");
        return -1;
    }
    return fout;
}

int main(int argc, char **argv) {
    if (argc != 3) {
        printf("Formatul este gresit!\nFormatul trebuie sa fie: ./mycp <intrare> <iesire>\n");
        return 0;
    }
    char *fisierIntrare = argv[1], *fisierIesire = argv[2];
    int fin = openFisierIntrare(fisierIntrare);
    /*
    // Ma gandisem initial ca nu pot copia "nimic" in fisierul de output, aici:
    // dar dupa mi-am dat seama ca ar fi mai ok asa, pentru ca in cerinta se cere
    // practic ca fisierele sa fie identice

    // De exemplu daca as fi avut acum un fisier care nu era gol ar fi mers
    // dar dupa daca il goleam si rulam din nou, in fisierul de output ramanea output-ul de la input-ul anterior(nevid)
    stat(fisierIntrare, &sb);
    int dimensiuneFisierIntrare = sb.st_size;
    if (dimensiuneFisierIntrare == 0) {
        printf("Fisierul de intrare este gol!\n");
        return 0;
    }
    */
    int fout = openFisierIesire(fisierIesire);
    const int max = 1024;
    char *continut = malloc(sizeof(char) * max);
    int nrBytes = read(fin, continut, max);
    write(fout, continut, nrBytes);
    free(continut);
    close(fin);
    close(fout);
    return 0;
}