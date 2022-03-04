import java.util.Arrays;

public class Exercitiul5 {

    public static void main(String[] args) {

        int[] array = new int[]{1, 2, 3, 4, 5, 6};

        // Afisare array

//        for (int i = 0; i < array.length; i++) {
//            System.out.print(array[i] + " ");
//        }
//        System.out.println();

        System.out.println(Arrays.toString(array));

        // Copiere array

//        int[] copyOfArray = new int[array.length];
//        for (int i=0; i< array.length;i++){
//            copyOfArray[i] = array[i];
//        }

        int[] copyOfArray = Arrays.copyOf(array, array.length);
        copyOfArray[0] = 9;
        System.out.println(Arrays.toString(copyOfArray));

        //verificare egalitate array-uri
        System.out.println(Arrays.equals(array, copyOfArray));

        // verificare egalitate array (manual)
        if (array.length != copyOfArray.length) {
            System.out.println("Nu sunt egale");
        } else {
            boolean suntEgale = true;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != copyOfArray[i]) {
                    System.out.println("Nu sunt egale " + array[i] + ", " + copyOfArray[i]);
                    suntEgale = false;
                    break;
                }
            }
            if (suntEgale) {
                System.out.println("Sunt egale");
            }
        }

        // fill
        int[] array3 = new int[5];
        Arrays.fill(array3, 3);
        System.out.println(Arrays.toString(array3));

        //sort
        int[] array4 = new int[]{1, 3, 5, 2, 4};
        Arrays.sort(array4);
        System.out.println(Arrays.toString(array4));


    }
}
