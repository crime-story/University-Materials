// Popescu Paullo Robertto Karloss Grupa 231
// Laborator 5

#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>

int functie(int n, int *shm_ptr, int index) {
	shm_ptr[index] = n;
	if (n != 1) {
		if (n % 2 == 1) {
			functie(3 * n + 1, shm_ptr, index + 1);
		}
		else {
			functie(n / 2, shm_ptr, index + 1);
		}
	}
	else {
		shm_ptr[index] = 1;
		return index;
	}
}

int main(int argc, char* argv[]) {
	if (argc < 2) {
		printf("Formatul este gresit!\nFormatul trebuie sa fie: ./lab5 <lista_numere>\n");
		return 0;
	}
	char shm_name[] = "lab5";
	int shm_fd = shm_open(shm_name, O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);
	if (shm_fd < 0) {
		perror(NULL);
		return errno;
	}
	size_t shm_size = getpagesize()*argc;
	if (ftruncate(shm_fd, shm_size) == -1) {
		perror(NULL);
		shm_unlink(shm_name);
		return errno;
	}
	int lungime;
	printf("Starting parent %d\n", getppid());
	for (int i = 1; i < argc; i++) {
		pid_t x = fork();
		if (x < 0)
			return errno;
		else if(x == 0) {
			int *shm_ptr = mmap(0, getpagesize(), PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, getpagesize()*i);
			if (shm_ptr == MAP_FAILED) {
				perror(NULL);
				shm_unlink(shm_name);
				return errno;
			}
			int numar = atoi(argv[i]);
			shm_ptr[0] = numar;
			lungime = functie(numar, shm_ptr, 1); // salvez lungimea secventei de ex pentru 16 se afiseaza 5 valori, 
												// deci lungimea rezultatelor este 5
			shm_ptr[lungime + 1] = -1; // am pus cu -1 ultima pozitie ca sa stiu unde ma opresc
			printf("Done Parent %d Me %d\n", getppid(), getpid());
			exit(0); // se termina copilul
		}
	}
	for (int i = 1; i < argc; i++)
		wait(NULL);
	for (int i = 1; i < argc; i++) {
		int *shm_ptr = mmap(0, getpagesize(), PROT_WRITE, MAP_SHARED, shm_fd, getpagesize()*i);
		int indice = 1;
		printf("%d: ", shm_ptr[0]);
		while (shm_ptr[indice] != -1){
			printf("%d ", shm_ptr[indice]);
			indice++;
		}
		printf("\n");
	}
	shm_unlink(shm_name);
	munmap(0, shm_size);
	return 0;
}