package collections;

import java.util.LinkedHashSet;
import java.util.Set;

public class LinkedHashSetExample {
    public static void main(String[] args) {
        // Set<String> set = new LinkedHashSet<>();
        LinkedHashSet<String> set = new LinkedHashSet<>();

        set.add("1");
        set.add("2");
        set.add("3");
        set.add("3");

        System.out.println("size: " + set.size());
        System.out.println("set: " + set);
    }
}
