package enums;

public class EnumEqualityExample {

    public static void main(String[] args) {

        Color color1 = Color.WHITE;
        Color color2 = Color.WHITE;

        System.out.println(color1 == color2);
        System.out.println(color1.equals(color2));
    }
}
