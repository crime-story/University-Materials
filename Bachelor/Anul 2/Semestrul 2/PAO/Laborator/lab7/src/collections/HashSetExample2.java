package collections;

import java.util.HashSet;
import java.util.Set;

public class HashSetExample2 {
    public static void main(String[] args) {
        Person person1 = new Person("Popescu", 30);
        Person person2 = new Person("Ionescu", 25);

        System.out.println("person1 hashcode: " + person1.hashCode());
        System.out.println("person2 hashcode: " + person2.hashCode());

        Person person3 = new Person("Popescu", 20);
        System.out.println("person3 hashcode: " + person3.hashCode());

        System.out.println("person1 equals person3: " + person1.equals(person3));

        Set<Person> set = new HashSet<>();
        set.add(person1);
        set.add(person2);
        set.add(person3);

        System.out.println("Set size: " + set.size());
        System.out.println("Set: " + set);
    }
}
