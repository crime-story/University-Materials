class Adresa{
    String adresa;

    public Adresa(String adresa) {
        this.adresa = adresa;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
}

final class Firma{
    private final String nume;
    private final int nrAngajati;
    private final double profit;
    private final Adresa adresa;

    public Firma(String nume, int nrAngajati, double profit, Adresa adresa) {
        this.nume = nume;
        this.nrAngajati = nrAngajati;
        this.profit = profit;
        this.adresa = new Adresa(adresa.getAdresa());
    }

    public String getNume() {
        return nume;
    }

    public int getNrAngajati() {
        return nrAngajati;
    }

    public double getProfit() {
        return profit;
    }

    public Adresa getAdresa() {
        return new Adresa(adresa.getAdresa());
    }
}

public class Imutabil {
    public static void main(String[] args) {
        Adresa adresa = new Adresa("tavi");
        Firma firma = new Firma("tav", 1, 1, adresa);
        adresa.setAdresa("ha ha ha");
        firma.getAdresa().setAdresa("ha ha ha");
        System.out.println(firma.getAdresa().getAdresa());
    }
}
