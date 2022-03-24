package interfete;

public interface PremiumProduct {

    int getDiscount();

    default boolean isShippingFreeForEasyBox(){
        return true;
    }

    static Float getDiscountedPrice(Float price, int discount){
        return price * (100 - discount) / 100;
    }
}
