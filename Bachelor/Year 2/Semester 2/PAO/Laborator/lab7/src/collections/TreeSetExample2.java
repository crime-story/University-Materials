package collections;

import java.util.TreeSet;

public class TreeSetExample2 {
    public static void main(String[] args) {
        // TreeSet is a sorted set. Compare by implementing Comparable interface
        TreeSet<Person> set = new TreeSet<>(new PersonComparator());
        Person person1 = new Person("Popescu", 30);
        Person person2 = new Person("Ionescu", 20);
        Person person3 = new Person("Xulescu", 10);

        set.add(person1);
        set.add(person2);
        set.add(person3);

        System.out.println(set);
    }
}
