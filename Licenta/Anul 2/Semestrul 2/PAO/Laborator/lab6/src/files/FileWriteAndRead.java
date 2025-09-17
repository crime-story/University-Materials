package files;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriteAndRead {

    public static void main(String[] args) throws IOException {

        // Scriere in fisier folosind FileWriter
        FileWriter fileWriter = new FileWriter("input.txt");
        fileWriter.write("This is the first line");
        fileWriter.write("\n");
        fileWriter.write("This is the second line");
        fileWriter.close();

        // Scriere in fisier folosind FileReader
        FileReader fileReader = new FileReader("input.txt");

        char[] charArray = new char[250];
        fileReader.read(charArray);
        for (int i = 0; i < charArray.length; i++) {
            System.out.print(charArray[i]);
        }

    }
}
