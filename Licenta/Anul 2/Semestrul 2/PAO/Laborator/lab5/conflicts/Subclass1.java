package conflicts;

public class Subclass1 extends Superclass implements AnInterface{

    public static void main(String[] args) {

        Subclass1 s = new Subclass1();
        System.out.println(s.getValue());
    }
}
