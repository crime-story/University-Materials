package collections;

import java.util.ArrayList;

public class ArrayListWithCarExample {
    public static void main(String[] args) {
        ArrayList<Car> list = new ArrayList<>();
        Car car1 = new Car("Ford", "white");
        Car car2 = new Car("BMW", "black");

        list.add(car1);
        list.add(car2);

        for (Car car : list) {
            System.out.println(car);
        }

        // objects must override equals to be able to use list.contains
        Car car3 = new Car("Ford", "white");
        System.out.println("List contains car3? " + list.contains(car3)); // initial afiseaza false pt ca nu aveam equals definit
        // dupa ce ne-am definit equals in clasa Car, va afisa true
    }
}
