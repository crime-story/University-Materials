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
	if(argc != 2) {
		printf("Formatul este gresit!\nFormatul trebuie sa fie: ./collatz <numar>\n");
		return 0;
	}
	else {
		int numar = atoi(argv[1]);
		pid_t x = fork();
		if (x < 0)
			return errno;
		else if (x == 0) {
			printf("%d: ", numar);
			functie(numar);
		}
		else {
			wait(NULL);
			printf("Child %d finished!\n", x);
		}
	}
	return 0;
}
