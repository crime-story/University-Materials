package serialization;

import java.io.*;

public class SerializationExample {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Person person = new Person("Popescu Ion", 25);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("output.ser"));
        objectOutputStream.writeObject(person);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("output.ser"));
        Person personFromFile = (Person) objectInputStream.readObject();
        System.out.println(personFromFile);
    }
}
