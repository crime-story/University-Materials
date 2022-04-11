package files;

import java.io.*;
import java.util.Scanner;

public class BufferedFileWriteAndRead {
    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("bufferedInput.txt"));
        bufferedWriter.write("This is line 1.");
        bufferedWriter.newLine();
        bufferedWriter.write("This is line 2.");

        bufferedWriter.close();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("bufferedInput.txt"));
        while (bufferedReader.ready()) {
            System.out.println(bufferedReader.readLine());
        }

        bufferedReader.close();

        System.out.println("Scanner:");
        Scanner scanner = new Scanner(new File("bufferedInput.txt"));
        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }

        scanner.close();

    }
}
