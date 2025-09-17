package stringbuilder;

public class Main1 {

    public static void main(String[] args) {

        //initialize
        StringBuilder stringBuilder = new StringBuilder("init");

        stringBuilder.append("ialize");
        System.out.println(stringBuilder.toString());

//        stringBuilder.reverse();
        System.out.println(stringBuilder.toString());

        System.out.println();
        System.out.println(stringBuilder.compareTo(new StringBuilder("asd")));

        System.out.println(stringBuilder.indexOf("n"));

        stringBuilder.insert(2,"A");
        System.out.println(stringBuilder.toString());
    }
}
