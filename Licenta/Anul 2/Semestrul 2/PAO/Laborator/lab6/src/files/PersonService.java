package files;

import java.io.*;
import java.util.Scanner;

/**
 * Citire obiect din fisier
 * Scriere audit in fisier
 */
public class PersonService {

    public static void main(String[] args) throws IOException {

        AuditService.audit("main", "start");

        File personsFile = new File("persons.txt");
        if (!personsFile.exists()) {
            throw new FileNotFoundException("Nu exista fisierul");
        }

        Scanner scanner = new Scanner(personsFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            Person person = new Person(parts[0], Integer.parseInt(parts[1].trim()));
            System.out.println(person);
        }

        AuditService.audit("main", "end");
    }


}
