package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TransientExample {

    public static void main(String[] args) throws Exception{

        Car car1 = new Car("Ford", "white", new Owner());
        System.out.println("Car1: " + car1);


        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("car.ser"));
        objectOutputStream.writeObject(car1);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("car.ser"));
        Car carFromFile = (Car) objectInputStream.readObject();
        System.out.println("From File:" + carFromFile);
    }
}
