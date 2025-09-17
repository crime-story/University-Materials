package functionalInterfaces;

import java.util.function.Supplier;

public class SupplierExample {

    // in: nothing
    // out: Object T

    public static void main(String[] args) {

        Supplier<Integer> randomSupplier = () -> (int) (Math.random() * 100);

        System.out.println(randomSupplier.get());
    }
}
