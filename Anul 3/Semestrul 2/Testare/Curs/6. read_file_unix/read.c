#include <stdlib.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <unistd.h>
#include <assert.h>
#include <sys/time.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <string.h>
#include <stdio.h>


ssize_t read_fi (int fildes, void *buf, size_t nbyte)
{
	nbyte = (rand() % nbyte) + 1;
	return read (fildes, buf, nbyte);
}

ssize_t read_all (int fildes, void *buf, size_t nbyte)
{
	assert (fildes >= 0);
	assert (buf);
	assert (nbyte >= 0);
	size_t left = nbyte;
	while (1) {
		int res = read (fildes, buf, left);
		// printf ("%d\n", res);
		if (res < 1)
			return res;
		buf += res;
		left -= res;
		assert (left >= 0);
		if (left == 0)
			return nbyte;
	}
}

int main (void)
{
	srand(time(NULL));

	int fd = open ("splay.py", O_RDONLY);
	assert (fd >= 0);

	struct stat buf;
	int res = fstat (fd, &buf);
	assert (res == 0);

	off_t len = buf.st_size;
	char *definitive = (char *) malloc (len);
	assert (definitive);

	res = read (fd, definitive, len);
	assert (res == len);

	int i;
	char *test = (char *) malloc (len);
	for (i=0; i<100; i++) {
		res = lseek (fd, 0, SEEK_SET);
		assert (res == 0);
		int j;
		for (j=0; j<len; j++) {
			test[j] = rand();
		}
		res = read_all (fd, test, len);
		assert (res == len);
		assert (strncmp(test, definitive, len) == 0);
	}

	return 0;
}