#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>

int main() {
	pid_t x = fork();
	if (x < 0)
		return errno;
	else if (x == 0) {
		char *argv[] = {"ls", NULL};
		execve("/bin/ls", argv, NULL);
	}
	else {
		printf("My PID=%d, Child PID=%d.\n", getpid(), x);
		wait(NULL);
		printf("Child %d finished!\n", x);
	}
	return 0;
}