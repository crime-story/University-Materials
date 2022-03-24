package interfete;

public class Computer extends Product implements PremiumProduct, Comparable{

    public Computer(Float price){
        super(price);
    }

    @Override
    public int getDiscount() {
        return 20;
    }

    @Override
    public boolean isShippingFreeForEasyBox() {
        return false;
    }

    @Override
    public int compareTo(Object o) {

        Computer computer = (Computer) o;
        return Float.compare(price, computer.price);

    }
}
