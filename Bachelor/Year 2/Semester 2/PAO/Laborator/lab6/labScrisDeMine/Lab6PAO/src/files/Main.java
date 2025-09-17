package files;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("input.txt");
        System.out.println(file.exists());

        file.createNewFile();
        System.out.println(file.exists());

        File dir = new File("folder");
        dir.mkdir();

        File multipleDir = new File("folder1/folder2/folder3");
        System.out.println(multipleDir.mkdirs());

        System.out.println(file.getAbsolutePath());

        File fileInsideFolder = new File(multipleDir.getAbsolutePath() + "/fileInFolder.txt");
        fileInsideFolder.createNewFile();
    }
}
