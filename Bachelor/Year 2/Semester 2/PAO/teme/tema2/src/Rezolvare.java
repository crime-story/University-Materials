import java.util.Scanner;

public class Rezolvare {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Introduceti n: ");
        int n = keyboard.nextInt();
        if (n > 0) {
            Persoana[] persoane = new Persoana[n];

            for (int i = 0; i < n; i++) {
                System.out.print("Introduceti numele: ");
                String nume_curent = keyboard.next();

                System.out.print("Introduceti varsta: ");
                int varsta_curenta = keyboard.nextInt();

                Persoana persoana = new Persoana();
                persoana.setData(nume_curent, varsta_curenta);
                persoane[i] = persoana;
            }

            for (int i = n - 1; i >= 0; i--) {
                System.out.println(persoane[i].obtine_informatii());
            }
        } else {
            System.out.println("N trebuie sa fie mai mare ca 0!");
        }
    }
}
