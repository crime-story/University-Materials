package lambda;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodReferenceExample {
    public static void main(String[] args) {
        Consumer<String> consumer = s -> System.out.println(s);

        consumer.accept("Hello world!"); // prints Hello world!

        Consumer<String> consumer2 = System.out::println;
        consumer2.accept("Hello world2!"); // prints Hello world2!

        // static method reference
        Consumer<String> consumer3 = MethodReferenceExample::staticMethod;

        // instance method reference
        Person person = new Person();
        Supplier<String> supplier = person::getName;
        // System.out.println(supplier.get());

        // constructor reference
        Supplier<Person> supplier1 = Person::new;
        Function<String, Person> function = Person::new;
        BiFunction<String, Integer, Person> biFunction = Person::new;

        Person person1 = supplier1.get();
        Person person2 = function.apply("Popescu Ion");
        Person person3 = biFunction.apply("Popescu Ion", 30);

    }

    public static void staticMethod(String param) {

    }
}
