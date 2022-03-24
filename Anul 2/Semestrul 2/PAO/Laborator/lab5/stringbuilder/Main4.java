package stringbuilder;

public class Main4 {

    // replace all 'a' character from String with 'A'
    public static void main(String[] args) {

        StringBuilder stringBuilder = new StringBuilder("abracadabra");

        while(stringBuilder.toString().contains("a")) {
            int indexOfa = stringBuilder.indexOf("a");
            stringBuilder.replace(indexOfa, indexOfa + 1, "A");
        }
        System.out.println(stringBuilder);
    }
}
