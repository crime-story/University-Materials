package exercitiul1;

public class Exercitiul8 {
    public static void main(String[] args) {
        int array[] = new int[10];

//        array[0] = 5;

        for (int i = 0; i < 10; i++) {
            array[i] = 100 + i;
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("Valoarea elementului de pe pozitia " + i + " este " + array[i]);
        }

        for (int element : array) {
            System.out.println(element);
        }

        System.out.println("\n" + array[0]);

    }
}
