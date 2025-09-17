package pachet3;

public abstract class Animal {

    String nume;
    int varsta;

    public void afiseazaAnimal() {
        System.out.println(nume + " " + varsta);
    }

    public abstract void afiseazaTip();
}
