package pachet4;

public abstract class Persoana {

    protected String nume;
    protected String prenume;

    public Persoana() {

    }

    public Persoana(String nume, String prenume) {
        this.nume = nume;
        this.prenume = prenume;
    }

    public void afiseazaPersoana() {
        System.out.println("Persoana: " + nume + " " + prenume);
    }

    public static void afiseazaTip() {
        System.out.println("Persoana");
    }
}
