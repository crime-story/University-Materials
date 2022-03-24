package stringbuilder;

public class Main2 {

    public static void main(String[] args) {

        System.out.println(method("donkey",2));

        StringBuilder sb = new StringBuilder("donkey");
        System.out.println(sb.delete(0,2).toString());
    }

    public static String method(String source, int index){

        StringBuilder stringBuilder = new StringBuilder(source);
        stringBuilder.deleteCharAt(index);

        return stringBuilder.toString();

    }
}
