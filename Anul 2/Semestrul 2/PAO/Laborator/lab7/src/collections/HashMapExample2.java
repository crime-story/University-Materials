package collections;

import java.util.HashMap;
import java.util.Map;

public class HashMapExample2 {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        map.put(3, "trei");

        System.out.println(map); // afiseaza {1=one, 2=two, 3=trei}

        map.put(4, "numarMare");
        map.put(5, "numarMare");
        map.put(6, "numarMare");

        map.put(null, "ValoareNull");

        map.put(7, null);
        map.put(8, null);

        System.out.println(map);
    }
}
