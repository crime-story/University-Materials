package interfete;

public class Main {

    public static void main(String[] args) {

        ShopService shopService = new ShopService();

        Product computer = new Computer(1000f);
        Product keyboard = new Keyboard(200f);
        Product mouse = new Mouse(150f);

        Product[] products = new Product[3];
        products[0] = computer;
        products[1] = keyboard;
        products[2] = mouse;

        shopService.setProducts(products);

        System.out.println("For premium user:");
        shopService.printPrices(true);

        System.out.println();
        System.out.println("For standard user:");
        shopService.printPrices(false);

        Computer c1 = new Computer(100f);
        Computer c2 = new Computer(200f);

        System.out.println(c1.compareTo(c2));

    }
}
