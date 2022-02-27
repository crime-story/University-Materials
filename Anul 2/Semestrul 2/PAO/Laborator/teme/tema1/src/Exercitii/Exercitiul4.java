package Exercitii;

import java.util.Scanner;

public class Exercitiul4 {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Introduceti numele zilei: ");
        String ziua = keyboard.next().toLowerCase();
        switch (ziua) {
            case "luni":
                System.out.println("Ziua " + 1);
                break;
            case "marti":
                System.out.println("Ziua " + 2);
                break;
            case "miercuri":
                System.out.println("Ziua " + 3);
                break;
            case "joi":
                System.out.println("Ziua " + 4);
                break;
            case "vineri":
                System.out.println("Ziua " + 5);
                break;
            case "sambata":
                System.out.println("Ziua " + 6);
                break;
            case "duminica":
                System.out.println(7);
                break;
            default:
                System.out.println("Nu exista nicio zi cu denumirea aceasta!");
        }
    }
}
