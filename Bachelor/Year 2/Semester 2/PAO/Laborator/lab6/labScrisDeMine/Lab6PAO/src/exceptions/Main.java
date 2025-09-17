package exceptions;

public class Main {
    public static void main(String[] args) {
        System.out.println("Method start");

        // daca apelam o metoda de pe o referinta care da catre null si nu catre un obiect instantiat
        // vom primii un nullptr
        String s = null;
        s.split(" "); // primim o exceptie

        System.out.println("Method end");
    }

}
