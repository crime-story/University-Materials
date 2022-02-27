package Exercitii;

import java.util.Scanner;

public class Exercitiul3 {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Introduceti n: ");
        int n = keyboard.nextInt(), factorial = 1, aux = n;

        // Varianta 1 cu while
        while (n != 0) {
            factorial *= n;
            n -= 1;
        }
        System.out.println("Factorial de " + aux + " este: " + factorial);

        /*
        // Varianta 2 cu do while
        do {
            factorial *= n;
            n -= 1;
        } while (n != 0);
        System.out.println("Factorial de " + aux + " este: " + factorial);
        */

    }
}
