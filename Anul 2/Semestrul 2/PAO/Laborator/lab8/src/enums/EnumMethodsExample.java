package enums;

import java.util.Scanner;

public class EnumMethodsExample {

    public static void main(String[] args) {

        for (Color c : Color.values()) {
            System.out.println(c.name() + " " + c.isPrimary());
        }

        Scanner s = new Scanner(System.in);

        while (true) {
            String colorString = s.next();
            Color color = Color.valueOf(colorString);
            System.out.println(color);
        }

        // valueOf("WHITE") -> Color.WHITE
        // valueOf("white") -> exception
    }
}
