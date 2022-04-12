package collections;

import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        // 1 -> one
        // 2 -> two
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        System.out.println("Method1:");

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("Method2:");

        for (Integer key : map.keySet()) {
            System.out.println(key + " -> " + map.get(key));
        }

        System.out.println("Values: " + map.values());
    }
}
