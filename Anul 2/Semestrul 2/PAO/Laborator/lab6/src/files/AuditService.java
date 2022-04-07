package files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AuditService {


    public static void audit(String method, String action) throws IOException {
        // Instantiem FileWriter cu append=true pentru a adauga continut dupa cel existent
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("audit.txt", true));
        bufferedWriter.write(method + " " + action + "\n");
        bufferedWriter.close();
    }
}
