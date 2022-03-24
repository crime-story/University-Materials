package package2;

public class Main {

    public static void main(String[] args) {

        Product product1 = new Product("Cola", 3f);
        Product product2 = new Product("Fanta", 2.5f);
        Product[] products = new Product[]{product1, product2};

        Receipt receipt = new Receipt("17.03.2022", products, "Auchan");

        System.out.println(receipt);

        ReceiptRecord record = new ReceiptRecord("17.03.2022", products, "Auchan");
        System.out.println(record);


    }
}
