import java.io.*;

class Persoana implements Serializable{
    String nume;
    int varsta;

    public Persoana(String nume, int varsta) {
        this.nume = nume;
        this.varsta = varsta;
        System.out.println("Constructor");
    }

    @Override
    public String toString() {
        return nume + "," +  varsta;
    }
}

public class Serializare {
    public static void main(String[] args) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("persoana.ser"));

        Persoana p =new Persoana("ana", 10), q=p;

        oos.writeObject(q);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("persoana.ser"));

        Persoana r = (Persoana) ois.readObject();
        ois.close();

        r.varsta=20;
        System.out.println(r.varsta);
        System.out.println(p.varsta);
    }
}
