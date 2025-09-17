#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>
#include <stdlib.h>

void functie(int n) {
	if (n != 1) {
		printf("%d ", n);
		if (n % 2 == 1) {
			functie(3 * n + 1);
		}
		else {
			functie(n / 2);
		}
	}
	else
		printf("%d\n", 1);
}

int main(int argc, char* argv[]) {
	if(argc < 2) {
		printf("Formatul este gresit!\nFormatul trebuie sa fie: ./ncollatz <lista_numere>\n");
		return 0;
	}
	printf("Starting parent %d\n", getppid());
	for(int i = 1; i < argc; i++) {
		pid_t x = fork();
		if(x < 0)
			return errno;
		if(x == 0) {
			int numar = atoi(argv[i]);
			printf("%d: ", numar);
			functie(numar);
			printf("Done Parent %d Me %d\n", getppid(), getpid());
			exit(0); // se termina copilul
		}
	}
	for(int i = 1; i < argc; i++)
		wait(NULL);
	printf("Done Parent %d Me %d\n", getppid(), getpid());
	return 0;
}