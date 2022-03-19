package pachet5;

public class Produs {

    public int pret;
    public String nume;


    public void seteaza(int pret) {
        this.pret = pret;
    }

    public void seteaza(int pret, String nume) {
        this.pret = pret;
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "Produs{" +
                "pret=" + pret +
                ", nume='" + nume + '\'' +
                '}';
    }
}
