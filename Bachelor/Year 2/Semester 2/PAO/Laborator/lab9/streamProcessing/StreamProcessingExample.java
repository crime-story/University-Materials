package streamProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamProcessingExample {
    public static void main(String[] args) {
        Product product1 = new Product("Produs1", 10);
        Product product2 = new Product("Produs2", 50);
        Product product3 = new Product("Produs3", 30);
        Product product4 = new Product("Produs4", 15);
        Product product5 = new Product("Produs4", 15);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);

        product1.setTags(new String[]{"tag1", "tag2"});
        product2.setTags(new String[]{"tag3"});

        products.stream()
                .forEach(System.out::println);

        /*
        // it prints:
        Product{name='Produs1', price=10}
        Product{name='Produs2', price=50}
        Product{name='Produs3', price=30}
        Product{name='Produs4', price=15}
        */

        System.out.println();

        // filter products with price > 20
        Predicate<Product> priceHigherThan20 = product -> product.getPrice() > 20;
        products.stream()
                .filter(priceHigherThan20)
                .forEach(System.out::println);
        /*
        // it prints
        Product{name='Produs2', price=50}
        Product{name='Produs3', price=30}
        */

        System.out.println();

        products.stream()
                .sorted((p1, p2) -> p1.getPrice() - p2.getPrice())
                .forEach(System.out::println);
        /*
        // it prints
        Product{name='Produs1', price=10}
        Product{name='Produs4', price=15}
        Product{name='Produs3', price=30}
        Product{name='Produs2', price=50}
        */

        System.out.println("Limit: ");
        products.stream()
                .limit(1)
                .forEach(System.out::println); // Product{name='Produs1', price=10}

        System.out.println("Distinct: ");
        products.stream()
                .distinct()
                .forEach(System.out::println);
        /*
        // it prints
        Product{name='Produs1', price=10}
        Product{name='Produs2', price=50}
        Product{name='Produs3', price=30}
        Product{name='Produs4', price=15}
        */

        System.out.println("Map: From Product to product name");
        products.stream()
                .map(p -> p.getName())
                .distinct() // se aplica doar pe nume, nu pe produse!
                .forEach(System.out::println);
        /*
        // it prints
        Produs1
        Produs2
        Produs3
        Produs4
        */

        System.out.println("FlatMap");
        products.stream()
                .flatMap(p -> Arrays.stream(p.getTags()))
                .forEach(System.out::println);
        /*
        // it prints
        tag1
        tag2
        tag3
        */

        System.out.println("Map of tags");
        products.stream()
                .map(p -> p.getTags())
                .forEach(array -> {
                    for (int i = 0; i < array.length; i++) {
                        System.out.println(array[i]);
                    }
                });
        // tag1
        // tag2
        // tag3


    }
}
