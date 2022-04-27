package functionalInterfaces;

import java.util.function.Function;

public class FunctionExample {

    // in: Object T
    // out: Object R

    public static void main(String[] args) {

        Function<String, Integer> lengthFunction = (String word) -> word.length();
        System.out.println( lengthFunction.apply("string"));

    }
}
