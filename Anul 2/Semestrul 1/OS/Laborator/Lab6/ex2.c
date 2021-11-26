#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <pthread.h>

#define NMAX 100

int A[NMAX][NMAX], B[NMAX][NMAX], C[NMAX][NMAX];

int numberOfRowsMatrixA, numberOfColumnsMatrixA;
int numberOfRowsMatrixB, numberOfColumnsMatrixB;
int numberOfRowsMatrixC, numberOfColumnsMatrixC;

int read() {
    printf("Enter the number of rows and columns for the matrix A:\n");
    scanf("%d %d", &numberOfRowsMatrixA, &numberOfColumnsMatrixA);
    printf("\nEnter the elements of the matrix A:\n");
    for (int i = 0; i < numberOfRowsMatrixA; i++)
        for (int j = 0; j < numberOfColumnsMatrixA; j++)
            scanf("%d", &A[i][j]);
    
    printf("Enter the number of rows and columns for the matrix B:\n");
    scanf("%d %d", &numberOfRowsMatrixB, &numberOfColumnsMatrixB);

    if (numberOfColumnsMatrixA != numberOfRowsMatrixB)
        return -1;
    
    printf("\nEnter the elements of the matrix B:\n");
    for (int i = 0; i < numberOfRowsMatrixB; i++)
        for (int j = 0; j < numberOfColumnsMatrixB; j++)
            scanf("%d", &B[i][j]);
    return 0;
}

void *computeElem(void *v) {
    int *arguments = (int*) v;
    int rowIndexOfMatrixA = arguments[0];
    int columnIndexOfMatrixB = arguments[1];
    int *element = malloc(sizeof(int));
    *element = 0;
    for (int i = 0; i < numberOfRowsMatrixB; i++)
        *element += A[rowIndexOfMatrixA][i] * B[i][columnIndexOfMatrixB];
    return element;
}

int main() {
    if (read() != 0) {
        printf("Matrix A cannot be multiplied by Matrix B!\n");
        printf("Reason: the number of columns of the Matrix A is different from the number of rows of the Matrix B.\n");
        return -1;
    }

    // We know that for Matrix C, the number of rows is equal to the number of rows of Matrix A, 
    // and the number of columns is equal to the number of columns of Matrix B

    numberOfRowsMatrixC = numberOfRowsMatrixA;
    numberOfColumnsMatrixC = numberOfColumnsMatrixB;

    pthread_t thrs[numberOfRowsMatrixC * numberOfColumnsMatrixC]; // thread for each matrix element
    int thrIndex = 0;
    for (int i = 0; i < numberOfRowsMatrixC; i++) {
        for (int j = 0; j < numberOfColumnsMatrixC; j++) {
            int *args = malloc(sizeof(int) * 2);
            args[0] = i; // row index of Matrix A
            args[1] = j; // column index of Matrix B
            if (pthread_create(&thrs[thrIndex], NULL, computeElem, args)) {
                perror(NULL);
                return errno;
            }
            thrIndex++;
        }
    }
    thrIndex = 0;
    for (int i = 0; i < numberOfRowsMatrixC; i++) {
        for (int j = 0; j < numberOfColumnsMatrixC; j++) {
            void *result;
            if (pthread_join(thrs[thrIndex], &result)) {
                perror(NULL);
                return errno;
            }
            C[i][j] = *((int*)result);
            thrIndex++;
        }
    }
    printf("\nMatrix C, following the multiplication of the two matrices:\n");
    for (int i = 0; i < numberOfRowsMatrixC; i++) {
        for (int j = 0; j < numberOfColumnsMatrixC; j++)
            printf("%d ", C[i][j]);
        printf("\n");
    }
    return 0;
}