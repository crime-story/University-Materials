package streamProcessing;

import java.util.ArrayList;
import java.util.List;

public class TerminalOperationsExample {
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

        System.out.println(products.stream()
                .max((p1, p2) -> p1.getPrice() - p2.getPrice()));

        // Optional[Product{name='Produs2', price=50}]

        System.out.println(products.stream()
                .min((p1, p2) -> p1.getPrice() - p2.getPrice()));

        // Optional[Product{name='Produs1', price=10}]
    }
}
