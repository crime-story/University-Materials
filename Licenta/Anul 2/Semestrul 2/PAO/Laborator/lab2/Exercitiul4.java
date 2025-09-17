public class Exercitiul4 {

    // 1 2 3 4 5
    // 0 0 0 0 0
    // 0 0 9 0 0
    // 0 0 0 0 0

    public static void main(String[] args) {

        // vector bidimensional
        int[][] array = new int[4][5];
        array[0] = new int[]{1, 2, 3, 4, 5};
        array[2][2] = 9;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }
}
