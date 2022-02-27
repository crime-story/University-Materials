package Exercitii;

public class Exercitiul2 {
    public static void main(String[] args) {
        System.out.println("Numerele care sunt divizible cu 3 intre 1 si 20 sunt: ");
        for (int i = 1; i <= 20; i++)
            if (i % 3 == 0)
                System.out.print(i + " ");
    }
}
