package exceptions;

import java.util.Scanner;

public class MathService {
    public static void main(String[] args) {
        // 1 2 3 4
        try {
            citesteSiAfiseazaMediaAritmetica();
        } catch (Exception e) {
            try {
                citesteSiAfiseazaMediaAritmetica();
            } catch (Exception e2) {
                System.out.println("2 linii gresite introduse!");
            }
        }
    }

    public static void citesteSiAfiseazaMediaAritmetica() throws Exception {
        MathService mathService = new MathService();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numerele!");
        String line = scanner.nextLine();
        String[] valori = line.split(" ");
        System.out.println(mathService.mediaAritmetica(valori));
    }

    public Float mediaAritmetica(String[] valori) throws Exception {
        Float suma = suma(valori);
        int numarElemente = valori.length;
        return suma / numarElemente;
    }

    private Float suma(String[] valori) { // private Float suma(String[] valori) throws Exception {
        Float suma = 0f;
        for (String v : valori) {
            Float f = conversieFloat(v);
            suma += f;
        }
        return suma;
    }

    private Float conversieFloat(String v) { // private Float conversieFloat(String v) throws Exception
        /*
        Float returnValue = 0f;
        try {
            returnValue = Float.valueOf(v);
            return returnValue;
        } catch (NumberFormatException e) {
            System.out.println("Valoare nu este un float: " + e.getMessage());
            return 0f;
        }
        */

        /*
        Float returnValue = 0f;
        try {
            returnValue = Float.valueOf(v);
            return returnValue;
        } catch (NumberFormatException e) {
            throw new Exception(e.getMessage());
        }
        */

        Float returnValue = 0f;
        returnValue = Float.valueOf(v);
        return returnValue;
    }
}
