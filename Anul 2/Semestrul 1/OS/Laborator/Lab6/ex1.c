#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>

void *reverseString(void *v){
    char *originalString = (char *)v;
    int lengthString = strlen(originalString);
    char *reversedString = malloc(sizeof(char) * lengthString);
    for (int i = 0; i < lengthString; i++) 
        reversedString[i] = originalString[lengthString - i - 1];
    return reversedString;
}

int main(int argc, char* argv[])
{
    if(argc != 2){
        printf("Enter only the string to reverse");
        return 1;
    }
    pthread_t thr;
    void *result;
    if (pthread_create (&thr, NULL, reverseString, argv[1])) {
        perror(NULL);
        return errno;
    }
    if (pthread_join(thr, &result)) {
        perror(NULL);
        return errno;
    }
    printf("%s\n", (char*)result);
    free(result);
    return 0;
}