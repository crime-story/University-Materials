import java.util.Scanner;

/*
    Scanner
 */
public class Exercitiul2 {

    public static void main(String[] args) {

        String numeUtilizator;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti numele");
        numeUtilizator = scanner.nextLine();
        System.out.println("Numele introdus: "+ numeUtilizator);

        System.out.println("Introduceti varsta");
        int varsta = scanner.nextInt();
        System.out.println("Varsta introdusa: "+ varsta);
    }

}
