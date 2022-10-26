import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FirNumarare extends Thread {
    private final String numeFisier;
    private List<String> s;

    public FirNumarare(String numeFisier) {
        this.numeFisier = numeFisier;
        this.s = new ArrayList<>();
    }

    public List<String> getS() {
        return s;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(new File(numeFisier));

            while (scanner.hasNextLine()) {
                String linie = scanner.nextLine();
                String[] prop = linie.split("[1234567890.,;:?! \n]+");

                for (String cuv : prop)
                    s.add(cuv);
                //    Collections.addAll(s, prop);
                //   s.addAll(List.of(prop));
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

public class NumarareCuvinteFisier {

    public static void main(String[] args) throws InterruptedException {

        FirNumarare firNumarare_1 = new FirNumarare("agentieRentSale_1.txt");
        FirNumarare firNumarare_2 = new FirNumarare("agentieRentSale_2.txt");

        firNumarare_1.start();
        firNumarare_1.join();

        firNumarare_2.start();
        firNumarare_2.join();

        List<String> l = firNumarare_1.getS();
        List<String> l2 = firNumarare_2.getS();

        for (String cuv : l2)
            l.add(cuv);

        System.out.println(l.
                stream()
                .filter(cuv -> cuv.length() >= 1)
                .distinct()
                .toList());

    }
}
