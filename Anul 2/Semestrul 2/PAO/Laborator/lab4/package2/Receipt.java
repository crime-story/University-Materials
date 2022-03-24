package package2;

import java.util.Arrays;

public class Receipt {

    private String date;
    private Product[] products;
    private String shopName;

    public Receipt(String date, Product[] products, String shopName) {
        this.date = date;
        this.products = products;
        this.shopName = shopName;
    }

    public String getDate() {
        return date;
    }

    public Product[] getProducts() {
        return products;
    }

    public String getShopName() {
        return shopName;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "date='" + date + '\'' +
                ", products=" + Arrays.toString(products) +
                ", shopName='" + shopName + '\'' +
                '}';
    }
}
