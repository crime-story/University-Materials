package package1;

public class Main {

    public static void main(String[] args) {

        Car car1 = new Car("1", "Ford", 2022);
        Car car2 = new Car("1", "Ford", 2012);

        System.out.println(car1.hashCode());
        System.out.println(car2.hashCode());


    }
}
