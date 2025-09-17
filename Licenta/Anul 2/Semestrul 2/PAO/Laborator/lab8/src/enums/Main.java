package enums;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Car> list = new ArrayList<>();
        Car car1 = new Car("Ford", Color.WHITE);
        Car car2 = new Car("BMW", Color.PURPLE);

        list.add(car1);
        list.add(car2);

        for (Car car : list) {
            System.out.println(car.getColor().isPrimary());
        }

    }
}
