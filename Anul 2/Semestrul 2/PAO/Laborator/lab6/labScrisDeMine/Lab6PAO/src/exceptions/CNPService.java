package exceptions;

import java.util.Scanner;

public class CNPService {
    public static void main(String[] args) {
        String line = null;
        do {
            line = citesteCNP();
        } while (line == null);

        System.out.println("CNP Valid: " + line);
    }

    private static String citesteCNP() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        // se pun mai intai subclasele la exceptii si dupa clasele parinte, alftel intra direct pe parinte si nu mai intra pe subclase
        try {
            validateCNP(line);
        } catch (CNPLengthValidationException e) {
            System.out.println("Lungime incorecta! " + e.getLength());
            return null;
        } catch (CNPValidationException e) {
            System.out.println("CNP ValidationException!");
            return null;
        } catch (ValidationException e) {
            System.out.println("ValidationException occured!");
            return null;
        } finally {
            //scanner.close(); // se inchide cand nu il mai folosim
            System.out.println("Afisam text chiar daca a fost o exceptie!");
        }
        return line;
    }

    private static void validateCNP(String line) throws ValidationException {
        if (line.length() != 13) {
            //throw new RuntimeException("asdasd");
            throw new CNPLengthValidationException(line.length());
        }
        if (!line.matches("[0-9]+")) {
            throw new CNPValidationException();
        }
    }
}
