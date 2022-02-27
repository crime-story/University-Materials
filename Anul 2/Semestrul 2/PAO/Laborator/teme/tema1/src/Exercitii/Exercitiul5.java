package Exercitii;

import java.lang.Math;

public class Exercitiul5 {
    public static void main(String[] args) {
        int tablou[] = new int[20];
        for (int i = 0; i < tablou.length; i++)
            tablou[i] = (int) Math.pow(i, 2);
        // sau tablou[i] = i * i;

        System.out.println("Valorile din array sunt: ");
        for(int i = tablou.length - 1 ; i >= 0 ; i--)
            System.out.print(tablou[i] + " ");
    }
}
