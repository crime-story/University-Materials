package package2;

import java.util.Arrays;

public record ReceiptRecord(String date, Product[] products, String shopName) {

    @Override
    public String toString() {
        return "ReceiptRecord{" +
                "date='" + date + '\'' +
                ", products=" + Arrays.toString(products) +
                ", shopName='" + shopName + '\'' +
                '}';
    }
}
