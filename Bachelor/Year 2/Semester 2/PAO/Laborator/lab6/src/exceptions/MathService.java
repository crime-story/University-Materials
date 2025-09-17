package exceptions;

import java.util.Scanner;

public class MathService {

    public static void main(String[] args) {


        // 1 2 3 4
        try {
            citesteSiAfiseazaMediaAritmetica();
        } catch (Exception e) {
            try {
                // incearca citirea a 2-a oara daca sunt exceptii
                citesteSiAfiseazaMediaAritmetica();
            } catch (Exception e2) {
                System.out.println("2 linii gresite introduse. Final de program");
            }
        }
    }

    public static void citesteSiAfiseazaMediaAritmetica() throws Exception {
        MathService mathService = new MathService();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numerele:");
        String line = scanner.nextLine();
        String[] valori = line.split(" ");

        mathService.medieAritmetica(valori);
    }

    public Float medieAritmetica(String[] valori) throws Exception {

        Float suma = suma(valori);
        int numarElemente = valori.length;
        return suma / numarElemente;
    }

    private Float suma(String[] valori) {
        Float suma = 0f;
        for (String v : valori) {
            Float f = conversieFloat(v);
            suma += f;
        }
        return suma;
    }

    private Float conversieFloat(String v) {
        //  Nu putem trata exceptia aici pentru ca nu avem suficient context. Alegem sa aruncam exceptia (nu prindem NumberFormatException)

        //        Float returnValue = 0f;
        //        try {
        //            returnValue = Float.valueOf(v);
        //            return returnValue;
        //        } catch (NumberFormatException e) {
        //            System.out.println("Valoarea nu este un float " + e.getMessage());
        //            return 0f;
        //        }

        Float returnValue = 0f;
        returnValue = Float.valueOf(v);
        return returnValue;
    }
}
