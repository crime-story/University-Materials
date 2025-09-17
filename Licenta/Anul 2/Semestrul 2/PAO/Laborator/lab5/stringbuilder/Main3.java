package stringbuilder;

public class Main3 {

    // stringbuilder replace

    public static void main(String[] args) {

        StringBuilder stringBuilder = new StringBuilder("donkey");
        stringBuilder.replace(0,2, "test");
        stringBuilder.replace(7,8, "Y");

        System.out.println(stringBuilder.toString());
    }
}
