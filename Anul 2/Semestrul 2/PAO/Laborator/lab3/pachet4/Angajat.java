package pachet4;

public class Angajat extends Persoana {

    public Angajat() {

    }

    public Angajat(String nume, String prenume) {
        super(nume, prenume);
    }

    @Override
    public void afiseazaPersoana() {
        System.out.println("Angajat: " + nume + " " + prenume);
    }

    public static void afiseazaTip() {
        System.out.println("Angajat");
    }
}
