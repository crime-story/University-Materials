package exceptions;

public class Main2 {

    public static void main(String[] args) {
        printString("");
    }

    public static void printString(String s) {

        try {
            if (s.isEmpty()) {
                throw new Exception();
            }
            System.out.println("end try");
        } catch (Exception e) {
            System.out.println("Caught exception");
        }

    }
}
