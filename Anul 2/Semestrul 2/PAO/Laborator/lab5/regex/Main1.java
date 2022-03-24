package regex;

public class Main1 {

    public static void main(String[] args) {

        String exemplu = "exemplu";
        System.out.println(exemplu.matches("exemplu"));

        // a - doar litera a
        // (a|b) - a sau b
        // [ab] - a sau b
        // (abc|def) - abc sau def
        // [ab]+ - a sau b, cel putin un caracter
        // [ab]*
        // .*

        System.out.println( "abc".matches(".*"));

        System.out.println("a.b".matches("a\\.."));
    }
}
