package streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StreamCreationExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("string1");
        list.add("string2");
        list.add("string3");
        list.add("string4");

        for (String element : list) {
            System.out.println(element);
        }

        // stream - metoda pe care o au toate colectiile
        // collection .stream()
        System.out.println(list.stream().count()); // 4
        list.stream()
                .filter(s -> s.equals("string1"))
                .forEach(s -> System.out.println(s));

        System.out.println("After filter");
        System.out.println(list); // [string1, string2, string3, string4

        Stream<String> stream = Stream.of("value1", "value2", "value3");

        stream.forEach(System.out::println);
        // value1
        // value2
        // value3

        // Stream.of an object array
        String[] arrayOfStrings = new String[]{"v1", "v2"};
        Stream<String> stream1 = Stream.of(arrayOfStrings);

        // Arrays.stream for a primitive array
        int[] arrayOfPrimitives = new int[]{1, 2, 3};
        Arrays.stream(arrayOfPrimitives).forEach(System.out::println);
        // 1
        // 2
        // 3

        System.out.println();

        Stream<String> stream3 = Stream.of("element1", "element2");
        Stream<String> filteredStream = stream3.filter(e -> e.equals("element1"));

        // generate
        Supplier<Integer> randomNumberSupplier = () -> (int) (Math.random() * 100);
        Stream<Integer> integerStream = Stream.generate(randomNumberSupplier);
        integerStream
                .limit(10)
                .forEach(System.out::println);

        System.out.println();

        // iterate
        Stream<Integer> integerStream2 = Stream.iterate(0, i -> i + 1);
        integerStream2
                .limit(5)
                .forEach(System.out::println);
        // 0
        // 1
        // 2
        // 3
        // 4
        // 5
    }
}
