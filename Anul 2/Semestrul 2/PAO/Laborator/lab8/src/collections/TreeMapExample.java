package collections;

import java.util.Map;
import java.util.TreeMap;

public class TreeMapExample {

    public static void main(String[] args) {

        Map<String, String> map = new TreeMap<>();
        map.put("c", "0");
        map.put("a", "1");
        map.put("b", "2");

        System.out.println(map);

    }
}
