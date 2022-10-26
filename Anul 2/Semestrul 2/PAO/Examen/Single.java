import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Singleton {
    private final String csvName;
    private final List<Persoana> l;
    private static Singleton instance;

    private Singleton(String csvName, List<Persoana> l) {
        this.csvName=csvName;
        this.l=l;
    }

    public static Singleton getInstance(String csvName, List<Persoana> l) {
        if (instance == null) {
            instance = new Singleton( csvName, l);
        }
        return instance;
    }

    public void read() {
        try {
            Scanner in = new Scanner(new FileInputStream(csvName));

            while (in.hasNext()) {
                try {
                    String line = in.nextLine();
                    String[] cuv = line.split("[ ,]+");

                    Persoana persoana = new Persoana(cuv[0], Integer.parseInt(cuv[1]));

                    l.add(persoana);
                }catch (Exception e){
                    System.out.print(e.toString());
                }
            }

            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void write() {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(csvName));

            for(Persoana p : l){
                out.println(p.toString());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

public class Single {
    public static void main(String[] args) {
        List<Persoana> l= new ArrayList<>();
        Singleton s = Singleton.getInstance("ceva.csv", l);
        s.read();

        if(l.isEmpty()) {
            l.add(new Persoana("adi", 3));
            l.add(new Persoana("roby", 7));
            s.write();
        }else{
            System.out.println(l);
        }
    }
}
