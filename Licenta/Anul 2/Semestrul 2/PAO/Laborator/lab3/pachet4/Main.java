package pachet4;

public class Main {

    public static void main(String[] args) {

        Angajat persoana1 = new Angajat("Popescu", "Ion");
        Persoana persoana2 = new Client("Georgescu", "Vaslie");

        persoana1.afiseazaPersoana();
        persoana2.afiseazaPersoana();

        persoana1.afiseazaTip();
    }
}
