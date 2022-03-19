package package4;

public class Main {

    public static void main(String[] args) {

        //               0123456789
        String string = "This is a string";

        System.out.println(string.toUpperCase());
        System.out.println(string.toLowerCase());
        System.out.println(string.length());

        System.out.println(string.substring(0));
        System.out.println(string.substring(2));
        System.out.println(string.substring(2, 6)); // [0,6)
//        System.out.println(string.substring(2, 20));

        System.out.println(string.charAt(1));

        String s1 = "e";
        String s2 = "abcd";
        System.out.println(s1.compareTo(s2));

        String string1 = "abecedar";
        String string2 = "AbeCedar";
        System.out.println(string1.equalsIgnoreCase(string2));

        String s = "   car   ";
        System.out.println(s.trim());


    }
}
