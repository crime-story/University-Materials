package exceptions;

import java.util.Scanner;

public class CNPService {

    public static void main(String[] args) {

        String line = null;
        do {
            line = citesteCNP();
        }
        while (line == null);

        System.out.println("CNP valid: " + line);
    }

    private static String citesteCNP() {

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            validateCNP(line);
        } catch (CNPLengthValidationException e) {
            System.out.println("Lungime incorecta: "+e.getLength());
            return null;
        } catch (CNPValidationException e) {
            System.out.println("CNP Validation exception");
            return null;
        } catch (ValidationException e) {
            System.out.println("Validation exception occured");
            return null;
        } finally {
            System.out.println("Afisam text chiar daca a fost o exceptie");
        }
        return line;
    }

    private static void validateCNP(String line) throws ValidationException {

        if (line.length() != 13) {
            throw new CNPLengthValidationException(line.length());
        }

        if (!line.matches("[0-9]+")) {
            throw new CNPValidationException();
        }
    }
}
