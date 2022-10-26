import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class FirNumarare extends Thread {
    private final String numeFisier;
    private final int smin;
    private final int cmin;
    private int nrAparitii;

    public FirNumarare (String numeFisier, int smin, int cmin) {
        this.numeFisier = numeFisier;
        this.smin = smin;
        this.cmin =cmin;
        this.nrAparitii = 0;
    }

    public int getNrAparitii() {
        return nrAparitii;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(new File(numeFisier));

            while(scanner.hasNextLine()) {
                String linie = scanner.nextLine();
                String[] prop = linie.split("[.,;:?! \n]+");
                int c = Integer.parseInt(prop[2]);
                int s = Integer.parseInt(prop[3]);
                if(c>=cmin&&s>=smin){
                        nrAparitii++;

                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

public class NumarareCuvinteFisier {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int s = scanner.nextInt();
        int c = scanner.nextInt();

        FirNumarare firNumarare_1 = new FirNumarare("agentieRentSale_1.txt", s, c);
        FirNumarare firNumarare_2 = new FirNumarare("agentieRentSale_2.txt", s,c);

        firNumarare_1.start();
        firNumarare_1.join();

        firNumarare_2.start();
        firNumarare_2.join();

        int nrTotalAparitii = firNumarare_1.getNrAparitii() + firNumarare_2.getNrAparitii();

        System.out.println(nrTotalAparitii);
    }
}
