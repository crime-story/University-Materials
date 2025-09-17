import java.util.Arrays;

public class Exercitiul1 {

    public static void main(String[] args) {

        // initializare
        int[] array = new int[5];

        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();

        int[] array2 = new int[]{1, 2, 3, 4, 5};

        for (int i = 0; i < array2.length; i++) {
            System.out.print(array2[i] + " ");
        }

        // exception
        System.out.println(array[10]);
    }
}
