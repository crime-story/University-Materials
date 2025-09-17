package exceptions;

public class Main {

    // Aruncarea exceptiilor opreste executia codului
    public static void main(String[] args) {

        System.out.println("Method start");

        String s = null;
        s.split(" ");

        System.out.println("Method end");
    }
}
