// Using struct for each element from matrix C
// and malloc to allocate memory for each matrix

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <errno.h>

int **A, **B, **C;

struct element {
    int row, column;
};


int numberOfRowsMatrixA, numberOfColumnsMatrixA;
int numberOfRowsMatrixB, numberOfColumnsMatrixB;
int numberOfRowsMatrixC, numberOfColumnsMatrixC;

int read() {
    printf("Enter the number of rows and columns for the matrix A:\n");
    scanf("%d %d", &numberOfRowsMatrixA, &numberOfColumnsMatrixA);

    A = (int**)malloc(numberOfRowsMatrixA*sizeof(int*));
    for (int i = 0; i < numberOfRowsMatrixA; i++)
        A[i] = (int*)malloc(numberOfColumnsMatrixA*sizeof(int));

    printf("\nEnter the elements of the matrix A:\n");
    for (int i = 0; i < numberOfRowsMatrixA; i++)
        for (int j = 0; j < numberOfColumnsMatrixA; j++)
            scanf("%d", &A[i][j]);
    
    printf("Enter the number of rows and columns for the matrix B:\n");
    scanf("%d %d", &numberOfRowsMatrixB, &numberOfColumnsMatrixB);

    B = (int**)malloc(numberOfRowsMatrixB*sizeof(int*));
    for (int i = 0; i < numberOfRowsMatrixB; i++)
        B[i] = (int*)malloc(numberOfColumnsMatrixB*sizeof(int));    

    if (numberOfColumnsMatrixA != numberOfRowsMatrixB)
        return -1;
    
    printf("\nEnter the elements of the matrix B:\n");
    for (int i = 0; i < numberOfRowsMatrixB; i++)
        for (int j = 0; j < numberOfColumnsMatrixB; j++)
            scanf("%d", &B[i][j]);
    return 0;
}

void *computeElem(void *v) {
    int row = ((struct element*)v)->row;
    int column = ((struct element*)v)->column;
    int *element = malloc(sizeof(int));
    *element = 0;
    for (int i = 0; i < numberOfRowsMatrixB; i++)
        *element += A[row][i] * B[i][column];
    return element;
}

int main() {
    if (read() != 0) { // if we have a problem during reading we stop
        printf("Matrix A cannot be multiplied by Matrix B!\n");
        printf("Reason: the number of columns of the Matrix A is different from the number of rows of the Matrix B.\n");
        return -1;
    }
    
    // We know that for Matrix C, the number of rows is equal to the number of rows of Matrix A, 
    // and the number of columns is equal to the number of columns of Matrix B

    numberOfRowsMatrixC = numberOfRowsMatrixA;
    numberOfColumnsMatrixC = numberOfColumnsMatrixB;

    C = (int**)malloc(numberOfRowsMatrixC*sizeof(int*));
    for (int i = 0; i < numberOfRowsMatrixC; i++)
        C[i] = (int*)malloc(numberOfColumnsMatrixC*sizeof(int));  

    pthread_t thrs[numberOfRowsMatrixC * numberOfColumnsMatrixC]; // thread for each matrix element
    struct element* args = malloc(numberOfRowsMatrixC * numberOfColumnsMatrixC * sizeof(struct element));
    int thrIndex = 0;
    for (int i = 0; i < numberOfRowsMatrixC; i++) {
        for (int j = 0; j < numberOfColumnsMatrixC; j++) {
            args[thrIndex].row = i; // row index of Matrix A
            args[thrIndex].column = j; // column index of Matrix B
            if (pthread_create(&thrs[thrIndex], NULL, computeElem, &args[thrIndex])) {
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
            if (pthread_join(thrs[thrIndex], &result)) { // wait for each thread
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

    free(args);
    for (int i = 0; i < numberOfRowsMatrixC; i++)
        free(C[i]);
    free(C);
    return 0;
}