package collections;

import java.util.HashSet;
import java.util.Set;

public class HashSetExample {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        // Setul nu admite duplicate!!!
        set.add("string1");
        set.add("string2");
        set.add("string3");
        set.add("string3");

        System.out.println("Set size: " + set.size());
        System.out.println("Set: " + set);
        set.remove("string2");
    }
}
