#include <pthread.h>
#include <errno.h>
#include <semaphore.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>


typedef struct {
    int patient_id;      ///position in *patients
    int consultation_duration;
    pthread_t pthread;   ///thread id
} patient_t;

time_t time0;           ///time of starting the program
sem_t doctors;
pthread_mutex_t mtx;

patient_t **patients;
int max_nr_of_patients;
int nr_of_active_patients;

pthread_t *stack_of_finished_threads;   /// we call pthread_join "on the go"
int max_nr_of_elements_in_finished_threads_stack;
int nr_of_elements_in_finished_threads_stack;

void *consultatie(void *v_patient) {
    patient_t *patient = (patient_t *) v_patient;
    time_t arrive_time = time(NULL) - time0;
    if (sem_wait(&doctors)) {      /// I wait for a doctor to consult me
        perror(NULL);
        pthread_cancel(pthread_self());  ///If it fails, I close the current thread
    }
    time_t time_when_entering_consultation = time(NULL) - time0;
    time_t consultation_duration = patient->consultation_duration;
    sleep(consultation_duration);   /// The consultation :))
    if (sem_post(&doctors)) {        ///Free the doctor to consult another patient
        perror(NULL);
        pthread_cancel(pthread_self());
    }
    time_t time_of_leaving = time(NULL) - time0;

    pthread_mutex_lock(&mtx); ///I don't want another threads to print information + race condition of the stack
    printf("\nPatient %d waited for %ld seconds and had a consultation of %ld seconds\n", patient->patient_id,
           time_when_entering_consultation - arrive_time,
           consultation_duration);
    printf("Time of arrival: %ld\n", arrive_time);
    printf("Time of consultation beginning: %ld\n", time_when_entering_consultation);
    printf("Time of leaving: %ld\n", time_of_leaving);

    if (nr_of_elements_in_finished_threads_stack >= max_nr_of_elements_in_finished_threads_stack) { ///alocate more elements on stack
        max_nr_of_elements_in_finished_threads_stack *= 2;
        stack_of_finished_threads = realloc(stack_of_finished_threads,
                                            max_nr_of_elements_in_finished_threads_stack * sizeof(pthread_t));
    }
    stack_of_finished_threads[nr_of_elements_in_finished_threads_stack++] = pthread_self(); ///to be used for thread_join
    pthread_mutex_unlock(&mtx);
    free(patient);
    return NULL;
}

void join_threads(bool force_join_of_all_threads) {
    while (nr_of_active_patients > 0) {
        pthread_mutex_lock(&mtx);
        bool display_info = false;
        while (nr_of_elements_in_finished_threads_stack > 0) {
            display_info = true;
            void *result;
            pthread_t pthread = stack_of_finished_threads[--nr_of_elements_in_finished_threads_stack];
            if (pthread_join(pthread, &result)) {
                perror(NULL);
            }
            nr_of_active_patients--;
        }
        if (display_info) {
            printf("\nPatients Left: %d\n", nr_of_active_patients);
            int nr_of_free_doctors;
            if (sem_getvalue(&doctors, &nr_of_free_doctors)) {
                perror(NULL);
            } else
                printf("Free Doctors: %d\n", nr_of_free_doctors);
        }
        pthread_mutex_unlock(&mtx);
        if (!force_join_of_all_threads)
            break;
    }
}

int main(int argc, char *argv[]) {
    ///citire valori utilizate in program

    time_t max_time_before_new_doctor = 32;
    time_t max_time_before_doctor_leaving = 64;
    time_t max_time_before_new_patient = 8;
    time_t max_time_of_consultation = 16;
    if (argc >= 2) {
        max_time_before_new_doctor = atoi(argv[1]);
    }
    if (argc >= 3) {
        max_time_before_doctor_leaving = atoi(argv[2]);
    }
    if (argc >= 4) {
        max_time_before_new_patient = atoi(argv[3]);
    }
    if (argc >= 5) {
        max_time_of_consultation = atoi(argv[4]);
    }

    patients = calloc(1, sizeof(patient_t *));
    max_nr_of_patients = 1;

    stack_of_finished_threads = calloc(1, sizeof(pthread_t));
    max_nr_of_elements_in_finished_threads_stack = 1;

    printf("Max number of seconds before new doctor: %ld\n", max_time_before_new_doctor);
    printf("Max number of seconds before a doctor leave: %ld\n", max_time_before_doctor_leaving);
    printf("Max number of seconds before new patient: %ld\n", max_time_before_new_patient);
    printf("Max number of seconds for a consultation: %ld\n", max_time_of_consultation);

    /// setare comportament program

    int flags = fcntl(0, F_GETFL, 0);  ///iau argumentele care erau setate pt fd 0 (read din stdin)
    fcntl(0, F_SETFL, flags | O_NONBLOCK); ///setez pt read din stdin argumentele care erau setat + nonblocking reading

    srand(time(NULL));     ///seed for random values

    if (sem_init(&doctors, 0, 0)) {
        perror(NULL);
        return errno;
    }

    if (pthread_mutex_init(&mtx, NULL)) {
        perror(NULL);
        return errno;
    }


    ///setare valori initiale pentru variabilele din program

    time0 = time(NULL);
    time_t time_of_last_added_doctor = time0;
    time_t time_of_last_added_patient = time0;
    time_t time_of_last_doctor_leaving = time0;
    time_t time_before_new_doctor = 0;
    time_t time_before_new_patient = 0;
    time_t time_before_doctor_leaving = max_time_before_doctor_leaving;

    ///program

    while (true) {
        time_t time1 = time(NULL);
        if (time1 - time_of_last_added_doctor >= time_before_new_doctor) {
            if (sem_post(&doctors)) {
                perror(NULL);
            }
            printf("\nNew Doctor Arrived!\n");
            time_of_last_added_doctor = time1;
            time_before_new_doctor = rand() % max_time_before_new_doctor + 1;
        }

        if (time1 - time_of_last_added_patient >= time_before_new_patient) {
            if (nr_of_active_patients >= max_nr_of_patients) {
                max_nr_of_patients *= 2;
                patients = realloc(patients, max_nr_of_patients * sizeof(patient_t *));
            }

            patient_t *patient = malloc(sizeof(patient_t));
            patient->consultation_duration = rand() % max_time_of_consultation + 1; //between [1, max...]
            patient->patient_id = nr_of_active_patients;  ///index in vector; all patients in the clinic
            if (pthread_create(&patient->pthread, NULL, consultatie, patient)) {
                perror(NULL);
                return errno;
            }
            patients[nr_of_active_patients++] = patient;
            printf("\nNew Patient Arrived!\n");
            time_of_last_added_patient = time1;
            time_before_new_patient = rand() % max_time_before_new_patient + 1;
        }

        if (time1 - time_of_last_doctor_leaving >= time_before_doctor_leaving) {///Doctor leaving for personal reasons
            int nr_of_free_doctors;
            if (sem_getvalue(&doctors, &nr_of_free_doctors)) { ///number of doctors that are not consulting someone
                perror(NULL);
            } else if (nr_of_free_doctors > 0) {
                sem_trywait(&doctors); /// try to decrease number of doctors without blocking the main thread
                printf("\nDoctor Leaved!\n");
                time_of_last_doctor_leaving = time1;
                time_before_doctor_leaving = rand() % max_time_before_doctor_leaving + 1;
            }
        }

        join_threads(false);

        char ch[5] = {0};
        read(0, ch, 4);
        if (strcmp(ch, "stop") == 0)
            break;
    }

    printf("\nInitializing End Of Simulation...");
    join_threads(true);

    pthread_mutex_destroy(&mtx);
    sem_destroy(&doctors);
    free(patients);
    free(stack_of_finished_threads);

    printf("Simulation ended after %ld seconds!\n", time(NULL) - time0);
    return 0;
}
