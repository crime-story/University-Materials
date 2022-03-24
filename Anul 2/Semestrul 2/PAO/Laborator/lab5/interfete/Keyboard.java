package interfete;

public class Keyboard extends Product implements PremiumProduct{

    public Keyboard(Float price) {
        super(price);
    }

    @Override
    public int getDiscount() {
        return 10;
    }
}
