package interfete;

public class ShopService {

    private Product[] products;

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public void printPrices(boolean isUserPremium) {

        for (Product product : products) {
            if (isUserPremium && product instanceof PremiumProduct) {
                float discountedPrice = PremiumProduct.getDiscountedPrice(product.price, ((PremiumProduct) product).getDiscount());
                System.out.println("Premium price: " + discountedPrice);
                System.out.println("Free shipping for EB: " + ((PremiumProduct) product).isShippingFreeForEasyBox());
            } else {
                System.out.println("Standard price: " + product.price);
            }
        }
    }
}
