package lambda;

import java.util.function.BiFunction;

public class LambdaExample {
    public static void main(String[] args) {
        // Consumer -> no return type, 1 param
        // Supplier -> return type, no param
        // Predicate -> return boolean, T param
        // Function -> 1 return, 1 param
        // Bifunction -> 1 return, 2 param

        BiFunction<String, String, String> biFunction = (x, y) -> x + y;

    }
}
